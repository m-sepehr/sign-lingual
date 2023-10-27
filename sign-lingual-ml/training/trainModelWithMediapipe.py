import time
import numpy as np
import mediapipe as mp
import tensorflow as tf
from tensorflow.keras import layers, models
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

    # Loading landmarks from file and training the model  
# loading data from file
# -------change the file name here-------
with np.load("../extracted-landmarks/X_y_train_data_augmented_V4.npz") as data:
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
        layers.Dense(128, activation='relu', input_shape=(X_train.shape[1],)),
        layers.Dropout(0.5),
        layers.Dense(64, activation='relu'),
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
    model.save("../models/asl_landmark_detection_model_mediapipe_augmented_V4.h5")
    # ---------------------------------------

    end_time = time.time()
    elapsed_time = end_time - start_time

    print(f"Total time taken: {elapsed_time:.2f} seconds")
