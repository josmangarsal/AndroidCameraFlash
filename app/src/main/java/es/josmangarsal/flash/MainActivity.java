package es.josmangarsal.flash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Switch flash_switch;
    Switch cam_switch;

    private CameraManager cameraManager;
    private String cameraId;
    private Boolean isTorchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check camera flash
        checkCamera();

        // Flash status switch
        flash_switch = (Switch) findViewById(R.id.flash_switch);
        cam_switch = (Switch) findViewById(R.id.cam_switch);

        isTorchOn = false;

        // Get camera
        getCamera(0);

        // Camera switch event
        cam_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cam_switch.isChecked()){
                    getCamera(0);
                    //Toast.makeText(getApplicationContext(), "Back camera", Toast.LENGTH_SHORT).show();
                } else {
                    getCamera(1);
                    //Toast.makeText(getApplicationContext(), "Front camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Flash switch event
        flash_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!flash_switch.isChecked()){
                        turnOffFlashLight();
                        //Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_SHORT).show();
                    } else {
                        turnOnFlashLight();
                        //Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkCamera() {
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }
    }

    private void getCamera(int id) {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            //Log.d("DEBUG", "Cameras: " + cameraManager.getCameraIdList().length);
            cameraId = cameraManager.getCameraIdList()[id];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
                isTorchOn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
                isTorchOn = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTorchOn){
            turnOnFlashLight();
        }
    }
}
