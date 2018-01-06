package com.example.danilo.testingmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Danilo on 2016-04-28.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login.db";

    // Login table name
    private static final String TABLE_LOGIN = "LOGIN";

    // Login Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_FIRSTNAME = "fname";
    public static final String KEY_LASTNAME = "lname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "uname";
    public static final String KEY_UID = "uid";
    public static final String KEY_DID = "did";
    public static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, DATABASE_NAME, null, 4);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_DID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String uname, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, fname); // FirstName
        values.put(KEY_LASTNAME, lname); // LastName
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, uname); // UserName
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
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


    public int deleteEntry(String Email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //String id=String.valueOf(ID);
        String where="email=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{Email}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }



    //this is if you want to update specific user
    public void  updateEntry(String firstName, String lastName, String userEmail, String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
       /* DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());*/

        // Assign values for each row.
        updatedValues.put("fname", firstName);
        updatedValues.put("lname", lastName);
        updatedValues.put("email", userEmail);
        updatedValues.put("uname", username);
        updatedValues.put("uid", password);
        //updatedValues.put("created_at", date);

        String where="email = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userEmail});
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    /**
     * Re create database
     * Delete all tables and create them again
     * */
    private void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);

        //onUpgrade(db,DATABASE_VERSION,1);
        db.close();
    }

    public HashMap getSingleUserInfo(String userEmail) {

        HashMap user = new HashMap();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("LOGIN", null, " email=?", new String[]{userEmail}, null, null, null);
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
            Cursor cursor=db.query("LOGIN", null, " email=?", new String[]{userEmail}, null, null, null);
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
    public void insertEntry(String firstName, String lastName,String userEmail, String userName,String password, String did)
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
            newValues.put("did", did);
            newValues.put("created_at", date);

            // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        //Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.d("InsertFail", "Driver Insert: " + e.getMessage());
        }

    }



}
