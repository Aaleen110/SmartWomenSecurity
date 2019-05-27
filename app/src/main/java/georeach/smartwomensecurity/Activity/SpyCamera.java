package georeach.smartwomensecurity.Activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import georeach.smartwomensecurity.R;

/**
 * Created by cepl-pc on 02-06-2017.
 */

public class SpyCamera extends Activity {

 //The Camera class is deprecated but it can be used.
 // Google has got a new class hardware Camera2 for doing camera stuffs but that requires api level 18+
    //So dont worry about the crossed Camera. :)

    Camera c;
    private boolean safeToTakePicture = false;

    //We will set surfaceview to 1dp, 1dp because we do not want any view as it is a spy camera
    //Also we will set background color black to Frame layoout that covers the screen
    private FrameLayout layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.spy_camera_layout);

//Setting Landscape orientation and Full screen Activity without even notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        layout = (FrameLayout) findViewById(R.id.layout);


        //Setting onClickListener to layout so if the user clicks anywhere on the screen it should click pictures
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safeToTakePicture) {
                    c.setDisplayOrientation(90);
                    c.startPreview();
                    c.takePicture(null, null, mPicture);
                    safeToTakePicture = false;
                }

            }
        });

    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("test", "Photo is captured");

            if (data!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                if(bitmap!=null){

                    File file=new File(Environment.getExternalStorageDirectory()+"/pics");
                    if(!file.isDirectory()){
                        file.mkdir();
                    }

                    file=new File(Environment.getExternalStorageDirectory()+"/pics",System.currentTimeMillis()+".jpg");


                    try
                    {
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);

                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    catch(Exception exception)
                    {
                        exception.printStackTrace();
                    }

                }else {
                    Toast.makeText(SpyCamera.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

               // MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "test title", "test desc");

            }

            //finished saving picture
            safeToTakePicture = true;
        }


    };

    @Override
    protected void onStart() {
        super.onStart();

        //open camera

        c = Camera.open();

       //SurfaceView that displays the space when camera is open, used for debugging, now set to 1dp, 1dp as we do not want to display camera
        SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = sv.getHolder();

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    c.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                c.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                c.startPreview();
                safeToTakePicture = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    //Stop Camera
    @Override
    protected void onStop() {
        super.onStop();
        c.stopPreview();
        c.release();

    }
}