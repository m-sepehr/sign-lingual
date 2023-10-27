import cv2
import mediapipe as mp

mp_hands = mp.solutions.hands
hands = mp_hands.Hands(max_num_hands=2, min_detection_confidence=0.7, min_tracking_confidence=0.7)

mp_drawing = mp.solutions.drawing_utils

cap = cv2.VideoCapture(0) 

while cap.isOpened():
    ret, image = cap.read()
    if not ret:
        continue

    image = cv2.flip(image, 1)
    
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    
    results = hands.process(rgb_image)
    
    if results.multi_hand_landmarks:
        for landmarks in results.multi_hand_landmarks:
            for idx, landmark in enumerate(landmarks.landmark):
                h, w, _ = image.shape
                cx, cy = int(landmark.x * w), int(landmark.y * h)
                cv2.circle(image, (cx, cy), 5, (255, 0, 0), -1)
                cv2.putText(image, f"{idx}: ({landmark.x:.4f}, {landmark.y:.4f})", (cx, cy), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
            
            mp_drawing.draw_landmarks(image, landmarks, mp_hands.HAND_CONNECTIONS)\
            
    cv2.imshow("Hand Landmarks", image)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
