package com.estar.nashbud.camera_package;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 28-03-2018.
 */

public class Show_camera extends SurfaceView implements SurfaceHolder.Callback{
    Camera camera;
    SurfaceHolder surfaceHolder;
    public Show_camera(Context context , Camera camera) {
        super(context);
        surfaceHolder=getHolder();
        this.camera=camera;
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters parameters = camera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size msize = null;

        for(Camera.Size size : sizes){

            msize = size;

        }


        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){

            parameters.set("orientation","portrait");
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }
        else
        {
            parameters.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }

        parameters.setPictureSize(msize.width,msize.height);

        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        /*Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int rotation = this.getWindowManager().getDefaultDisplay()
                .getRotation();


        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; // Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; // Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;// Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;// Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;
        Camera.Parameters params = camera.getParameters();
        params.setRotation(rotate);
        camera.setParameters(params);
        camera.setDisplayOrientation(90);
        camera.startPreview();*/

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       camera.stopPreview();
       camera.release();
    }
}
