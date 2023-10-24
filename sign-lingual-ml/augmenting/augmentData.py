import os
import cv2
import numpy as np
from tqdm import tqdm

def random_shear(img, x_shear, y_shear):
    """Function to shear the image."""
    rows, cols, _ = img.shape
    M = np.float32([[1, x_shear, 0], [y_shear, 1, 0]])
    return cv2.warpAffine(img, M, (cols, rows))

def process_images_folder(main_folder):
    for subdir, _, files in os.walk(main_folder):
        for file in files:
            # Ensure the file is an image
            if file.lower().endswith(('.png', '.jpg', '.jpeg')):
                img_path = os.path.join(subdir, file)
                
                # Read the image
                img = cv2.imread(img_path)
                
                # Flip and save as a new image
                flipped_img = cv2.flip(img, 1)  # Horizontal flip
                flipped_img_path = os.path.join(subdir, "flipped_" + file)
                cv2.imwrite(flipped_img_path, flipped_img)
                
                # Process both original and flipped images
                for image, path in [(img, img_path), (flipped_img, flipped_img_path)]:
                    # Randomly rotate the image and overwrite
                    angle = np.random.uniform(-10, 10)
                    M = cv2.getRotationMatrix2D((image.shape[1] / 2, image.shape[0] / 2), angle, 1)
                    rotated_img = cv2.warpAffine(image, M, (image.shape[1], image.shape[0]))
                    cv2.imwrite(path, rotated_img)
                    
                    # Randomly tilt (shear) the image vertically and horizontally and overwrite
                    x_shear = np.random.uniform(-0.1, 0.1)
                    y_shear = np.random.uniform(-0.1, 0.1)
                    sheared_img = random_shear(rotated_img, x_shear, y_shear)
                    cv2.imwrite(path, sheared_img)

# ----------- place the dataset folder in the parent sign-lingual-ml folder -----------
main_folder_path = "../archive/asl_alphabet_train/asl_alphabet_train"
# -------------------------------------------------------------------------------------
process_images_folder(main_folder_path)
