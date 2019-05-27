package georeach.smartwomensecurity.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.SQLiteDBHelper;


public class RegisterActivity extends AppCompatActivity{

    private EditText mInputName, mInputPassword, mInputEmail, mInputMobileNo;
    private Button mButtonRegister;
    private TextView mLoginTextView;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        openHelper = new SQLiteDBHelper(this);

        mInputName = (EditText) findViewById(R.id.nameEditText);
        mInputEmail = (EditText) findViewById(R.id.emailEditText);
        mInputPassword = (EditText) findViewById(R.id.passwordEditText);
        mInputMobileNo = (EditText) findViewById(R.id.mobileEditText);

        mButtonRegister = (Button) findViewById(R.id.buttonRegister);

        mLoginTextView = (TextView) findViewById(R.id.loginTextView);
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();

                String name = mInputName.getText().toString();
                String mobile = mInputMobileNo.getText().toString();
                String email = mInputEmail.getText().toString();
                String pass = mInputPassword.getText().toString();


                //VALIDATIONS
                if (name.equals("") || name == null) {

                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                }  else if (mobile.equals("") || mobile == null || mobile.length() != 10) {

                    Toast.makeText(getApplicationContext(), "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (email.equals("") || email == null || !validate(email)) {

                    Toast.makeText(getApplicationContext(), "Please enter a valid email id", Toast.LENGTH_SHORT).show();

                }
                else if (pass.equals("") || pass == null) {

                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else {

                    //Calling InsertData Method - Defined below
                    InsertData(name,mobile, email, pass);
                    //Alert dialog after clicking the Register Account
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("inform");
                    builder.setMessage("Your Account is Successfull registered");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Finishing the dialog and removing Activity from stack.
                            dialogInterface.dismiss();
                            finish();
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }



    public void InsertData(String Name,String mobile, String email, String password ) {

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_NAME,Name);
        values.put(SQLiteDBHelper.COLUMN_MOBILE,mobile);
        values.put(SQLiteDBHelper.COLUMN_EMAIL,email);
        values.put(SQLiteDBHelper.COLUMN_PASSWORD,password);

        long rowname = db.insert(SQLiteDBHelper.TABLE_NAME, null, values);

        if (rowname>0){
            Log.d("DATA", "Data Inserted");
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}

