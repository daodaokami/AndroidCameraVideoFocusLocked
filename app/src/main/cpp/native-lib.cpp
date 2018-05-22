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
JNIEXPORT jdoubleArray JNICALL
Java_com_daodaokami_lut_camera_1video_1c_1lut_tools_PicturePrcess_getCameraPose(JNIEnv *env,
                                                                                jobject instance,
                                                                                jintArray img1_,
                                                                                jintArray img2_,
                                                                                jint height,
                                                                                jint width) {
    jint *img1 = env->GetIntArrayElements(img1_, NULL);
    jint *img2 = env->GetIntArrayElements(img2_, NULL);

    jdouble drt[12];//9+3
    // TODO
    cv::Mat mImg1(height, width, CV_8UC4, img1);
    cv::Mat mImg2(height, width, CV_8UC4, img2);
    //double 是一个12维度的向量6个自由度，代表了旋转和位移
    jdoubleArray result = env->NewDoubleArray(12);
    double rt[12];
    for(int i=0;i<9;i++){
        if(i == 0|| i==4 || i==8){
            rt[i] = i*1.0;
        }else{
            rt[i] = 0;
        }
    }
    rt[9] = 1;
    rt[10] = 0;
    rt[11] = 0;

    cv::Mat K;
    K = (Mat_<double>(3,3)<<937.68, 0, 387.41,
    0, 931.39, 478.88,
    0, 0, 1);

    cv::Mat R, t;
    pose_estimation_2d2d(mImg1, mImg2, K, R, t);

    for(int i=0;i<3;i++){
        for(int j=0;j<3;j++){
            rt[i*3+j] = R.at<double>(i,j);
        }
    }
    rt[9] = t.at<double>(0,0);
    rt[10] = t.at<double>(1,0);
    rt[11] = t.at<double>(2,0);
    env->SetDoubleArrayRegion(result, 0, 12, rt);
    env->ReleaseIntArrayElements(img1_, img1, 0);
    env->ReleaseIntArrayElements(img2_, img2, 0);
    return result;
}