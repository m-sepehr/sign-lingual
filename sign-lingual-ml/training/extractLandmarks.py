import time
import os
import cv2
import numpy as np
import mediapipe as mp
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from multiprocessing import Pool, cpu_count, Manager
from tqdm import tqdm

# augmentations
datagen = ImageDataGenerator( 
    width_shift_range=0.1,  # random width shifts
    height_shift_range=0.1,  # random height shifts
    horizontal_flip=True,  # random horizontal flip
    fill_mode='nearest'
)

# constants
image_height, image_width = 200, 200
num_classes = 29  # outputs

# initializing MediaPipe hand solution
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=True)

def process_image(data, progress_list=None):
    image_path, class_folder = data
    landmarks_list = augment_and_extract_landmarks(image_path)
    landmarks_with_label = [(landmarks, class_folder) for landmarks in landmarks_list if landmarks]
    if progress_list is not None:
        progress_list.append(1)
    return landmarks_with_label

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

    # original image landmarks
    x_landmarks, y_landmarks = extract_landmarks(image)
    if x_landmarks and y_landmarks:
        landmarks_list.append((x_landmarks + y_landmarks))
       # print(f"Hand detected in {os.path.basename(image_path)}.")

    # augmented image landmarks
    for _ in range(augmentations):
        augmented_image = datagen.random_transform(image)
        x_landmarks, y_landmarks = extract_landmarks(augmented_image)
        if x_landmarks and y_landmarks:
            landmarks_list.append((x_landmarks + y_landmarks))

    return landmarks_list


def main():
    start_time = time.time()

    label_map = {
        0: 'A', 1: 'B', 2: 'C', 3: 'D', 4: 'del', 5: 'E',
        6: 'F', 7: 'G', 8: 'H', 9: 'I', 10: 'J', 11: 'K', 12: 'L', 13: 'M', 14: 'N', 15: 'nothing', 16: 'O',
        17: 'P', 18: 'Q', 19: 'R', 20: 'S', 21: 'space', 22: 'T',
        23: 'U', 24: 'V', 25: 'W', 26: 'X', 27: 'Y', 28: 'Z'
    }

    label_map = {v: k for k, v in label_map.items()}

    # directories for training and testing data
    # ----------- place the dataset folder in the parent sign-lingual-ml folder -----------
    train_dir = "../archive/original_rotated/asl_alphabet_train/asl_alphabet_train"
    # -------------------------------------------------------------------------------------

    X_train = []
    y_train = []

    # setting the number of processes to the number of available CPU cores
    # can be changed to a number to reduce the number of processes and not use all CPU cores
    num_processes = cpu_count()


    with Pool(num_processes) as pool:
        # preparing a list of tuples containing (image_path, class_folder)
        image_data = []
        for class_folder in os.listdir(train_dir):
            if class_folder == ".DS_Store":
                continue  # Skip .DS_Store file
            print(f"Processing images in {class_folder}...")
            class_folder_path = os.path.join(train_dir, class_folder)
            for image_name in os.listdir(class_folder_path):
                if image_name == ".DS_Store":
                    continue  # Skip .DS_Store file
                image_path = os.path.join(class_folder_path, image_name)
                image_data.append((image_path, class_folder))

         # initializing progress list and tqdm progress bar
        manager = Manager()
        progress_list = manager.list()
        pbar = tqdm(total=len(image_data), position=0, leave=True)

        # the update function for tqdm
        def update(*args):
            pbar.update(len(progress_list) - pbar.n)

        # processing images with progress bar
        results = [pool.apply_async(process_image, args=(data, progress_list), callback=update) for data in image_data]

        # waiting for all processes to finish to retrieve results
        results = [res.get() for res in results]

        pbar.close()

        # flattening the list of results and splitting them into X_train and y_train
        for landmarks_list in results:
            for landmarks, label in landmarks_list:
                X_train.append(landmarks)
                y_train.append(label)

    # converting to NumPy arrays
    X_train = np.array(X_train)
    y_train = np.array(y_train)

    # saving X_train and y_train to a file
    # -------change the file name here-------
    np.savez("../extracted-landmarks/X_y_train_data_augmented_V4.npz", X_train=X_train, y_train=y_train)
    # ---------------------------------------
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Total time taken: {elapsed_time:.2f} seconds")


if __name__ == "__main__":
    main()