package georeach.smartwomensecurity.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;

import georeach.smartwomensecurity.Adapter.CustomContactAdapter;
import georeach.smartwomensecurity.Fragments.CustomMessage;
import georeach.smartwomensecurity.Fragments.CustomMessageGps;
import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.DbHelper;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Locale;


public class SendGps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, CustomMessageGps.SendCustomMessageGps {

   //Getting Last Known Location using Google Play Services --latest stuff in android
    GoogleApiClient mGoogleApiClient;

    private Button mBtnAddContact;
    private FloatingActionButton fabGps;
    private AutoCompleteTextView actv;
    private ArrayList<ModalContact> mContactGpsArrayList;
    private ListView mListViewContacts;
    private CustomContactAdapter mAdapter;
    DbHelper dbHelper;

    private String name, number;

    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mArrayAdapter;
    private String contct_name;


    public static String message = "Help I am in trouble at";

    //String to send message saved in shared preferences
    String newMessage;

    //Shared Preferences reference to store the custom typed text message
    SharedPreferences sharedPreferences2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_gps_layout);

        init();

        //Database Object
        dbHelper = new DbHelper(SendGps.this, "GpsContacts", null, 1);
        populate();


        //Creating object for API Client because we will get current location of the user using Google API Client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        actv.setFocusable(false);
        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(actv, InputMethodManager.SHOW_IMPLICIT);
            }
        });

//Obtaining SharedPreferences object and editor and fetching the message from database if stored
        sharedPreferences2 = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        newMessage = sharedPreferences2.getString("msgs", message);



        //CONTACTS FETCHING LOGIC
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                contct_name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                mArrayList.add(contct_name);

            }
        }




        mBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If edittext is empty and user clicks add button
                if (actv.getText().toString().equals("")){

                    Toast.makeText(SendGps.this, "Enter a name", Toast.LENGTH_SHORT).show();

                }
                else {

                    name = actv.getText().toString();
                    number = getPhoneNumber(name, SendGps.this);

                    if (number.equals("null")) {

                        Toast.makeText(SendGps.this, "Contact not found", Toast.LENGTH_SHORT).show();

                    } else {

                        ModalContact modalContact = new ModalContact(name, number);
                        boolean insert = dbHelper.AddGpsContact(modalContact);

                        if (insert) {
                            populate();
                            mt("Contact Added");
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mt("Could not add contact");
                        }
                    }
                }
            }
        });




        mListViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                //ALERT DIALOG to delete contact

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendGps.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want remove this contact?");
                alertDialog.setIcon(R.drawable.bin);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        ModalContact modalContact = new ModalContact(mContactGpsArrayList.get(position).getName(), mContactGpsArrayList.get(position).getMobile());
                        int delete = dbHelper.deleteGpsContact(modalContact);

                        if (delete>0)
                        {
                            populate();
                            mt("Contact Deleted");
                        }
                        else {
                            mt("Could not delete contact");
                        }

                   //     mContactGpsArrayList.remove(mContactGpsArrayList.get(position));
                        mAdapter.notifyDataSetChanged();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                return false;
            }
        });

    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

       //Getting last known location: Google play services
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);



        //Checking if last location is not null
        if (mLastLocation != null) {



            fabGps.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (mContactGpsArrayList.size()>0){

                        int n =  mContactGpsArrayList.size();


                        for (int i = 0; i <n ; i++) {
                            //Appending latitude and longitude in the url of google map to get current location
                            String url  = "http://maps.google.co.in/maps?q="+mLastLocation.getLatitude()+","+mLastLocation.getLongitude();

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(mContactGpsArrayList.get(i).getMobile(), null, newMessage+"\n"+url, null, null);
                            Toast.makeText(SendGps.this, "Location sent sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mt("First add a contact to hot contacts");
                    }

                    return false;
                }
            });



        }
        else
        {
            //IF YOU DEVICE GPS IS NOT ENABLED or YOU DONT HAVE PERMISSION TO USE LOCATION SERVICE or YOUR APP IS NOT GETTING YOUR LAST LOCATION
            Toast.makeText(this, "Check if you have gps enabled and the app has permission to use location services", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //Method to get contact number from android Contacts Application
    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "null";
        return ret;
    }

    private void populate() {

        mContactGpsArrayList.clear();
        mContactGpsArrayList.addAll(dbHelper.getAllGpsContacts());
        mAdapter.notifyDataSetChanged();

    }

    private void mt(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Set Alert Message");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Fragment to  write custom typed message
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CustomMessageGps customMessageGps = new CustomMessageGps();

        Bundle bundle = new Bundle();
        bundle.putString("defaultGps", newMessage);
        customMessageGps.setArguments(bundle);
        ft.addToBackStack("Message");
        ft.add(R.id.containerGps, customMessageGps);
        ft.commit();


        return super.onOptionsItemSelected(item);
    }

    private void init() {


        fabGps = (FloatingActionButton) findViewById(R.id.fabGps);
        mBtnAddContact = (Button) findViewById(R.id.btnAddContactGps);
        actv = (AutoCompleteTextView) findViewById(R.id.actv1);
        mListViewContacts = (ListView) findViewById(R.id.listViewContacts);
        mContactGpsArrayList = new ArrayList<ModalContact>();
        mAdapter = new CustomContactAdapter(SendGps.this, mContactGpsArrayList);
        mListViewContacts.setAdapter(mAdapter);
        mArrayList = new ArrayList<String>();

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
        actv.setThreshold(1);
        actv.setAdapter(mArrayAdapter);


    }


    @Override
    public void setCustomTexts(String s) {
        newMessage = s;
        sharedPreferences2.edit().putString("msgs", s).apply();

    }


}
