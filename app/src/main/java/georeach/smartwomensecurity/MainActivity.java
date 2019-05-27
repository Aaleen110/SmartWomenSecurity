package georeach.smartwomensecurity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import georeach.smartwomensecurity.Activity.EmergencyCall;
import georeach.smartwomensecurity.Activity.SendGps;
import georeach.smartwomensecurity.Activity.SendMessage;
import georeach.smartwomensecurity.Activity.SpyAudioRecorder;
import georeach.smartwomensecurity.Activity.SpyCamera;
import georeach.smartwomensecurity.Activity.SpyVideo;
import georeach.smartwomensecurity.Fragments.AboutFragment;
import georeach.smartwomensecurity.Fragments.HelpFragment;
import georeach.smartwomensecurity.UserSession.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView mCardViewSendMessage, mCardViewSendGps, mCardViewSpyCamera, mCardViewSpyVideo, mCardViewSpyAudio, mCardViewEmergencyCall;
    SessionManager sessionManager;
    FrameLayout frameLayout;
    private TextView txtPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //INITALIZE VARIABLES
        init();
        requestAppPermissions();


        //ONCLICK LISTENERS FOR CARD CLICK



            mCardViewSendMessage.setOnClickListener(this);
            mCardViewSendGps.setOnClickListener(this);
            mCardViewSpyCamera.setOnClickListener(this);
            mCardViewSpyVideo.setOnClickListener(this);
            mCardViewSpyAudio.setOnClickListener(this);
            mCardViewEmergencyCall.setOnClickListener(this);




        sessionManager = new SessionManager(this);

        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetails();

   //      Password
        String pass = user.get(SessionManager.KEY_PASS);

     //    email
        String email = user.get(SessionManager.KEY_EMAIL);


        Snackbar snackbar = Snackbar
                .make(frameLayout, email+" Logged in", Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cardViewSendMessage:

                if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {

                    Intent intentMessage = new Intent(MainActivity.this, SendMessage.class);
                    startActivity(intentMessage);
                }else{
                    requestAppPermissions();
                }

                break;

            case R.id.cardViewSendGps:
                if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {
                    Intent intentGps = new Intent(MainActivity.this, SendGps.class);
                    startActivity(intentGps);
                }else{
                    requestAppPermissions();
                }




                break;

            case R.id.cardViewSpyCamera:
                if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {
                    Intent intentCamera = new Intent(MainActivity.this, SpyCamera.class);
                    startActivity(intentCamera);
                }else{
                    requestAppPermissions();
                }

                break;

            case R.id.cardViewSpyVideo:
                if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {

                    Intent intentVideo = new Intent(MainActivity.this, SpyVideo.class);
                    startActivity(intentVideo);
                }else{
                    requestAppPermissions();
                }

                break;

            case R.id.cardViewRecorder:
                if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {

                    Intent intentAudio = new Intent(MainActivity.this, SpyAudioRecorder.class);
                    startActivity(intentAudio);
                }else{
                    requestAppPermissions();
                }


                break;

            case R.id.cardViewEmergencyCall:
                    if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {
                        Intent intentEmergencyCall = new Intent(MainActivity.this, EmergencyCall.class);
                        startActivity(intentEmergencyCall);
                }else{
                    requestAppPermissions();
                }


        break;

        }

    }

    //OPTION MENU FOR HELP AND ABOUT

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();


        switch (item.getItemId()){
            case R.id.about_menu:
                AboutFragment aboutFragment = new AboutFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.container, aboutFragment);
                ft.addToBackStack("about")
                        .commit();


                break;
            case R.id.help_menu:
                HelpFragment helpFragment = new HelpFragment();
                FragmentTransaction ft1 = fm.beginTransaction();
                ft1.add(R.id.container, helpFragment);
                ft1.addToBackStack("about")
                        .commit();

                break;

            case R.id.logout:

                sessionManager.logoutUser();

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {

        mCardViewSendMessage = (CardView) findViewById(R.id.cardViewSendMessage);
        mCardViewSendGps = (CardView) findViewById(R.id.cardViewSendGps);
        mCardViewSpyCamera = (CardView) findViewById(R.id.cardViewSpyCamera);
        mCardViewSpyVideo = (CardView) findViewById(R.id.cardViewSpyVideo);
        mCardViewSpyAudio= (CardView) findViewById(R.id.cardViewRecorder);
        mCardViewEmergencyCall = (CardView) findViewById(R.id.cardViewEmergencyCall);
        frameLayout = (FrameLayout) findViewById(R.id.container);

    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasAudioPermissions() && hasCallPermissions() && hasCameraPermissions() && hasWritePermissions() && hasSMSPermissions() && hasACSPermissions() && hasAFLPermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION

                }, 1); // your request code
    }



    private boolean hasAudioPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasSMSPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasACSPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasCallPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasAFLPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
}
