package com.daodaokami.lut.camera_video_c_lut.tools;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class PicturePrcess implements Runnable {
    private static String TAG = "PictureProcess";
    private String vedioPath;
    private boolean isStart;
    private boolean isOver;
    public PicturePrcess(String vedioPath){
        this.vedioPath = vedioPath;
        this.isOver = false;
    }
    public boolean GetIsOver(){
        return this.isOver;
    }
    public PicturePrcess(){
        this.isOver = false;
    }
    public String getVedioPath(){
        return this.vedioPath;
    }
    public void setVedioPath(String vedioPath){
        this.vedioPath = vedioPath;
    }
    public void vedio2pics(){
        vedio2pics(this.vedioPath);
    }
    public void vedio2pics(String vedioPath){
        if(vedioPath.isEmpty()){
            if(this.vedioPath.isEmpty()){
                Log.i(TAG, "vedio2pics: error no vedio path!");
                return;
            }else{
                vedioPath = this.vedioPath;
            }
        }
        videoDepose(vedioPath);
    }
    private void videoDepose(String vedioPath){
        File file = new File(vedioPath);
        Log.i(TAG, "videoDepose: "+ Environment.getExternalStorageDirectory());
        if(file.exists()){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(vedioPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.v(TAG, "Time:"+ time);
            int seconds = Integer.valueOf(time) / 1000;
            int posEndFlag = vedioPath.lastIndexOf("/");
            String picPath = vedioPath.substring(0, posEndFlag+1);

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
        vedio2pics();
    }
}
