# ------------------------------------------------------------------------------
# This file is used to manually build the mediapipe .aar file needed for android.
#
# After cloning the mediapipe repo, place this file in:
# /mediapipe/java/com/google/mediapipe/solutions/hands
#
# Install the Bazel build tool (tried on v6.1.1)
# https://docs.bazel.build/versions/master/install.html
#
# Build the .aar file with the following command:
#        bazel build -c opt --strip=ALWAYS \
#        --host_crosstool_top=@bazel_tools//tools/cpp:toolchain \
#        --fat_apk_cpu=arm64-v8a,armeabi-v7a \
#        --legacy_whole_archive=0 \
#        --features=-legacy_whole_archive \
#        --copt=-fvisibility=hidden \
#        --copt=-ffunction-sections \
#        --copt=-fdata-sections \
#        --copt=-fstack-protector \
#        --copt=-Oz \
#        --copt=-fomit-frame-pointer \
#        --copt=-DABSL_MIN_LOG_LEVEL=2 \
#        --linkopt=-Wl,--gc-sections,--strip-all \
#                --javacopt="-Acom.google.auto.value.AutoBuilderIsUnstable" \
#        //mediapipe/java/com/google/mediapipe/solutions/hands:hand_tracking.aar
# ------------------------------------------------------------------------------

load("//mediapipe/java/com/google/mediapipe:mediapipe_aar.bzl", "mediapipe_aar")

mediapipe_aar(
    name = "hand_tracking",
    srcs = glob(["**/*.java"]),
    gen_libmediapipe = True,
)

android_library(
    name = "hands",
    srcs = [
        "HandLandmark.java",
        "Hands.java",
        "HandsOptions.java",
        "HandsResult.java",
    ],
    assets = [
        "//mediapipe/modules/hand_landmark:hand_landmark_full.tflite",
        "//mediapipe/modules/hand_landmark:hand_landmark_lite.tflite",
        "//mediapipe/modules/hand_landmark:hand_landmark_tracking_cpu_image.binarypb",
        "//mediapipe/modules/hand_landmark:hand_landmark_tracking_gpu_image.binarypb",
        "//mediapipe/modules/hand_landmark:handedness.txt",
        "//mediapipe/modules/palm_detection:palm_detection_full.tflite",
        "//mediapipe/modules/palm_detection:palm_detection_lite.tflite",
    ],
    assets_dir = "",
    javacopts = ["-Acom.google.auto.value.AutoBuilderIsUnstable"],
    manifest = ":AndroidManifest.xml",
    visibility = ["//visibility:public"],
    deps = [
        "//mediapipe/java/com/google/mediapipe/solutioncore:solution_base",
        "//mediapipe/framework/formats:classification_java_proto_lite",
        "//mediapipe/framework/formats:landmark_java_proto_lite",
        "//mediapipe/java/com/google/mediapipe/framework:android_framework",
	"//third_party:autovalue",
        "@maven//:androidx_annotation_annotation",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_google_guava_guava",
    ],
)


# Expose the java source files for building mediapipe AAR.
filegroup(
    name = "java_src",
    srcs = glob(["*.java"]),
    visibility = ["//mediapipe:__subpackages__"],
)