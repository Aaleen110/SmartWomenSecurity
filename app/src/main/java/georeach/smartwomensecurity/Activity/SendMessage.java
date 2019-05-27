package georeach.smartwomensecurity.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import georeach.smartwomensecurity.Adapter.CustomContactAdapter;
import georeach.smartwomensecurity.Fragments.CustomMessage;
import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.DbHelper;


public class SendMessage extends AppCompatActivity implements CustomMessage.SendCustomMessage{

    private Button mBtnAddContact;
    private FloatingActionButton fab;
    private AutoCompleteTextView actv;
    private ArrayList<ModalContact> mContactAddArrayList;
    private ListView mListViewContacts;
    private CustomContactAdapter mAdapter;
    private DbHelper dbHelper;
    private String name, number;

    //ArrayList and ArrayAdapet for getting and storing names and number for autocomplete textview
    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mArrayAdapter;
    private String contct_name;

    //Default Message to send if not specified
    public static String message = "Help I am in trouble";

    //String to send message saved in shared preferences
    String newMessage;

    //Shared Preferences reference to store the custom typed text message
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_layout);


      //init method to initialize elements
        init();

         dbHelper = new DbHelper(this, "Contacts", null, 1);

        //populate method to populate the list onCreate to see contacts when the activity is opened
          populate();


        //Obtaining SharedPreferences object and editor and fetching the message from database if stored
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        newMessage = sharedPreferences.getString("msg", message);


        actv.setFocusable(false);
        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(actv, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        //CONTACTS FETCHING from Contacts application android LOGIC
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

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

                //Validation if edit text is empty and user hits Add contact button
                if (actv.getText().toString().equals("")){
                    Toast.makeText(SendMessage.this, "Enter a name", Toast.LENGTH_SHORT).show();
                }
                else {
                    name = actv.getText().toString();
                    number = getPhoneNumber(name, SendMessage.this);


                    //Validation if number not found from Contacts application
                    if (number.equals("null")) {

                        Toast.makeText(SendMessage.this, "Contact not found", Toast.LENGTH_SHORT).show();

                    } else {

                        //Savind contact in SQLite dattabase
                        ModalContact modalContact = new ModalContact(name, number);
                        boolean insert = dbHelper.AddContact(modalContact);

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


        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                //Validate if contacts are present in List
                if (mContactAddArrayList.size()>0){

                    //Getting size of arraylist where contacs are stored
                   int n =  mContactAddArrayList.size();

                    //sending message logic: to send message to all the contacts in the list.
                    for (int i = 0; i <n ; i++) {

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(mContactAddArrayList.get(i).getMobile(), null, newMessage, null, null);
                        Toast.makeText(SendMessage.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    mt("First add a contact to hot contacts");
                }

                return false;
            }
        });



        mListViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                //ALERT DIALOG TO DELETE CONTACT

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendMessage.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want remove this contact?");
                alertDialog.setIcon(R.drawable.bin);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        ModalContact modalContact = new ModalContact(mContactAddArrayList.get(position).getName(), mContactAddArrayList.get(position).getMobile());
                        int delete = dbHelper.deleteContact(modalContact);

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

                return false;
            }
        });
    }


    private void populate() {

        mContactAddArrayList.clear();
        mContactAddArrayList.addAll(dbHelper.getAllContacts());
        mAdapter.notifyDataSetChanged();

    }

    private void mt(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Set Alert Message");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CustomMessage customMessage = new CustomMessage();

        Bundle bundle = new Bundle();
        bundle.putString("default", newMessage);
        customMessage.setArguments(bundle);
        ft.addToBackStack("Message");
        ft.add(R.id.containerMessage, customMessage);
        ft.commit();
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mBtnAddContact = (Button) findViewById(R.id.btnAddContact);
        actv = (AutoCompleteTextView) findViewById(R.id.actv);
        mListViewContacts = (ListView) findViewById(R.id.listViewContacts);
        mContactAddArrayList = new ArrayList<ModalContact>();
        mAdapter = new CustomContactAdapter(SendMessage.this, mContactAddArrayList);
        mListViewContacts.setAdapter(mAdapter);

        actv = (AutoCompleteTextView) findViewById(R.id.actv);
        mArrayList = new ArrayList<String>();

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
        actv.setThreshold(1);
        actv.setAdapter(mArrayAdapter);
    }


    //Getting custom message from CUSTOM MESSAGE fragment via an interface
    @Override
    public void setCustomText(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        newMessage = s;
    sharedPreferences.edit().putString("msg", s).apply();

    }

}
