import cv2
import numpy as np
import mediapipe as mp
import tensorflow as tf
from tensorflow.keras import backend as K

# constants
image_width, image_height = 200, 200  

def f1_metric(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    recall = true_positives / (possible_positives + K.epsilon())
    f1_val = 2*(precision*recall)/(precision+recall+K.epsilon())
    return f1_val
# loading the model

model = tf.keras.models.load_model("../models/asl_landmark_detection_model_mediapipe.h5", custom_objects={"f1_metric": f1_metric})

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



# webcam feed
cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 200)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 200)

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

    cv2.imshow("ASL Detection", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
