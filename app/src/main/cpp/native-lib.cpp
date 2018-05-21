#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include "include/c_traingulation.h"
#include "include/c_features_matcher.h"
using namespace std;
using namespace cv;

class ImageUtils{
public:
    void imageToGray(Mat inputImg, string outFilePath);
};

void ImageUtils::imageToGray(Mat inputImg, string outFilePath) {
    Mat gray;
    Mat input = inputImg.clone();
    cvtColor(input, gray, COLOR_BGR2GRAY);
    imwrite(outFilePath,  gray);
}

extern "C" JNIEXPORT jstring
JNICALL
Java_com_daodaokami_lut_camera_1video_1c_1lut_CameraActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_daodaokami_lut_camera_1video_1c_1lut_cex_CExFuncLoader_getCameraPose(JNIEnv *env,
                                                                              jobject instance,
                                                                              jstring imgPath_,
                                                                              jint imgCount) {
    const char *imgPath = env->GetStringUTFChars(imgPath_, 0);
    const int img_count = imgCount;
    // TODO
    //now we have imgpath, we read img in the c++ file
    env->ReleaseStringUTFChars(imgPath_, imgPath);
    string s_img_path = imgPath;
    //we set the img path and img count to the c++ function
    Mat img;
    getImg(s_img_path, img);

}