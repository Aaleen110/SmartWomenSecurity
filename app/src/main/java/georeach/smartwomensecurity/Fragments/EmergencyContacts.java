package georeach.smartwomensecurity.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import georeach.smartwomensecurity.Adapter.EmergencyContactsAdapter;
import georeach.smartwomensecurity.R;

/**
 * Created by Aaleen on 6/7/2017.
 */

public class EmergencyContacts extends Fragment {

    private ListView mListViewEm;
    private EmergencyContactsAdapter mAdapter;
    private String[] contactsNameArr = {"Police", "Ambulance", "Fire", "Womens Helpline", "Child abuse hotline", "Disaster Management"};
    private String[] contactsNumberArr = {"100", "102", "101", "181", "1098", "108"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.emergency_contacts_help, null);

        mListViewEm = (ListView) v.findViewById(R.id.listViewEm);
        mAdapter = new EmergencyContactsAdapter(getContext(), contactsNameArr, contactsNumberArr);
        mListViewEm.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mListViewEm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              String call =  contactsNumberArr[position].toString();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + call));
                startActivity(callIntent);


             }
        });
        return v;
    }



}
