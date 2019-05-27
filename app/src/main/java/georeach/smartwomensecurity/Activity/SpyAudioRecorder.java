package georeach.smartwomensecurity.Activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import georeach.smartwomensecurity.R;

/**
 * Created by Aaleen on 6/6/2017.
 */

public class SpyAudioRecorder extends Activity {

    MediaRecorder mMediaRecorder;
    Boolean recording = false;
    SurfaceView surfaceView;

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(SpyAudioRecorder.this, "Touch anywhere to start spy audio", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.spy_audio_recorder);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mMediaRecorder = new MediaRecorder();

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setClickable(true);


        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recording) {
                    mMediaRecorder.stop();
                    Toast.makeText(SpyAudioRecorder.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                } else {
                    recording = true;
                    mMediaRecorder.start();
                    Toast.makeText(SpyAudioRecorder.this, "Recording started", Toast.LENGTH_SHORT).show();

                }


            }

        });


        try {

            File newFile = File.createTempFile("audio", ".3gp", Environment.getExternalStorageDirectory());

            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setOutputFile(newFile.getAbsolutePath());
            mMediaRecorder.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
