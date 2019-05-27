package georeach.smartwomensecurity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import georeach.smartwomensecurity.Modal.ModalContact;
import georeach.smartwomensecurity.R;

/**
 * Created by Aaleen on 6/3/2017.
 */

public class CustomContactAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ModalContact> mContactList;

    public CustomContactAdapter(Context mContext, ArrayList<ModalContact> mContactList) {
        this.mContext = mContext;
        this.mContactList = mContactList;
    }

    @Override
    public int getCount() {
        return mContactList.size();
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
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.custom_view_contactlist, null);
        }

        TextView mTxtCustomName = (TextView) convertView.findViewById(R.id.txtCustomName);
        TextView mTxtCustomMobile = (TextView) convertView.findViewById(R.id.txtCustomMobile);

        ModalContact modalContact = mContactList.get(position);

        mTxtCustomName.setText(modalContact.getName());
        mTxtCustomMobile.setText(modalContact.getMobile());



        return convertView;
    }
}
