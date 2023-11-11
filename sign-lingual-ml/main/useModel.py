import os
from openai import OpenAI
import cv2
import numpy as np
import mediapipe as mp
import tensorflow as tf
from tensorflow.keras import backend as K
import gtts
from playsound import playsound
from dotenv import load_dotenv
import asyncio
import threading

# constants
image_width, image_height = 200, 200 

# setting the OpenAI API key
# -----------------------------------------------------------------------
# create a .env file in the same directory as this file
# write: OPENAI_API_KEY=<your api key> in the .env file
# -----------------------------------------------------------------------

# loading environment variables from api.env
load_dotenv('api.env')

client = OpenAI(
    api_key=os.environ['OPENAI_API_KEY']
)

def f1_metric(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    recall = true_positives / (possible_positives + K.epsilon())
    f1_val = 2*(precision*recall)/(precision+recall+K.epsilon())
    return f1_val

# loading the model
# --------------- change the file path to the model you want to use ----------------
model = tf.keras.models.load_model("../models/asl_landmark_detection_model_mediapipe.h5", custom_objects={"f1_metric": f1_metric})
# ----------------------------------------------------------------------------------

# initializing MediaPipe hand solution
mp_hands = mp.solutions.hands
hands = mp_hands.Hands()

# creating a mapping of integer labels to class names
label_map = {
    0: 'A', 1: 'B', 2: 'C', 3: 'D', 4: 'del', 5: 'E',
    6: 'F', 7: 'G', 8: 'H', 9: 'I', 10: 'J', 11: 'K', 12: 'L', 13: 'M', 14: 'N', 15: 'nothing', 16: 'O',
    17: 'P', 18: 'Q', 19: 'R', 20: 'S', 21: 'space', 22: 'T',
    23: 'U', 24: 'V', 25: 'W', 26: 'X', 27: 'Y', 28: 'Z'
}

def predict_asl_letter(landmarks):
    x_landmarks = [landmark.x * image_width for landmark in landmarks.landmark]
    y_landmarks = [landmark.y * image_height for landmark in landmarks.landmark]
    
    # converting landmarks to NumPy array and reshaping for prediction
    landmarks_array = np.array(x_landmarks + y_landmarks).reshape(1, -1)
    
    # using the model to predict the label
    prediction = model.predict(landmarks_array)
    
    # getting the predicted class name
    predicted_label = label_map[np.argmax(prediction)]
    
    # getting the prediction probability
    predicted_prob = np.max(prediction) * 100  # as percentage
    
    return predicted_label, predicted_prob

def correct_typo_play_audio(word):

    global corrected_sequence, context_buffer

    # adding context window for better typo corrections
    context = ' '.join(context_buffer[-5:])  # window size can be adjusted here

    completion = client.chat.completions.create(
        model="gpt-4-1106-preview",
        messages=[
            {"role": "system", "content": "You correct typos in the last word and return only the last word's correction. Don't add punctuation. If you don't understand the last word, just return the original last word. Nothing else."},
            {"role": "user", "content": context + ' ' + word},
        ],
        #temperature=1,
    )
    # extracting the last word from the completion as a precaution
    corrected_word = completion.choices[0].message.content.strip().split()[-1]
    corrected_sequence += corrected_word.upper() + ' '
    
    # ----------------- Google TTS -----------------
    #tts = gtts.gTTS(corrected_word, slow=True)
    #tts.save("output.mp3")
    # ----------------------------------------------

    # ----------------- OpenAI TTS -----------------
    response = client.audio.speech.create(
    model="tts-1",
      voice="echo",
      response_format="mp3",
      speed=1,
      input= corrected_word
    )

    response.stream_to_file("output.mp3")
    # ----------------------------------------------

    playsound("output.mp3")

    context_buffer.append(corrected_word)  # add the corrected word to the context buffer

    if len(context_buffer) > 5:  # maintaining the last 5 words for context by removing the oldest word
        context_buffer.pop(0)  

    
    

# variables to keep track of the current symbol and how many consecutive frames it has appeared in
current_symbol = None
previous_symbol = None
current_symbol_count = 0
captured_sequence = ""
capture_threshold = 7  # Number of frames to detect the same symbol before capturing it
corrected_sequence = ""  # sequence of words after typo correction
context_buffer = [] # Buffer to hold the sliding window of words for context

# webcam feed
cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 200)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 200)

def main():
    global current_symbol, previous_symbol, current_symbol_count, captured_sequence, corrected_sequence, context_buffer
    while cap.isOpened():
        
        ret, frame = cap.read()
        if not ret:
            continue

        # flipping frame horizontally
        frame = cv2.flip(frame, 1)

        # converting frame to RGB for Mediapipe
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(rgb_frame)

        # If hand landmarks are found, predicting the ASL letter and displaying it
        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                predicted_label, predicted_prob = predict_asl_letter(hand_landmarks)

                # calculating bounding box for the hand
                x_coords = [landmark.x for landmark in hand_landmarks.landmark]
                y_coords = [landmark.y for landmark in hand_landmarks.landmark]
                x_min, x_max = int(min(x_coords) * image_width), int(max(x_coords) * image_width)
                y_min, y_max = int(min(y_coords) * image_height), int(max(y_coords) * image_height)

                # drawing hand landmarks
                mp.solutions.drawing_utils.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)

                # displaying the predicted ASL letter and probability on the frame next to the hand
                label_text = f"{predicted_label} ({predicted_prob:.2f}%)"
                cv2.putText(frame, label_text, (x_max, y_min), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2, cv2.LINE_AA)

                # checking if the current symbol is the same as the previous symbol
                if predicted_label == current_symbol:
                    current_symbol_count += 1
                else:
                    current_symbol = predicted_label
                    current_symbol_count = 1

                 # if the symbol has been detected consistently, add it to the captured sequence
                if current_symbol_count == capture_threshold and current_symbol != previous_symbol: # avoiding adding the same symbol twice (right now only 1 unique letter captured at a time)

                    if predicted_label == 'space':

                        # if space is detected, taking the last word from captured_sequence and uploading for correction
                        words = captured_sequence.strip().rsplit(' ', 1)

                        if len(words) > 0:
                            
                            # creating a thread to handle the typo correction, audio playback, context buffer update
                            tts_thread = threading.Thread(target=correct_typo_play_audio, args=(words[-1],))
                            tts_thread.start()

                        captured_sequence += ' '
                        previous_symbol = current_symbol

                    # text deletion (to fix)
                    elif predicted_label == 'del' and len(captured_sequence) > 0:

                        captured_sequence = captured_sequence[:-1]  # remove the last character for "del"
                        # handle deletion for corrected_sequence

                        if corrected_sequence and corrected_sequence[-1] == ' ':
                            # remove the last word from corrected_sequence
                            corrected_sequence = corrected_sequence.rsplit(' ', 2)[0] + ' '
                        else:
                            # remove the last character from corrected_sequence
                            corrected_sequence = corrected_sequence[:-1]
                    else:
                        captured_sequence += predicted_label  # Append the detected symbol
                        previous_symbol = current_symbol
                    # resetting the counter after adding or deleting the symbol
                    current_symbol_count = 0

        # displaying the captured sequence on the frame
        cv2.putText(frame, f"Captured: {captured_sequence}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (147, 20, 255), 2, cv2.LINE_AA)
        # displaying the corrected sequence on the frame
        cv2.putText(frame, f"Corrected: {corrected_sequence}", (10, 75), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2, cv2.LINE_AA)

        cv2.imshow("ASL Detection", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

main()