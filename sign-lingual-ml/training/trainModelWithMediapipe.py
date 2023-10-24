import time
import os
import cv2
import numpy as np
import mediapipe as mp
import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.regularizers import l2
from tensorflow.keras.callbacks import EarlyStopping
from sklearn.preprocessing import StandardScaler
from tensorflow.keras import backend as K
from sklearn.model_selection import train_test_split

start_time = time.time()

# Initializing MediaPipe hand solution
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=True)


label_map = {
    0: 'A', 1: 'B', 2: 'C', 3: 'D', 4: 'del', 5: 'E',
    6: 'F', 7: 'G', 8: 'H', 9: 'I', 10: 'J', 11: 'K', 12: 'L', 13: 'M', 14: 'N', 15: 'nothing', 16: 'O',
    17: 'P', 18: 'Q', 19: 'R', 20: 'S', 21: 'space', 22: 'T',
    23: 'U', 24: 'V', 25: 'W', 26: 'X', 27: 'Y', 28: 'Z'
}

label_map = {v: k for k, v in label_map.items()}

# augmentations
datagen = ImageDataGenerator( 
    width_shift_range=0.1,  # random width shifts
    height_shift_range=0.1,  # random height shifts
    horizontal_flip=True,  # random horizontal flip
    fill_mode='nearest'
)

def extract_landmarks(img):
    rgb_image = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    results = hands.process(rgb_image)
    
    if results.multi_hand_landmarks:
        landmarks = results.multi_hand_landmarks[0]
        return [landmark.x * image_width for landmark in landmarks.landmark], \
               [landmark.y * image_height for landmark in landmarks.landmark]
    else:
        return [], []
    
# return landmarks for original and augmented images
def augment_and_extract_landmarks(image_path, augmentations=0):
    
    image = cv2.imread(image_path)
    landmarks_list = []

    # Original image landmarks
    x_landmarks, y_landmarks = extract_landmarks(image)
    if x_landmarks and y_landmarks:
        landmarks_list.append((x_landmarks + y_landmarks))

    # Augmented image landmarks
    for _ in range(augmentations):
        augmented_image = datagen.random_transform(image)
        x_landmarks, y_landmarks = extract_landmarks(augmented_image)
        if x_landmarks and y_landmarks:
            landmarks_list.append((x_landmarks + y_landmarks))

    return landmarks_list

def get_label_value(label):
    try:
        return label_map[label]
    except KeyError:
        print(f"Unexpected label: {label}")
        return -1

def f1_metric(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    recall = true_positives / (possible_positives + K.epsilon())
    f1_val = 2*(precision*recall)/(precision+recall+K.epsilon())
    return f1_val

# directories for training and testing data
# ----------- place the dataset folder in the parent sign-lingual-ml folder -----------
train_dir = "../archive/asl_alphabet_train/asl_alphabet_train"
# -------------------------------------------------------------------------------------

# constants
image_height, image_width = 200, 200
num_classes = 29  # outputs
batch_size = 4096
epochs = 10000

X_train = []
y_train = []

time.sleep(1) # allowing logs to print before user input

user_choice = input("[1] Extract landmarks from images\n[2] Load landmarks from .npz file and train model\nEnter your choice: ")

match user_choice:

    # Extracting landmarks from images
    case "1":  
        for class_folder in os.listdir(train_dir):
            if class_folder == ".DS_Store":
                continue  # Skip .DS_Store file
            print(f"Processing images in {class_folder}...")
            class_folder_path = os.path.join(train_dir, class_folder)
            for image_name in os.listdir(class_folder_path):
                if image_name == ".DS_Store":
                    continue  # Skip .DS_Store file
                image_path = os.path.join(class_folder_path, image_name)
                landmarks_list = augment_and_extract_landmarks(image_path)
                for landmarks in landmarks_list:
                    print(f"Hand detected in {image_name}.")
                    X_train.append(landmarks)
                    y_train.append(class_folder)

        # Convert to NumPy arrays
        X_train = np.array(X_train)
        y_train = np.array(y_train)
        
        # Saving X_train and y_train to a file
        # -------change the file name here-------
        np.savez("../extracted-landmarks/X_y_train_data_augmented_V3.npz", X_train=X_train, y_train=y_train)
        # ---------------------------------------
        end_time = time.time()
        elapsed_time = end_time - start_time
        print(f"Total time taken: {elapsed_time:.2f} seconds")
        exit()

    # Loading landmarks from file and training the model
    case "2":   
        # loading data from file
        # -------change the file name here-------
        with np.load("../extracted-landmarks/X_y_train_data_augmented_V3.npz") as data:
        # ---------------------------------------
            X_train = data['X_train']
            y_train = data['y_train']

        scaler = StandardScaler().fit(X_train)
        X_train = scaler.transform(X_train)

        # converting string labels to integer labels using the mapping
        y_train = np.array([get_label_value(label) for label in y_train])


        # converting labels to one-hot encoded vectors
        y_train = tf.keras.utils.to_categorical(y_train, num_classes=num_classes)

        #  DNN model
        model = models.Sequential([
            layers.Dense(36, activation='relu', input_shape=(X_train.shape[1],)),
            layers.Dense(num_classes, activation='softmax')
        ])

        # Desired learning rate
        learning_rate = 0.001

        # Create an Adam optimizer instance with the custom learning rate
        optimizer = Adam(learning_rate=learning_rate)

        # Compiling the model with the custom optimizer
        model.compile(optimizer=optimizer,
              loss='categorical_crossentropy',
              metrics=['accuracy', f1_metric])

        # early stopping callback
        early_stop = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)

        # Shuffle and split data into training and validation sets
        X_train, X_val, y_train, y_val = train_test_split(X_train, y_train, test_size=0.2, random_state=42)

        # training the model
        history = model.fit(X_train, y_train, epochs=epochs, batch_size=batch_size, validation_data=(X_val, y_val), callbacks=[early_stop])

        # saving the trained model for future use
        # -------change the file name here-------
        model.save("../models/asl_landmark_detection_model_mediapipe_augmented_V3.h5")
        # ---------------------------------------

        end_time = time.time()
        elapsed_time = end_time - start_time

        print(f"Total time taken: {elapsed_time:.2f} seconds")
