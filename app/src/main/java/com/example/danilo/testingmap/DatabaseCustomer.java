package com.example.danilo.testingmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Console;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Danilo on 2016-07-13.
 *
 *
 */


public class DatabaseCustomer extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "customer.db";

    // Login table name
    private static final String TABLE_LOGIN = "CustomerLogin";

    // Login Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_FIRSTNAME = "fname";
    public static final String KEY_LASTNAME = "lname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "uname";
    public static final String KEY_UID = "uid";
    public static final String KEY_PID = "pID";
    public static final String KEY_CREATED_AT = "created_at";



    public DatabaseCustomer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_PID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        Log.d("error", "onUpgrade: " + oldVersion + " " + newVersion);
        // Create tables again
        onCreate(db);

    }

    /**
     * Getting user data from database
     * */
    public HashMap getUserDetails(){
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("uid", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    public HashMap getSingleUserInfo(String userEmail) {

        HashMap user = new HashMap();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN, null, " email=?", new String[]{userEmail}, null, null, null);
        cursor.moveToFirst();
        //String password = cursor.getString(cursor.getColumnIndex("uid"));
        String firstName = cursor.getString(cursor.getColumnIndex("fname"));
        String lastName = cursor.getString(cursor.getColumnIndex("lname"));
        String emailAdd = cursor.getString(cursor.getColumnIndex("email"));
        String userName = cursor.getString(cursor.getColumnIndex("uname"));

        user.put("fname", firstName);
        user.put("lname", lastName);
        user.put("email", emailAdd);
        user.put("uname", userName);

        cursor.close();
        db.close();
        return user;
    }

    public String getSingleEntry(String userEmail)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=db.query(TABLE_LOGIN, null, " email=?", new String[]{userEmail}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("uid"));
        cursor.close();
        return password;


    }

    //insert driver
    public void insertEntry(String firstName, String lastName,String userEmail, String userName,String password, String User)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues newValues = new ContentValues();

            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());

            // Assign values for each row.
            newValues.put("fname", firstName);
            newValues.put("lname", lastName);
            newValues.put("email", userEmail);
            newValues.put("uname", userName);
            newValues.put("uid", password);
            newValues.put("pID", User);
            newValues.put("created_at", date);

            // Insert the row into your table
            db.insert(TABLE_LOGIN, null, newValues);
            //Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.d("InsertFail", "Driver Insert: " + e.getMessage());
        }

    }

    public HashMap getCustomerpID(String userEmail) {

        HashMap user = new HashMap();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN, null, " email=?", new String[]{userEmail}, null, null, null);
        cursor.moveToFirst();
        //String password = cursor.getString(cursor.getColumnIndex("uid"));
        String firstName = cursor.getString(cursor.getColumnIndex("fname"));
        String lastName = cursor.getString(cursor.getColumnIndex("lname"));
        String emailAdd = cursor.getString(cursor.getColumnIndex("email"));
        String pID = cursor.getString(cursor.getColumnIndex("pID"));

        //user.put("fname", firstName);
        //user.put("lname", lastName);
        //user.put("email", emailAdd);
        user.put("pID", pID);

        cursor.close();
        db.close();
        return user;
    }

}
