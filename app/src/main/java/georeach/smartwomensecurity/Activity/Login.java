package georeach.smartwomensecurity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import georeach.smartwomensecurity.Fragments.ForgotPassFragment;
import georeach.smartwomensecurity.MainActivity;
import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.SQLiteDBHelper;
import georeach.smartwomensecurity.UserSession.SessionManager;

public class Login extends AppCompatActivity {

    private EditText input_email;
    private EditText input_password;
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase db;
    Cursor cursor;
    private Button btn_login, btn_Register;

    //Made SessionManager class to handle user sessions
   SessionManager sessionManager;

    private TextView  textviewForgotPassword;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);


        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_Register = (Button) findViewById(R.id.btn_register);
        textviewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);

      sessionManager = new SessionManager(this);

        if(sessionManager.isLoggedIn()) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

        }else {

            //Opening SQLite
            dbhelper = new SQLiteDBHelper(this);
            db = dbhelper.getReadableDatabase();


            btn_Register.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });


            textviewForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Validation for empty edittext and forgot password being hit
                    if (input_email.getText().toString().equals("")) {

                        Toast.makeText(Login.this, "Enter an email to change password", Toast.LENGTH_SHORT).show();
                    } else {

                        //Method to check if Email Id exist in Database or not...this will return a boolean
                        if (CheckIsEmailAlreadyInDBorNot(SQLiteDBHelper.TABLE_NAME, SQLiteDBHelper.COLUMN_EMAIL, input_email.getText().toString())) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
                            alertDialog.setTitle("Forgot Password..?");
                            alertDialog.setMessage("An OTP will be sent to you shortly...");
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    //Using Random class to generate Random OTP
                                    Random r = new Random();

                                    //Minimum to maximum
                                    int Low = 100000;
                                    int High = 200000;
                                    int randomOtp = r.nextInt(High - Low) + Low;

                                    //Getting mobile number from database on basis of Email id entered
                                    Cursor c = db.rawQuery("SELECT * FROM login WHERE TRIM(email) = '" + input_email.getText().toString().trim() + "'", null);
                                    c.moveToFirst();
                                    String mobile_number = c.getString(c.getColumnIndex("mobile"));

                                    //Sms manager to send OTP to user via sms
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile_number, null, "Your OTP is:\n" + randomOtp, null, null);

                                    ForgotPassFragment forgotPassFragment = new ForgotPassFragment();
                                    FragmentManager fm = getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    //Also passing the OTP to fragment forgot password in order to validate the OTP
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("otp", randomOtp);
                                    bundle.putString("email", input_email.getText().toString());
                                    forgotPassFragment.setArguments(bundle);
                                    ft.add(R.id.containerLogin, forgotPassFragment);
                                    ft.addToBackStack("login");
                                    ft.commit();


                                }
                            });

                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.show();


                        }
                        //IF email id in edittext does not match with the email ids in database
                        else {
                            Toast.makeText(Login.this, "Email Id does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String quer = "Select * from " + SQLiteDBHelper.TABLE_NAME;

                    Log.d("QUERY", quer);

                    String email = input_email.getText().toString();
                    String pass = input_password.getText().toString();

                    if (email.equals("") || email == null) {
                        Toast.makeText(getApplicationContext(), "Please enter Email address", Toast.LENGTH_SHORT).show();
                    } else if (pass.equals("") || pass == null) {
                        Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                    } else {

                        //Method to see if the userid and password exist in database...returns boolean
                        if (isLogin(email, pass)) {
                            Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

                            //Calling SessionManager class method createLoginSession to create a session by adding credentials in Shared Prefs
                            sessionManager.createLoginSession(pass, email);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);


                        } else {

                            //I am showing Alert Dialog Box here for alerting user about wrong credentials
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Username or Password is wrong.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }
                }
            });

        }

    }


    boolean isLogin(String email,String pass){
        cursor = db.rawQuery("SELECT *FROM " + SQLiteDBHelper.TABLE_NAME + " WHERE " + SQLiteDBHelper.COLUMN_EMAIL + "=? AND " + SQLiteDBHelper.COLUMN_PASSWORD + "=?", new String[]{email, pass});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return  true;
            }
        }
        return false;
    }

    public boolean CheckIsEmailAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {

        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}

