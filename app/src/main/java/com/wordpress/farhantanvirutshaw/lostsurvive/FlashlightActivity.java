package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FlashlightActivity extends AppCompatActivity
{
    boolean hasFlash,isOn;
    Camera camera;
    Camera.Parameters p;
    Button bt;

    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);

        bt= (Button) findViewById(R.id.torchButton);
        hasFlash=getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

    }

    public void torchOnOff(View view)
    {
        if(!hasFlash)
        {
            Toast t=Toast.makeText(this,"your phone do not support flash",Toast.LENGTH_SHORT);
            t.show();

            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            if(hasCameraPermission != PackageManager.PERMISSION_GRANTED)
            {
                if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                {
                    showMessageOKCancel("You need to allow access to your Camera",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
                return;
            }
        }

        if(isOn)
        {
            turnFlashLightOff();
            isOn=false;
            bt.setBackgroundResource(R.drawable.button2);
        }

        else
        {
            turnFlashLightOn();
            isOn=true;
            bt.setBackgroundResource(R.drawable.button1);
        }
    }

    public void turnFlashLightOn()
    {
        try
        {
            camera=Camera.open();
            p=camera.getParameters();

            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();

            Toast toast=Toast.makeText(this,"Flashlight Turned On",Toast.LENGTH_SHORT);
            toast.show();

        }

        catch (Exception e)
        {
            Toast toast=Toast.makeText(this,"An Error Ocurred While Opening The Flahlight",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void turnFlashLightOff()
    {
        try
        {
            p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            Toast toast=Toast.makeText(this,"Flashlight Turned Off",Toast.LENGTH_SHORT);
            toast.show();
        }

        catch (Exception e)
        {
            Toast toast=Toast.makeText(this,"An Error Ocurred While Opening The Flahlight",Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (isOn) {
            turnFlashLightOn();
            bt.setBackgroundResource(R.drawable.button1);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (isOn) {
            turnFlashLightOn();
            bt.setBackgroundResource(R.drawable.button1);
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        if (isOn)
        {
            turnFlashLightOff();
            bt.setBackgroundResource(R.drawable.button2);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(FlashlightActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
