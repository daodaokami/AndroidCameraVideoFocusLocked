cmake_minimum_required(VERSION 3.4.1)

set(OpenCV_DIR D:/OpenCV-android-sdk/sdk/native/jni)
find_package(OpenCV REQUIRED)

include_directories( ${PROJECT_SOURCE_DIR}/include )

add_library( native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

find_library(
              log-lib
              log )

include_directories(src/main/cpp/include)

target_link_libraries(
                       native-lib
                       ${log-lib}
                       ${OpenCV_LIBS}
)