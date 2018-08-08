package com.example.mdashikulislam.cameraflashlightapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView imageFlashLight;
    private Button btnEnable;
    private static final int CAMERA_REQUEST = 50;
    private  boolean flasLightStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFlashLight = findViewById(R.id.imageView);
        btnEnable = findViewById(R.id.enableCamera);


        final boolean hasCameraFlashLight = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnable = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

       btnEnable.setEnabled(!isEnable);
       imageFlashLight.setEnabled(isEnable);

       btnEnable.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);
           }
       });

       imageFlashLight.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (hasCameraFlashLight){
                   if (flasLightStatus){
                       flashLightOff();
                   }else{
                       flashLightOn();
                   }
               }else{
                   Toast.makeText(MainActivity.this,"No Flash Light Available",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }

    private void flashLightOff(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,false);
            flasLightStatus = false;
            imageFlashLight.setImageResource(R.drawable.off);
        } catch (CameraAccessException e) {

        }
    }
    private void flashLightOn(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);
            flasLightStatus = true;
            imageFlashLight.setImageResource(R.drawable.on);
        } catch (CameraAccessException e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    btnEnable.setEnabled(false);
                    btnEnable.setText("Camera Enable");
                    imageFlashLight.setEnabled(true);
                }else {
                    Toast.makeText(MainActivity.this,"Permission Denied for camera",Toast.LENGTH_LONG).show();
                }
             break;
        }
    }
}
