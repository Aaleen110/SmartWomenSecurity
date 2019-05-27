package georeach.smartwomensecurity.Fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import georeach.smartwomensecurity.R;
import georeach.smartwomensecurity.SQLite.SQLiteDBHelper;


public class ChangePassword extends Fragment {



    private EditText  mEdtEnterPass, mEdtConfirmPass;
    private Button  mBtnUpdatePassword;

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    String email_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password_frag, null);

        openHelper = new SQLiteDBHelper(getContext());
        db = openHelper.getWritableDatabase();

        mEdtEnterPass = (EditText) view.findViewById(R.id.edtEnterPass);
        mEdtConfirmPass = (EditText) view.findViewById(R.id.edtConfirmPass);
        mBtnUpdatePassword = (Button) view.findViewById(R.id.btnUpdatePass);

        Bundle bundle = getArguments();

        //Getting email id from previous fragment
       email_id = bundle.getString("email_id");

        mBtnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          //Validations
                if (mEdtEnterPass.getText().toString().equals("") || mEdtConfirmPass.getText().toString().equals("")) {

                    Toast.makeText(getContext(), "Enter the password first", Toast.LENGTH_SHORT).show();

                } else {

                    if (mEdtEnterPass.getText().toString().equals(mEdtConfirmPass.getText().toString())) {

                        //Sqlite database update query.....given below
                        //Updating password with the help of email id passed from LoginActivity->forgotpassword->Changepassword
                        updatePassword(email_id, mEdtConfirmPass.getText().toString());

                        Toast.makeText(getContext(), "Password Changed sucessfully", Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

                    } else {

                        Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });


        return view;
    }

    public void updatePassword(String email, String password) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        db.update(SQLiteDBHelper.TABLE_NAME, contentValues, "email=?", new String[]{email});
    }
}
