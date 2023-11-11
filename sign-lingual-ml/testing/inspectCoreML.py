# --------------------------------------------------
# This script is used to inspect the CoreML model
# and print the input and output descriptions.
# --------------------------------------------------

import coremltools as ct

# loading CoreML model
coreml_model = ct.models.MLModel('../models/coreml.mlpackage')

# printing input and output descriptions
print(coreml_model.get_spec())