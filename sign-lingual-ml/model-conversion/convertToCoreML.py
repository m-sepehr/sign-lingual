# ------------------------------------------------------
# This script converts the Keras model to CoreML format
# for use on Apple devices. CoreML can leverage the
# Apple Silicon's Neural Engine for faster inference.
# ------------------------------------------------------

import coremltools as ct
import tensorflow as tf

# loading tensorflow model
model = tf.keras.models.load_model('../models/asl_landmark_detection_model_mediapipe.h5')

# converting to CoreML mlprogram
coreml_model = ct.convert(model, convert_to="mlprogram")

# saving the model
coreml_model.save('../models/coreml.mlpackage')