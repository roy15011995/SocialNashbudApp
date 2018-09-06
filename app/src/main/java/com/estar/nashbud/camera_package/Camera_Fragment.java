package com.estar.nashbud.camera_package;


//import android.graphics.Camera;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.estar.nashbud.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Camera_Fragment extends Fragment {

    FrameLayout frameLayout;
    Camera camera;
    Show_camera show_camera;
    LinearLayout flash;
    ImageView CameraOpen,CameraRotate;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    Context context;
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";
    int currentCameraId = 0;
    Activity activity;
    Uri FilePathUri;
    private int REQUEST_CAMERA = 0;

    private String cameraId = CAMERA_BACK;
    ArrayList<String> add_Image = new ArrayList<>();
    private  List<Camera.Size> sizes;
    private Camera.Size previewSize;

    public Camera_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_, container, false);
        frameLayout = (FrameLayout)view.findViewById(R.id.camera_frame);
        CameraOpen = (ImageView)view.findViewById(R.id.camera_open);
        CameraRotate = (ImageView)view.findViewById(R.id.rotate);
        flash = (LinearLayout)view.findViewById(R.id.flash_on);

        context = this.getActivity();
        activity = getActivity();

        final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                AudioManager mgr = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
            }
        };


        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashLightOn(v);
            }
        });

        final Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File picture_file = getOutputMediaFile();
                //Log.e("GetPictureFile",""+picture_file);
                if(picture_file == null){
                    return;
                }
                else
                {
                    try {
                        FileOutputStream fileOutputStream=new FileOutputStream(picture_file);
                        Log.e("FileOutPutStream",""+fileOutputStream);

                        fileOutputStream.write(data);
                        fileOutputStream.close();

                          add_Image.add(picture_file.getAbsolutePath());

                        Intent i = new Intent(getActivity(),ShowImageActivity.class);
                        Log.e("ImageAdded",""+picture_file.getAbsolutePath());
                        i.putExtra("CAMERA_IMAGE", picture_file.getAbsolutePath());
                        getActivity().finish();
                        startActivity(i);

                          //camera.startPreview();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Check Memory space in Device",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        CameraRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipcamera();

            }
        });

        CameraOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    camera = Camera.open();
                    SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
                    camera.setPreviewTexture(st);
                    camera.startPreview();
                    camera.takePicture(shutterCallback, null, mPictureCallback);
                }catch (Exception ex){
                    Log.e("Exception Error", ex.getMessage());
                }
            }
        });

        return view;

    }

    public void flashLightOn(View view) {

        try {

                camera = Camera.open();
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getBaseContext(), "Exception flashLightOn()",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void flashLightOff(View view) {
        try {
            if (getActivity().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getBaseContext(), "Exception flashLightOff",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void flipcamera() {

      //  if (inPreview) {
            camera.stopPreview();
        //}
//NB: if you don't release the current camera before switching, you app will crash
        camera.release();

//swap the id of the camera to be used
        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera = Camera.open(currentCameraId);

     //   setCameraDisplayOrientation(CameraActivity.this, currentCameraId, camera);
        try {
            show_camera = new Show_camera(this.getActivity(),camera ,"rear");
            frameLayout.addView(show_camera);
          //  camera.setPreviewDisplay(previewHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }

    @Override
    public void onPause() {
        super.onPause();

        if(frameLayout!=null){
            frameLayout.removeView(show_camera);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        camera=Camera.open();
        if(frameLayout!=null){

            if(camera!=null){
                try{
                    show_camera = new Show_camera(this.getActivity(),camera,"front");
                    frameLayout.addView(show_camera);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    public File getOutputMediaFile() {
       String state = Environment.getExternalStorageState();
       if(!state.equals(Environment.MEDIA_MOUNTED)){
           return null;
       }
       else {
           File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "NashBud Images");

           if(!folder_gui.exists()){
               folder_gui.mkdir();
           }
           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

           File outputFile = new File(folder_gui, timeStamp+".jpg");

           return outputFile;
       }
    }

    public class Show_camera extends SurfaceView implements SurfaceHolder.Callback {
        Camera camera;
        SurfaceHolder surfaceHolder;
        String camera_direction;

        public Show_camera(Context context, Camera camera, String camera_direction) {
            super(context);
            surfaceHolder = getHolder();
            this.camera = camera;
            sizes = camera.getParameters().getSupportedPictureSizes();
            this.camera_direction = camera_direction;
            surfaceHolder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            Camera.Parameters parameters = camera.getParameters();

            sizes = parameters.getSupportedPictureSizes();
            Camera.Size msize = null;

            for (Camera.Size size : sizes) {
                msize = size;
            }

            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
                parameters.setPreviewSize(msize.width, msize.height);
            } else {
                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
                parameters.setPreviewSize(msize.width, msize.height);
            }


            parameters.setPictureSize(msize.width, msize.height);

            camera.setParameters(parameters);

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay()
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
        Log.e("GetRotate ",""+rotate);
        Camera.Parameters params = camera.getParameters();

        params.setRotation(rotate);
        camera.setParameters(params);
        camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

            if (sizes != null) {
                previewSize = getOptimalPreviewSize(sizes, width, height);
            }
            float ratio;
            if(previewSize.height >= previewSize.width)
                ratio = (float) previewSize.height / (float) previewSize.width;
            else
                ratio = (float) previewSize.width / (float) previewSize.height;

            setMeasuredDimension(width, (int) (width * ratio));
        }


        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) h / w;

            if (sizes == null)
                return null;

            Camera.Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            for (Camera.Size size : sizes) {
                double ratio = (double) size.height / size.width;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;

                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }

            return optimalSize;
        }

    }


}
