package com.example.danilo.testingmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Danilo on 2016-04-27.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "foodOrder";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "CustomerName";
    public static final String COL_3 = "Email";
    public static final String COL_4 = "PlaceToGo";
    public static final String COL_5 = "City";
    public static final String COL_6 = "PostalCode";
    public static final String COL_7 = "Country";
    public static final String COL_8 = "FullAddress";
    public static final String COL_9 = "WhatTheyWant";


    public static final String CREATE_ORDER_TABLE = "CREATE TABLE " +
            TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_2 + " TEXT NOT NULL, "
            + COL_3 + " TEXT, "
            + COL_4 + " TEXT NOT NULL, "
            + COL_5 + " TEXT NOT NULL, "
            + COL_6 + " TEXT NOT NULL, "
            + COL_7 + " TEXT NOT NULL, "
            + COL_8 + " TEXT NOT NULL, "
            + COL_9 + " TEXT NOT NULL);";
    public static final String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 8);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try
        {
            db.execSQL(CREATE_ORDER_TABLE);
            Log.d("Database", "Order table CREATED!!");
        }
        catch(Exception e)
        {
            Log.d("Database Fail", "table not created " + e.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            db.execSQL(DROP_ORDER_TABLE);
            onCreate(db);
        }
        catch(Exception e)
        {
            Log.d("Database Delete", "onUpgrade: " + e.getMessage());
        }
    }

    public Cursor GetAllMediumInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, COL_5 + " = 'Kitchener' ", null, null, null,
                COL_2 + " DESC");
        return res;
    }


    public Cursor GetOrders(String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query(TABLE_NAME, null, COL_5 + "= '" +  city + "'" , null, null, null,
                COL_1 + " DESC");

        return res;
    }
    public void InsertNewOrder(String name, String email, String placeToGo, String city,
                               String postalCode, String country, String address, String whatTheyWant)
    {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2,name);
            contentValues.put(COL_3,email);
            contentValues.put(COL_4,placeToGo);
            contentValues.put(COL_5,city);
            contentValues.put(COL_6,postalCode);
            contentValues.put(COL_7,country);
            contentValues.put(COL_8,address);
            contentValues.put(COL_9, whatTheyWant);
            db.insert(TABLE_NAME, null, contentValues);
            //long rowInserted = db.insert(TABLE_NAME, null, contentValues);
            Log.d("Customer Name", "Inserted Customer name: " + name);
            /*
            if(rowInserted != -1)
            {
                //send the notification to owners from here cause new order has been inserted
                Intent intent = new Intent();
                PendingIntent pItent = PendingIntent.getActivities(this,0, intent, 0);
                Notification noti = new Notification.Builder(this)
                .setTicker("Ticker Title")   //ticker title
                .setContentTitle("Notification Content")
                 .setContentText("this is testing")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pItent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,noti);
            }*/
        }
        catch (Exception e)
        {
            Log.d("Order insert", "Insert Order not inserted: " + name + e.getMessage());
        }

    }
}
