#----------------------------------------------------------------------------------
# This script converts the Keras model to TensorFlow Lite format for use on mobile
# and embedded devices. The TensorFlow Lite model is saved in the models folder.
#----------------------------------------------------------------------------------

import tensorflow as tf
from tensorflow.keras.models import load_model

# loading the model
model = load_model("../models/asl_landmark_detection_model_mediapipe.h5")

# converting the model to TensorFlow Lite format
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

# saving the model
with open("../models/asl_landmark_detection_model_mediapipe.tflite", "wb") as f:
    f.write(tflite_model)

# ----------------------------------------------------------------------------------
# Still need to compile the model if using a Coral Edge TPU
# After installing EDGE TPU runtime, run the following command:
# edgetpu_compiler path/to/tflite/model
#
# Note:  The Edge TPU Compiler is no longer available for ARM64 systems 
#        (such as the Coral Dev Board or Raspberry Pi 4). 
#        
#        There is a web-based compiler available at:
#        https://colab.research.google.com/github/google-coral/tutorials/blob/master/compile_for_edgetpu.ipynb
# ----------------------------------------------------------------------------------
