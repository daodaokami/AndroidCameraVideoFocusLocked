package com.daodaokami.lut.camera_video_c_lut.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;
import android.util.Size;

import com.daodaokami.lut.camera_video_c_lut.cex.CExFuncLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicturePrcess implements Runnable {
    private static CExFuncLoader cExFuncLoader = new CExFuncLoader();

    private static String TAG = "PictureProcess";
    private String videoPath;
    private boolean isStart;
    private boolean isOver;
    public PicturePrcess(String videoPath){
        this.videoPath = videoPath;
        this.isOver = false;
    }
    public boolean GetIsOver(){
        return this.isOver;
    }
    public PicturePrcess(){
        this.isOver = false;
    }
    public String getVideoPath(){
        return this.videoPath;
    }
    public void setVideoPath(String videoPath){
        this.videoPath = videoPath;
    }

    public native double[] getCameraPose(int[] img1, int[] img2, int height, int width);

    public void video2pics(){
        video2pics(this.videoPath);
    }

    public void video2pics(String videoPath){
        if(videoPath.isEmpty()){
            if(this.videoPath.isEmpty()){
                Log.i(TAG, "vedio2pics: error no vedio path!");
                return;
            }else{
                videoPath = this.videoPath;
            }
        }
        videoDepose(videoPath);//这里都是Java写的视频帧化
    }
    public int[] getPicture(String picPath){
        int[] datas = null;
        Bitmap bitmap;
        FileInputStream fis = null;
        File picFile;
        picFile = new File(picPath);
        try{

            fis = new FileInputStream(picFile);
            Log.i(TAG, "getPicture: file size is "+fis.available()+".");
            bitmap = BitmapFactory.decodeStream(fis);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            datas = new int[w*h];
            bitmap.getPixels(datas, 0, w, 0, 0, w, h);
            Log.i(TAG, "getPicture: save pic in byte[]!");
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public Point getPictureSize(String picPath){//先高，后长
        Bitmap bitmap;
        FileInputStream fis = null;
        File picFile;
        picFile = new File(picPath);
        int height, width;
        Point p = new Point();
        try{
            fis = new FileInputStream(picFile);
            Log.i(TAG, "getPicture: file size is "+fis.available()+".");
            bitmap = BitmapFactory.decodeStream(fis);
            height = bitmap.getHeight();
            width = bitmap.getWidth();
            p.x = height;
            p.y = width;
            Log.i(TAG, "getPicture: save pic in byte[]!");
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
    private void videoDepose(String videoPath){
        File file = new File(videoPath);
        Log.i(TAG, "videoDepose: "+ Environment.getExternalStorageDirectory());
        if(file.exists()){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.v(TAG, "Time:"+ time);
            int seconds = Integer.valueOf(time) / 1000;
            int posEndFlag = videoPath.lastIndexOf("/");
            String picPath = videoPath.substring(0, posEndFlag+1);

            for (int i = 0; i < seconds; i++) {
                Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                String pathPic = picPath + File.separator + i + ".jpg";

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pathPic);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);//不压缩
                    Log.v(TAG, "Picture Got");
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.isOver = true;
        }else{
            Log.i(TAG, "videoDepose: no vedio error!");
        }
    }

    @Override
    public void run() {
        video2pics();
        int height, width;
        String videoPath = getVideoPath();
        int posEndFlag = videoPath.lastIndexOf("/");
        String picPath = videoPath.substring(0, posEndFlag+1);

        Point point = getPictureSize(picPath+"0.jpg");
        height = point.x;
        width = point.y;
        String picPath0, picPath1;
        picPath0 = picPath+"0.jpg";
        picPath1 = picPath+"1.jpg";
        int[] img0 = getPicture(picPath0);
        int[] img1 = getPicture(picPath1);
        double[] rt = getCameraPose(img0, img1, height, width);
    }
}
