package com.mobile.maxsandoval.opencvcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    static {
        System.loadLibrary("opencv_java3");
    }

    //Camera View.
    private CameraBridgeViewBase mCameraView;

    //sepia
    private Mat mSepiaKernel;
    private Mat mBorde;

    //parametros
    int camera = 0;
    String resolution;
    String w = "1280", h= "960";
    int x, y;
    int efecto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            camera = bundle.getInt("camara");
            resolution = bundle.getString("resolucion");
            efecto = bundle.getInt("efecto");

            w = resolution.substring(0, resolution.indexOf("x")).trim();
            Log.e("Resolucion X:", String.valueOf(w));
            h = resolution.substring(resolution.indexOf("x")+1, resolution.length()).trim();
            Log.e("Resolucion Y: ", String.valueOf(h));
        }
        x = Integer.parseInt(w);
        y = Integer.parseInt(h);
        mCameraView = new JavaCameraView(this, camera);
        mCameraView.setMaxFrameSize(x, y);
        mCameraView.setCvCameraViewListener(CameraActivity.this);
        mCameraView.enableView();
        setContentView(mCameraView);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

        mSepiaKernel = new Mat(4, 4, CvType.CV_32F);
        mSepiaKernel.put(0, 0, /* R */0.189f, 0.769f, 0.393f, 0f);
        mSepiaKernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
        mSepiaKernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
        mSepiaKernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);

        mBorde = new Mat(4, 4, CvType.CV_32F);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final Mat rgba=inputFrame.rgba();
        final Mat destino = new Mat();

        switch (efecto) {

            case 0:
                Size sizeRgba = rgba.size();
                Mat rgbaInnerWindow;
                int rows = (int) sizeRgba.height;
                int cols = (int) sizeRgba.width;

                int left = cols / 8;
                int top = rows / 8;
                int width = cols * 3 / 4;
                int height = rows * 3 / 4;

                rgbaInnerWindow = rgba.submat(top, top + height, left, left + width);
                Core.transform(rgbaInnerWindow, rgbaInnerWindow, mSepiaKernel);
                rgbaInnerWindow.release();
                break;

            case 1:

                break;
            case 2:

                break;
            case 3:

                Imgproc.blur (rgba, destino, new Size(10,10));
                return  destino;

            case 4:
                Mat src_gray = inputFrame.gray();

                Imgproc.Laplacian(src_gray,destino,CvType.CV_8UC1,3,.5,.5);
                return destino;

        }
        return rgba;
    }
}
