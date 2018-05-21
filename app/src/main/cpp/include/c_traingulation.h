//
// Created by lut on 2018/5/18.
//

#ifndef CAMERA_VIDEO_C_LUT_TRAINGULATION_H
#define CAMERA_VIDEO_C_LUT_TRAINGULATION_H

#include "c_common_head.h"
#include <vector>
#include "c_features_matcher.h"
using namespace cv;
using namespace std;

Point2f pixel2cam(const Point2d& p, const Mat& K){
    return Point2f(
            (p.x - K.at<double>(0, 2)) / K.at<double>(0, 0),
            (p.y - K.at<double>(1, 2)) / K.at<double>(1, 1)
    );
}
void pose_estimation_2d2d(
        const vector<KeyPoint>& keypoints_1,
        const vector<KeyPoint>& keypoints_2,
        const vector<DMatch>& matches,
        const Mat& K,
        Mat& R, Mat& t);
void pose_estimation_2d2d(const Mat& img_1,
                          const Mat& img_2,
                          const Mat& K,Mat& R, Mat& t){
    vector<KeyPoint> keypoints_1;
    vector<KeyPoint> keypoints_2;
    Mat descriptors_1;
    Mat descriptors_2;
    vector<DMatch> good_matches;

    get_matchfeatures_orb(img_1, img_2, keypoints_1, keypoints_2,
                          descriptors_1, descriptors_2, good_matches);

    pose_estimation_2d2d(keypoints_1, keypoints_2, good_matches,
                         K, R, t);

    string s_R="", s_t="";
    for(int i=0;i<3;i++){
        for(int j=0;j<3;j++){
            s_R += R.at<double>(i,j);
            s_R += " ";
        }
    }
    for(int i=0;i<3;i++){
        s_t += R.at<double>(i,0);
        s_t += " ";
    }
    cout<<"R"<<endl<<R<<endl;
    cout<<"t"<<endl<<t<<endl;
    cout<<"s_R"<<endl<<s_R<<endl;
    cout<<"s_t"<<endl<<s_t<<endl;
}

void pose_estimation_2d2d(
        const vector<KeyPoint>& keypoints_1,
        const vector<KeyPoint>& keypoints_2,
        const vector<DMatch>& matches,
        const Mat& K,
        Mat& R, Mat& t) {
    vector<Point2f> points1, points2;
    for (int i = 0; i < (int)matches.size(); i++) {
        points1.push_back(keypoints_1[matches[i].queryIdx].pt);
        points2.push_back(keypoints_2[matches[i].trainIdx].pt);
    }

    //计算基础矩阵
    Mat fundamental_matrix;
    fundamental_matrix = findFundamentalMat(points1, points2, CV_FM_8POINT);

    //-- 计算本质矩阵
    Point2d principal_point(K.at<double>(0, 2), K.at<double>(1, 2));
    int focal_length = ceil(max(K.at<double>(0, 0), K.at<double>(1, 1)));
    Mat essential_matrix;
    essential_matrix = findEssentialMat(points1, points2, focal_length, principal_point);

    //-- 计算单应矩阵
    Mat homography_matrix;
    homography_matrix = findHomography(points1, points2, RANSAC, 3);

    recoverPose(essential_matrix, points1, points2, R, t, focal_length, principal_point);
}

#endif //CAMERA_VIDEO_C_LUT_TRAINGULATION_H
