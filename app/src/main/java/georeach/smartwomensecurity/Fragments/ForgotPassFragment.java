package georeach.smartwomensecurity.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import georeach.smartwomensecurity.R;


public class ForgotPassFragment extends Fragment {

    private EditText mEdtOtp;
    private Button mBtnSendOtp;
    int otp;
    String email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forgot_password_frag, null);

                mEdtOtp = (EditText) view.findViewById(R.id.edtOtp);
                mBtnSendOtp = (Button) view.findViewById(R.id.btnSendOtp);

        final Bundle bundle = getArguments();

        //getting OTP here to validate it with the OTP user will input
        otp =  bundle.getInt("otp");

        //getting email id from previous activity to apply logic to change password
        email = bundle.getString("email");


        mBtnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Validations
               if (mEdtOtp.getText().toString().equals("")){
                   Toast.makeText(getContext(), "Enter Otp", Toast.LENGTH_SHORT).show();
               }

               //Checking if OTP matches with the OTP sent
               else if(mEdtOtp.getText().toString().equals(otp+"")){
                   Toast.makeText(getContext(), "Otp matched", Toast.LENGTH_SHORT).show();


                   ChangePassword changePassword = new ChangePassword();
                   FragmentManager fm = getActivity().getSupportFragmentManager();
                   FragmentTransaction ft = fm.beginTransaction();
                   Bundle bundle1 = new Bundle();

                   //Bundling the emial id of user to CHangePassword fragment in order to apply logic to change password
                   bundle1.putString("email_id", email);
                   changePassword.setArguments(bundle1);
                   ft.add(R.id.containerForgot, changePassword);
                   ft.addToBackStack("forgot");
                   ft.commit();
                }
                else
               {
                   Toast.makeText(getContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
               }

            }
        });

        return view;
    }


    }
