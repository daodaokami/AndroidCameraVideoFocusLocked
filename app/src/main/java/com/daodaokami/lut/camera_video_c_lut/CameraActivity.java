package com.daodaokami.lut.camera_video_c_lut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daodaokami.lut.camera_video_c_lut.cex.CExFuncLoader;
import com.daodaokami.lut.camera_video_c_lut.tools.PicturePrcess;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static java.time.chrono.IsoEra.CE;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
  }

    private static String TAG = "CAMERA_ACTIVITY";
    private Button decomposeBtn;
    private boolean isDecomposedAvailable;
    private String videoPath;
    private PicturePrcess ppc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setContentView(R.layout.activity_camera);
        decomposeBtn = (Button)findViewById(R.id.btn_decompose_video);
        decomposeBtn.setOnClickListener(this);
        isDecomposedAvailable = false;
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2VideoFragment.newInstance())
                    .commit();
        }
        //STATE = STATE_DONOTHING;
        ppc = new PicturePrcess();
        EventBus.getDefault().register(this);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.tv_calculate_state);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    @Override
    public void onClick(View v) {
        /*if(vedioPath.isEmpty()){
            STATE = STATE_DONOTHING;
        }*/
        if(isDecomposedAvailable){
            Thread mPicThread = new Thread(ppc);
            mPicThread.start();
            //这里最好再开个线程来跑图像的处理
            Log.i(TAG, "onClick: is decompose available");
        }else{
            //nothing to do
            Log.i(TAG, "onClick: is decompose not available vedio not found");
        }
        //cExFuncLoader.getCameraPose(videoPath, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //ppc.GetIsOver(),状态转换还要考虑一下

    }

    @Subscribe
    public void onEventBackgroundThread(VedioReadyEvent vrEvent){
        Log.d(TAG, "messageEventBus: "+vrEvent.toString());
        isDecomposedAvailable = true;
        videoPath = vrEvent.getVedioPath();
        ppc.setVideoPath(videoPath);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
