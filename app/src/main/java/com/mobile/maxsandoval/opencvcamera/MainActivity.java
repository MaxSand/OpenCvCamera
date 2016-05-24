package com.mobile.maxsandoval.opencvcamera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static {
        System.loadLibrary("opencv_java3");
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    private int mNumCameras;
    private List<Camera.Size> mSupportedImageSizes;

    private Spinner spinnerCamera, spinnerResolution, spinnerEffect;
    private Button start;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCamera = (Spinner) findViewById(R.id.spinnerCamera);
        spinnerResolution = (Spinner) findViewById(R.id.spinnerResolution);
        spinnerEffect = (Spinner) findViewById(R.id.spinnerEffect);
        start = (Button)findViewById(R.id.button);

        mNumCameras = Camera.getNumberOfCameras();
        Log.d(TAG + " Numero de camaras", String.valueOf(mNumCameras));

        List<String> listCamera;
        listCamera = new ArrayList<String>();
        for (int i = 0; i < mNumCameras; i++) {
            listCamera.add("Camera "+ (i+1));
        }
        ArrayAdapter<String> adapterCamera;
        adapterCamera = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCamera);
        adapterCamera.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCamera.setAdapter(adapterCamera);

        List<String> listEffect;
        listEffect = new ArrayList<String>();
        listEffect.add("Sepia");
        listEffect.add("Negativo");
        listEffect.add("Retirar un canal");
        listEffect.add("Borde");
        listEffect.add("Laplaciano");

        ArrayAdapter<String> adapterEffect;
        adapterEffect = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEffect);
        adapterEffect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEffect.setAdapter(adapterEffect);

        spinnerCamera.setOnItemSelectedListener(this);
        spinnerResolution.setOnItemSelectedListener(this);
        spinnerEffect.setOnItemSelectedListener(this);




        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int camera = spinnerCamera.getSelectedItemPosition();
                Log.d(TAG ,"Camara: "+String.valueOf(camera));
                String resolution = spinnerResolution.getSelectedItem().toString();
                Log.d(TAG ,"Resolucion: "+ resolution);
                int efecto = spinnerEffect.getSelectedItemPosition();
                Log.d(TAG ,"Efecto: "+String.valueOf(efecto));

                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("camara", camera);
                bundle.putString("resolucion", resolution);
                bundle.putInt("efecto", efecto);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCamera:

                Log.d(TAG + " Camara seleccionada", String.valueOf(position));

                Camera camera = Camera.open(position);
                Camera.Parameters parameters = camera.getParameters();
                camera.release();
                mSupportedImageSizes = parameters.getSupportedPreviewSizes();

                Log.d(TAG + " Numero de Resoluciones", String.valueOf(mSupportedImageSizes.size()));

                List<String> listResolution;
                listResolution = new ArrayList<String>();
                for (int i = 0; i < mSupportedImageSizes.size(); i++){

                    Camera.Size size = mSupportedImageSizes.get(i);
                    listResolution.add(size.width +" x "+ size.height);
                }

                ArrayAdapter<String> adapterResolution;
                adapterResolution = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listResolution);
                adapterResolution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerResolution.setAdapter(adapterResolution);

                break;

            case R.id.spinnerResolution:

                Log.d(TAG + " Resolucion seleccionada", String.valueOf(position));

                break;
            case R.id.spinnerEffect:

                Log.d(TAG + " Efecto seleccionado", String.valueOf(position));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
