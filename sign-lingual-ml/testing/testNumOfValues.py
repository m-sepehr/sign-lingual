import numpy as np

def display_landmarks_and_values(npz_file_path):
    # Load the .npz file
    data = np.load(npz_file_path)

    # Display the number of landmarks and their values for each array in the .npz file
    for key in data.files:
        print(f"Number of landmarks for image {key}: {data[key].shape[0]}")
        print("Landmark values:")
        print(data[key])
        print("\n" + "-"*50 + "\n")  # Separator for clarity

    # Close the .npz file after reading
    data.close()

# Placeholder for the .npz file path
npz_file_path_placeholder = "../extracted-landmarks/X_y_train_data_augmented_V3.npz"
display_landmarks_and_values(npz_file_path_placeholder)


