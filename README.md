# Sign-Lingual

## Overview
The **Sign-Lingual Project** is a real-time sign language recognition system that translates hand gestures into text or speech using machine learning and OpenAI technologies. It leverages advanced tools like OpenAI’s Language Model (LLM) for typo correction and contextual understanding, as well as OpenAI’s Whisper for text-to-speech conversion. The system consists of three key components:

1. **Machine Learning Model**: Handles gesture recognition through a trained neural network model.
2. **PC Host Application**: Acts as the server and inference engine, performing gesture recognition and communicating with Firebase and the mobile app.
3. **Android App**: Provides the user interface for visualizing and interacting with the recognized signs in real-time.

---

## 1. Machine Learning (ML) Component

The **sign-lingual-ml** directory contains the machine learning workflows for preprocessing, training, and model deployment. This includes the key steps needed to recognize hand gestures based on landmarks extracted from images or video frames.

### Key Components:

#### 1.1 Data Preprocessing and Landmark Extraction

- **Landmark Extraction**: The `extractLandmarks.py` script is responsible for extracting hand landmarks from images using **MediaPipe**. This script initializes **MediaPipe’s hand solution** to process each image, extracting the hand landmarks as the key points needed for gesture recognition. The extracted landmarks are fed into the training pipeline for further processing.

  Key functionalities include:
  - **MediaPipe Integration**: The script initializes **MediaPipe** in static mode to extract hand landmarks from individual images.
  - **Augmentation**: To ensure robustness, the script uses `ImageDataGenerator` to apply augmentations such as:
    - Random width and height shifts.
    - Horizontal flips.
    - Filling gaps with nearest-neighbor interpolation.

  The landmarks extracted from each image are paired with their corresponding labels (e.g., gestures "A", "B", "C", etc.), which are critical for training the machine learning model.

#### 1.2 Model Training

The model is trained using TensorFlow/Keras, and the `trainModelWithMediapipe.py` script orchestrates the training process:

- **Model Architecture**: The custom deep neural network (DNN) used for gesture recognition includes:
  - **Dense (Fully Connected) Layers**: For processing hand landmarks.
  - **L2 Regularization**: To prevent overfitting and ensure the model generalizes well.
  - **Adam Optimizer**: Used to optimize the model during training.
  
- **Training Process**:
  - Hand gestures are mapped to labels (e.g., "A", "B", "C", etc.). These labels are converted into numerical values for classification using a label mapping dictionary.
  - **Early Stopping** is employed to stop training when the model performance stops improving.
  
  The final trained model is saved in the **models/** directory in HDF5 format (`asl_landmark_detection_model_mediapipe.h5`), making it ready for deployment.

#### 1.3 Model Conversion

After training, the model is converted into a mobile-compatible format using TensorFlow Lite:

- **TensorFlow Lite Conversion**: The `convertToTensorFlowLite.py` script converts the trained model into **TensorFlow Lite** format (`asl_landmark_detection_model_mediapipe.tflite`) for efficient inference on mobile devices. Optimizations are applied during the conversion process to minimize the model size and computational load.

#### 1.4 Real-Time Inference and OpenAI Whisper Integration

The **useModel.py** script handles real-time gesture recognition, including integration with OpenAI Whisper for text-to-speech:

- **Mediapipe Integration**: Captures real-time hand gestures, extracts hand landmarks, and feeds them into the TensorFlow Lite model for gesture recognition.
  
- **Firebase Integration**: Recognized gestures are sent to Firebase, allowing real-time updates to be retrieved by the Android app.
  
- **OpenAI Whisper Integration**: Converts recognized gestures into audible speech using **gTTS** (Google Text-to-Speech) and OpenAI’s Whisper for enhanced TTS functionality. Whisper adds the ability to generate natural-sounding speech for the recognized signs, improving user accessibility.

### Overall Workflow:
1. **Landmark Extraction**: Extract hand landmarks using MediaPipe.
2. **Model Training**: Train the neural network model using TensorFlow/Keras.
3. **Model Conversion**: Convert the trained model to TensorFlow Lite format for mobile deployment.
4. **Inference and TTS**: Capture real-time gestures, perform inference, and send the results to Firebase. Optionally, convert recognized text to speech using OpenAI Whisper.

---

## 2. PC Host Application Architecture

The **PC Host Application** orchestrates the entire recognition process and facilitates real-time communication between the system components.

### Key Components:

#### 2.1 Server Setup and Communication

- **Flask-based API Server**: The `server.py` script sets up a Flask web server that handles communication between the mobile app and the machine learning backend.

- **Zeroconf Networking**: This allows devices on the local network to discover the server and establish connections without manual configuration.

- **QR Code Scanning**: The server uses QR codes to authenticate devices, which carry necessary configuration details like Firebase URLs and user tokens.

- **Firebase Communication**: The host continuously sends recognized gestures to Firebase, enabling real-time updates for the Android app.

#### 2.2 Real-Time Model Inference

- **MediaPipe Integration**: The PC host uses MediaPipe to capture hand gestures from a connected camera, extract the landmarks, and perform gesture recognition in real-time using the TensorFlow Lite model.
  
- **OpenAI Whisper Integration**: Converts recognized gestures into speech using Whisper, providing auditory feedback for users.

#### 2.3 OpenAI LLM Integration for Typo Correction and Contextual Understanding

The PC host application integrates **OpenAI's Language Models (LLM)** to enhance the recognized text by correcting typos and generating contextually relevant responses. This ensures that the recognized gestures are refined for better accuracy and user understanding.

- **Typo Correction**: Once a gesture is recognized and converted into text, the system uses OpenAI LLM to analyze the text for possible corrections. This step helps ensure that any minor errors during recognition are automatically corrected before being sent to the user.

- **Contextual Understanding**: In addition to typo correction, the OpenAI LLM can provide extended contextual responses based on the recognized gestures. This is particularly useful for multi-sign sequences that form sentences, as the model can generate more human-like and grammatically accurate responses.

### Example Workflow for OpenAI Integration:
1. **Gesture Recognition**: The recognized text is sent to OpenAI’s LLM for further processing.
2. **Typo Correction**: OpenAI analyzes the text, corrects any errors, and ensures it is formatted correctly.
3. **Contextual Response**: OpenAI generates contextual responses or suggestions, such as completing a sentence or improving its structure.
4. **Send to Firebase**: The corrected text is sent to Firebase and displayed on the Android app.

---

## 3. Android App Architecture

The **Android App** provides the interface for users to visualize and interact with the system. It retrieves real-time gesture recognition results from Firebase and enables live sign language translation.

### Key Components:

#### 3.1 MainActivity.java

- **Firebase Integration**: Connects to the Firebase Realtime Database to retrieve recognized gestures sent from the PC host.

- **Navigation**: Provides options to access settings, user guides, and initiate live translation sessions with the PC host.

#### 3.2 LiveTranslation.java

This activity handles the real-time display of recognized gestures, using Firebase to continuously retrieve and update results.

- **Chaquopy Integration**: If additional gesture processing is required, the app can use **Chaquopy** to run Python scripts on Android.

- **UI Updates**: The recognized gestures are displayed in real-time using a **TextView**, with the option to start or stop live translation sessions.

---

## Installation and Setup

### PC Host Application:
1. Install the dependencies:
   ```bash
   pip install -r requirements.txt
   ```
2. Run the Flask server:
   ```bash
   python server.py
   ```
3. Configure Firebase with the appropriate database URL and credentials.

### Android Application:
1. Open the project in Android Studio.
2. Sync the Gradle project and install dependencies.
3. Build and install the app on your Android device.

### Machine Learning:
1. To train the model, navigate to the **sign-lingual-ml** directory and run:
   ```bash
   python training/trainModelWithMediapipe.py
   ```
2. Convert the trained model to TensorFlow Lite:
   ```bash
   python model-conversion/convertToTensorFlowLite.py
   ```

---

## Contribution

Contributions are welcome! Feel free to open issues or submit pull requests to enhance the system's functionality or optimize the machine learning models.

