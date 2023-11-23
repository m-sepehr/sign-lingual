import cv2
import numpy as np
import mediapipe as mp
import tflite_runtime.interpreter as tflite
from tflite_runtime.interpreter import load_delegate

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


# loading the TFLite model
# --------------- change the file path to the model you want to use ----------------
interpreter = tflite.Interpreter(
    model_path="../models/asl_landmark_detection_model_mediapipe_edgetpu.tflite",
    experimental_delegates=[load_delegate('libedgetpu.so.1')]
)
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
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
    
    x_landmarks = [landmark.x * image_width for landmark in landmarks.landmark]
    y_landmarks = [landmark.y * image_height for landmark in landmarks.landmark]
    
    landmarks_array = np.array(x_landmarks + y_landmarks, dtype=np.float32).reshape(1, -1)
    
    interpreter.set_tensor(input_details[0]['index'], landmarks_array)
    interpreter.invoke()
    prediction = interpreter.get_tensor(output_details[0]['index'])
    
    # getting the predicted class name
    predicted_label = label_map[np.argmax(prediction)]
    
    # getting the prediction probability
    predicted_prob = np.max(prediction) * 100  # as percentage
    
    return predicted_label, predicted_prob



# webcam feed
cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 600)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 600)

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