package georeach.smartwomensecurity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.Modal.ModalEmergency;
import georeach.smartwomensecurity.R;

/**
 * Created by Aaleen on 6/7/2017.
 */

public class EmergencyContactsAdapter extends BaseAdapter {

    private Context context;
    private String[] arrContact;
    private String[] arrNumber;

    public EmergencyContactsAdapter(Context context, String[] arrContact, String[] arrNumber) {
        this.context = context;
        this.arrContact = arrContact;
        this.arrNumber = arrNumber;
    }

    @Override
    public int getCount() {
        return arrContact.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_view_emergencylist, null);
        }

        TextView mTxtContact = (TextView) convertView.findViewById(R.id.txtContact);
        TextView mTxtContactNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);

        ModalEmergency modalEmergency = new ModalEmergency(arrContact[position], arrNumber[position]);

        mTxtContact.setText(modalEmergency.getContactName());
        mTxtContactNumber.setText(modalEmergency.getContactNumber());



        return convertView;
    }
}
