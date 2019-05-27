package georeach.smartwomensecurity.Fragments;

import android.content.Context;
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

/**
 * Created by Aaleen on 6/9/2017.
 */

public class CustomMessage extends Fragment {

    private EditText mEdtCustomMessage;
    private Button mBtnSaveCustomMessage;
    private String customTextMessage;
    SendCustomMessage sendCustomMessage;

    public interface SendCustomMessage{
        public void setCustomText(String s);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendCustomMessage = (SendCustomMessage) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_message_fragment, null);

        mEdtCustomMessage = (EditText) view.findViewById(R.id.edtCusomMessage);
        mBtnSaveCustomMessage = (Button) view.findViewById(R.id.btnSaveCustomMessage);

        Bundle bundle = getArguments();
        customTextMessage = bundle.getString("default");

        mEdtCustomMessage.setText(customTextMessage);

        mBtnSaveCustomMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEdtCustomMessage.getText().toString().equals("")){

                  customTextMessage =   mEdtCustomMessage.getText().toString();

                    sendCustomMessage.setCustomText(customTextMessage);

                    getActivity().getSupportFragmentManager().popBackStack();

                }

                else{
                    Toast.makeText(getContext(), "Please enter some message to send", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
