import os
import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# Define the directories for training and testing data
base_dir = "/Users/sepehr/Downloads/archive"
train_dir = os.path.join(base_dir, "asl_alphabet_train/asl_alphabet_train")
test_dir = os.path.join(base_dir, "asl_alphabet_test/asl_alphabet_test")

# Define constants
image_height, image_width = 200, 200  # Adjust to your image size
num_classes = 2  # Assuming 29 letters in the ASL alphabet
epochs = 10

# Use data augmentation for training dataset
train_datagen = ImageDataGenerator(
    rescale=1.0 / 255.0,
    rotation_range=20,
    width_shift_range=0.2,
    height_shift_range=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode='nearest')

# Load and preprocess the training dataset
train_generator = train_datagen.flow_from_directory(
    train_dir,
    target_size=(image_height, image_width),
    batch_size=32,
    class_mode='categorical')

# Create the CNN model
model = models.Sequential([
    layers.Conv2D(32, (3, 3), activation='relu', input_shape=(image_height, image_width, 3)),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(64, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Flatten(),
    layers.Dense(64, activation='relu'),
    layers.Dense(num_classes, activation='softmax')
])

# Compile the model
model.compile(optimizer='adam',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

# Train the model
history = model.fit(train_generator, epochs=epochs)

# Save the trained model for future use
model.save("asl_sign_detection_model.h5")
