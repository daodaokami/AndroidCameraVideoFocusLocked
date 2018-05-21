//
// Created by lut on 2018/5/18.
//

#ifndef CAMERA_VIDEO_C_LUT_COMMON_HEAD_H
#define CAMERA_VIDEO_C_LUT_COMMON_HEAD_H
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/calib3d/calib3d.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/opencv.hpp>
#include <iostream>
template <typename T>
std::string to_string(T value)
{
    std::ostringstream os ;
    os << value ;
    return os.str() ;
}
#endif //CAMERA_VIDEO_C_LUT_COMMON_HEAD_H
