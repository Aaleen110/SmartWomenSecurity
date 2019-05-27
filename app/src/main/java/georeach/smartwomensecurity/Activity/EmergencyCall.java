package georeach.smartwomensecurity.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import georeach.smartwomensecurity.Adapter.CustomContactAdapter;
import georeach.smartwomensecurity.Fragments.EmergencyContacts;
import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.DbHelper;


public class EmergencyCall extends AppCompatActivity implements SensorListener {

    private AutoCompleteTextView actv;
    private ListView mListViewEmergency;
    private FloatingActionButton fabAdd;
    private ArrayList<ModalContact> mEmergencyContactList;
    private CustomContactAdapter mAdapter;
    private String contact_name;
    DbHelper dbHelper;

    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mArrayAdapter;

    String name, number, contact;


    //Variables for Sensor
    private SensorManager sensorMgr;
    private static final int SHAKE_THRESHOLD = 800;
    long lastUpdate = 0;
    float x, y, z, last_x, last_y, last_z;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_call_layout);
        init();


        dbHelper = new DbHelper(this, "EmergencyContact", null, 1);
        populate();

        actv.setFocusable(false);
        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(actv, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        //CONTACTS FETCHING LOGIC
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                /*
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                */
                contact_name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                mArrayList.add(contact_name);

               /* if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        contact_phone = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    pCur.close();
                }
*/
            }
        }

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (actv.getText().toString().equals("")) {
                    Toast.makeText(EmergencyCall.this, "Enter a name", Toast.LENGTH_SHORT).show();
                } else {


                    name = actv.getText().toString();
                    number = getPhoneNumber(name, EmergencyCall.this);

                    if (number.equals("null")) {

                        Toast.makeText(EmergencyCall.this, "Contact not found", Toast.LENGTH_SHORT).show();

                    } else {
                        ModalContact modalContact = new ModalContact(name, number);
                        boolean insert = dbHelper.addEmergencyContact(modalContact);

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


        mListViewEmergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mEmergencyContactList.get(position).getMobile()));
                if (ActivityCompat.checkSelfPermission(EmergencyCall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });



   mListViewEmergency.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
       @Override
       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

           //Alert Dialaog to alert user before deleting a contact
           AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmergencyCall.this);
           alertDialog.setTitle("Confirm Delete...");
           alertDialog.setMessage("Are you sure you want remove this contact?");
           alertDialog.setIcon(R.drawable.bin);
           alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog,int which) {

                   ModalContact modalContact = new ModalContact(mEmergencyContactList.get(position).getName(), mEmergencyContactList.get(position).getMobile());
                   int delete = dbHelper.deleteEmergencyContact(modalContact);

                   if (delete>0)
                   {
                       populate();
                       mt("Contact Deleted");
                   }
                   else {
                       mt("Could not delete contact");
                   }
                   mAdapter.notifyDataSetChanged();
               }
           });

           alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           });
           alertDialog.show();


           return true;
       }
   });






        }

    private void mt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void populate() {
              //Populates Listview with contacts
        mEmergencyContactList.clear();
        mEmergencyContactList.addAll(dbHelper.getAllEmergecyContacts());

    }

    //Method to get Contact from Contacts App in Android
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


    private void init() {
        actv = (AutoCompleteTextView) findViewById(R.id.actv);
        mListViewEmergency = (ListView) findViewById(R.id.listEmergencyContacts);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        mEmergencyContactList = new ArrayList<ModalContact>();
        mAdapter = new CustomContactAdapter(this, mEmergencyContactList);
        mListViewEmergency.setAdapter(mAdapter);

        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
        actv.setThreshold(1);
        actv.setAdapter(mArrayAdapter);

        //Sensor Manager
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);




    }


    @Override
    public void onSensorChanged(int sensor, float[] values) {


        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);


                    //ONLY CALL iF SHAKE is MORE THAN 1500 (Shake measurrement)
                    if (speed>1500) {

                        //CALL LOGIC

                        if (mEmergencyContactList.size() > 0) {

                            int n = mEmergencyContactList.size();


                            for (int i = 0; i <n ; i++) {
                             contact =    mEmergencyContactList.get(i).getMobile();
                                  break;
                            }

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + contact));


                            if (ActivityCompat.checkSelfPermission(EmergencyCall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                return;
                            }
                            startActivity(callIntent);

                        }
                        else{
                            Toast.makeText(EmergencyCall.this, "Please add a number first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }


    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Emergency Numbers");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        EmergencyContacts emergencyContacts = new EmergencyContacts();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.containerEmergency, emergencyContacts);
        ft.addToBackStack("EMER");
        ft.commit();


        return super.onOptionsItemSelected(item);
    }
}

