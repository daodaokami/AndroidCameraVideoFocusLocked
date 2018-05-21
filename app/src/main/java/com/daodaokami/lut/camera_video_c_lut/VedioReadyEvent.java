package com.daodaokami.lut.camera_video_c_lut;

public class VedioReadyEvent {
    public String vedioPath;
    public VedioReadyEvent(String vedioPath){
        this.vedioPath = vedioPath;
    }
    public VedioReadyEvent(){

    }
    public String getVedioPath(){
        return this.vedioPath;
    }
    public void setVedioPath(String vedioPath){
        this.vedioPath = vedioPath;
    }
    @Override
    public String toString() {
        return vedioPath;
    }
}
