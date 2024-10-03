# Android Sign Language Translator App

This Android app is designed to translate sign language into text using camera-based input, integrating machine learning models for hand landmark detection and translation. It features Wi-Fi QR code generation for device setup and uses TensorFlow Lite for on-device model inference.

## Features

- **Live Translation**: The app captures sign language gestures through the camera and translates them into text using a machine learning model.
- **Wi-Fi QR Code Setup**: Generates and scans QR codes containing Wi-Fi credentials to configure devices for network access.
- **Offline Support**: The app uses TensorFlow Lite models for translation, making it functional without an internet connection.
- **Settings Customization**: Users can customize app settings through the `SettingsActivity`.

## Project Structure

### Java Files
- `MainActivity.java`: Handles the main user interface of the app.
- `LiveTranslation.java`: Manages the live camera feed and translates sign language gestures.
- `AutoFitTextureView.java`, `CameraConnectionFragment.java`: Handle camera preview and connection for capturing hand gestures.
- `BaseActivity.java`, `SignInActivity.java`, `StandaloneActivity.java`: Other UI components for user authentication and standalone functionalities.
- `LocaleConfig.java`: Manages localization settings for the app.
- `SettingsActivity.java`, `SettingsFragments.java`: Manage user settings and preferences.
- `networkActivity.java`: Generates Wi-Fi QR codes for network configuration.
- `NetworkDialog.java`: Displays QR codes for sharing Wi-Fi information.

### Python Scripts (for backend processing and machine learning)
- `network.py`: Defines the neural network architecture used for hand gesture recognition.
- `augmentData.py`: Performs data augmentation to improve the training dataset.
- `trainModelWithMediapipe.py`: Trains a machine learning model with hand landmark data extracted via Mediapipe.
- `convertToTensorFlowLite.py`: Converts the trained model into a TensorFlow Lite model for use on Android devices.
- `useModel.py`: Executes the trained model on live sign language input from the camera.

### Model Files
- `asl_landmark_detection_model_mediapipe.h5`: The Keras model for sign language detection.
- `asl_landmark_detection_model_mediapipe.tflite`: The TensorFlow Lite model for real-time inference on Android.

### Server Components
- `server.py`, `testServer.py`: Backend server components for Firebase integration and remote functionality.

### Other Files
- `AndroidManifest.xml`: Defines permissions, activities, and services for the app.
  
## Technologies Used

- **Android**: Primary platform for app development.
- **Python**: Used for machine learning model training and backend processing.
- **Mediapipe**: For extracting hand landmarks.
- **TensorFlow & TensorFlow Lite**: For training and deploying machine learning models on Android.
- **Chaquopy**: Embeds Python into the Android app.
- **QR Code (ZXing)**: For generating and reading Wi-Fi setup QR codes.

## Setup Instructions

### Prerequisites

- Android Studio
- Python 3.x with TensorFlow and Mediapipe installed
- Chaquopy plugin for integrating Python with Android

### Steps

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   ```
2. **Open in Android Studio**: Open the project in Android Studio and sync the dependencies.
3. **Install Python Dependencies**:
   ```bash
   pip install -r requirements.txt
   ```
4. **Prepare the Model**: Convert the trained model to TensorFlow Lite using `convertToTensorFlowLite.py`.
5. **Build and Run**: Compile and run the app in an Android emulator or physical device.

### Optional Steps

- Use `trainModelWithMediapipe.py` to retrain the model with new data.
- Generate a QR code for Wi-Fi setup in `networkActivity.java`.

## Usage

1. **Sign In**: Start by signing in via `SignInActivity`.
2. **Wi-Fi QR Code**: Configure device network settings by generating or scanning a QR code for Wi-Fi credentials in the `networkActivity`.
3. **Live Translation**: Start live translation by capturing sign language gestures through the camera.

## Contributing

Contributions are welcome! Please submit a pull request if you’d like to improve the project or add new features.
