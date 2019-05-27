package georeach.smartwomensecurity.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import georeach.smartwomensecurity.Modal.ModalContact;

/**
 * Created by Aaleen on 6/2/2017.
 */

public class DbHelper extends SQLiteOpenHelper {    SQLiteDatabase db;


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("Database: ", "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table Contacts(Name text, Mobile text)");
        db.execSQL("Create table GpsContacts(Name text, Mobile text)");
        db.execSQL("Create table EmergencyContact(Name text, Mobile text)");

        Log.d("Table: ", "Tables Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public ArrayList<ModalContact> getAllContacts() {
        ArrayList<ModalContact> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] column = new String[]{"Name",  "Mobile"};
        Cursor cursor = db.query("Contacts", column, null, null, null, null, null);
        list.clear();
        while (cursor.moveToNext()) {

            ModalContact modalContact = new ModalContact(cursor.getString(0), cursor.getString(1));
           // String data = cursor.getString(0) + ":" + cursor.getString(1);
            list.add(modalContact);
        }
        return list;
    }

    public boolean AddContact(ModalContact modalContact){
      SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        long rowname =  db.insert("Contacts", null, values);

        if (rowname>0){

            Log.d("Data Inserted:", rowname+"");

        }

        return true;
    }


    public int deleteContact(ModalContact modalContact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        int deleteCnt = db.delete("Contacts", "Name=? and Mobile=?", new String[]{modalContact.getName() , modalContact.getMobile()});
        if (deleteCnt>0){
            Log.d("Delete count:", deleteCnt+"");
        }
        return deleteCnt;
    }




    //INSERT DELETE OPERATIONS FOR GPS ACTIVITY TABLE

    public ArrayList<ModalContact> getAllGpsContacts() {
        ArrayList<ModalContact> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] column = new String[]{"Name",  "Mobile"};
        Cursor cursor = db.query("GpsContacts", column, null, null, null, null, null);
        list.clear();
        while (cursor.moveToNext()) {

            ModalContact modalContact = new ModalContact(cursor.getString(0), cursor.getString(1));
            // String data = cursor.getString(0) + ":" + cursor.getString(1);
            list.add(modalContact);
        }
        return list;
    }

    public boolean AddGpsContact(ModalContact modalContact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        long rowname =  db.insert("GpsContacts", null, values);

        if (rowname>0){

            Log.d("Data Inserted:", rowname+"");

        }

        return true;
    }


    public int deleteGpsContact(ModalContact modalContact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        int deleteCnt = db.delete("GpsContacts", "Name=? and Mobile=?", new String[]{modalContact.getName() , modalContact.getMobile()});
        if (deleteCnt>0){
            Log.d("Delete count:", deleteCnt+"");
        }
        return deleteCnt;
    }




    //INSERT DELETE OPERATIONS FOR EMERGENCY CALL ACTIVITY TABLE

    public ArrayList<ModalContact> getAllEmergecyContacts() {
        ArrayList<ModalContact> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] column = new String[]{"Name",  "Mobile"};
        Cursor cursor = db.query("EmergencyContact", column, null, null, null, null, null);
        list.clear();
        while (cursor.moveToNext()) {

            ModalContact modalContact = new ModalContact(cursor.getString(0), cursor.getString(1));
            // String data = cursor.getString(0) + ":" + cursor.getString(1);
            list.add(modalContact);
        }
        return list;
    }

    public boolean addEmergencyContact(ModalContact modalContact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        long rowname =  db.insert("EmergencyContact", null, values);

        if (rowname>0){

            Log.d("Data Inserted:", rowname+"");

        }

        return true;
    }


    public int deleteEmergencyContact(ModalContact modalContact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", modalContact.getName());
        values.put("Mobile", modalContact.getMobile());

        int deleteCnt = db.delete("EmergencyContact", "Name=? and Mobile=?", new String[]{modalContact.getName() , modalContact.getMobile()});
        if (deleteCnt>0){
            Log.d("Delete count:", deleteCnt+"");
        }
        return deleteCnt;
    }



}

