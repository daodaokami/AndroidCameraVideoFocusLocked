//
// Created by lut on 2018/5/18.
//

#ifndef CAMERA_VIDEO_C_LUT_FEATURES_MATCH_H
#define CAMERA_VIDEO_C_LUT_FEATURES_MATCH_H
#include "c_common_head.h"
using namespace std;
using namespace cv;

void getImg(const string filepath, Mat& img){
    string file = filepath + to_string(0)+".jpg";
    img = imread(file, IMREAD_COLOR);
}

void get_matchfeatures_orb(const Mat& img_1, const Mat& img_2,
                           vector<KeyPoint>& keypoints_1,
                           vector<KeyPoint>& keypoints_2,
                            Mat& descriptors_1, Mat& descriptors_2, vector<DMatch>& good_matches){
    Ptr<FeatureDetector> detector = ORB::create();
    Ptr<DescriptorExtractor> descriptor = ORB::create();
    /*Ptr<DescriptorMatcher> matcher  = DescriptorMatcher::create ( "BruteForce-Hamming" );
    //-- 第一步:检测 Oriented FAST 角点位置
    detector->detect ( img_1,keypoints_1 );
    detector->detect ( img_2,keypoints_2 );

    //-- 第二步:根据角点位置计算 BRIEF 描述子
    descriptor->compute ( img_1, keypoints_1, descriptors_1 );
    descriptor->compute ( img_2, keypoints_2, descriptors_2 );

    //-- 第三步:对两幅图像中的BRIEF描述子进行匹配，使用 Hamming 距离
    vector<DMatch> matches;
    //BFMatcher matcher ( NORM_HAMMING );
    matcher->match(descriptors_1, descriptors_2, matches, nullptr);

    double min_dist=10000, max_dist=0;

    //找出所有匹配之间的最小距离和最大距离, 即是最相似的和最不相似的两组点之间的距离
    for ( int i = 0; i < descriptors_1.rows; i++ )
    {
        double dist = matches[i].distance;
        if ( dist < min_dist ) min_dist = dist;
        if ( dist > max_dist ) max_dist = dist;
    }

    min_dist = min_element( matches.begin(), matches.end(), [](const DMatch& m1, const DMatch& m2) {return m1.distance<m2.distance;} )->distance;
    max_dist = max_element( matches.begin(), matches.end(), [](const DMatch& m1, const DMatch& m2) {return m1.distance<m2.distance;} )->distance;

    //当描述子之间的距离大于两倍的最小距离时,即认为匹配有误.但有时候最小距离会非常小,设置一个经验值30作为下限.
    for ( int i = 0; i < descriptors_1.rows; i++ )
    {
        if ( matches[i].distance <= max ( 2*min_dist, 30.0 ) )
        {
            good_matches.push_back ( matches[i] );
        }
    }*/
}

#endif //CAMERA_VIDEO_C_LUT_FEATURES_MATCH_H
