package com.example.danilo.testingmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DriverMain extends AppCompatActivity {

    DatabaseHandler loginDataBaseAdapter;
    Button btnLogout, btnStartWork;
    EditText txtInfo, firstname, lastName, email;
    GPSTracker gps;
    Button changepas;
    Intent startWorkIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Called when the activity is first created.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);


        Intent driverInfoIntent = getIntent();
        btnStartWork = (Button) findViewById(R.id.btnStartWork);
        txtInfo = (EditText) findViewById(R.id.txtInfo);
        firstname = (EditText) findViewById(R.id.fname);
        lastName = (EditText) findViewById(R.id.lname2);
        email = (EditText) findViewById(R.id.email);
        btnLogout =  (Button)findViewById(R.id.logout);


        firstname.setText(driverInfoIntent.getStringExtra("firstname"));
        lastName.setText(driverInfoIntent.getStringExtra("lastname"));
        email.setText(driverInfoIntent.getStringExtra("email"));

        firstname.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        btnStartWork.setOnClickListener(MyListener);
        btnLogout.setOnClickListener(MyListener);


        //maybe check to see if the user is actually logged in by havig a shared preferences for when a user is logged in

        /*
        gps = new GPSTracker(DriverMain.this);
        if(gps.canGetLocation())
        {
            Geocoder geocoder;
            List<Address> addresses;
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                //String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName();
                //Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();
                //startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);

                //startWorkIntent.putExtra("firstname",intentGet.getStringExtra("firstname"));
                //startWorkIntent.putExtra("city",city);
                //startWorkIntent.putExtra("state",state);
                //startActivity(startWorkIntent);
                //finish();

            }
            catch (Exception e) {

                Log.d("cannot Start", "StartWork: " + e.getMessage());;
            }


        }else
        {
            gps.showSettingsAlert();
        }

*/


        /*
        loginDataBaseAdapter = new DatabaseHandler(this);
        try
        {
            Intent intent = getIntent();
            String userEmail = intent.getStringExtra("currentEmail");
            HashMap currentLogged = loginDataBaseAdapter.getSingleUserInfo(userEmail);

            txtInfo.setText("FirstName: " + currentLogged.get("fname") + "\n" + "LastName: " + currentLogged.get("lname") + "\n"
                    + "email: " + currentLogged.get("email") + "\n " + "Username: " + currentLogged.get("uname"));

        }catch(Exception e)
        {
            Log.d("bad load", "onCreate: " + e.getMessage());
        }*/

    }
/*
    private void setListAdapter(Student[] students)
    {
        TestListAdapter adapter = new TestListAdapter(prepareListViewItems(students))
        list.setAdapter(adapter);
    }*/
    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //UserLogOut(v);
            StartWork(v);
        }
    };

    private void UserLogOut(View v)
    {
        if(btnLogout.isPressed())
        {
            //loginDataBaseAdapter.logoutUser(this);
            Intent logOutIntent = new Intent(getApplicationContext(), login.class);
            startActivity(logOutIntent);
            finish();
        }

    }

    private void StartWork(View v)
    {
        if(btnStartWork.isPressed())
        {
            startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
            //startActivity(startWorkIntent);
            //loginDataBaseAdapter.logoutUser(this);
            gps = new GPSTracker(DriverMain.this);
            if(gps.canGetLocation())
            {
                Geocoder geocoder;
                List<Address> addresses;
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                geocoder = new Geocoder(this, Locale.getDefault());

                Intent intentGet = getIntent();

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    //String country = addresses.get(0).getCountryName();
                    //String postalCode = addresses.get(0).getPostalCode();
                    //String knownName = addresses.get(0).getFeatureName();
                    //Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();
                    startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
                    startWorkIntent.putExtra("firstname",intentGet.getStringExtra("firstname"));
                    startWorkIntent.putExtra("city",city);
                    startWorkIntent.putExtra("state",state);
                    startActivity(startWorkIntent);
                    //finish();

                }
                catch (Exception e) {

                    Log.d("cannot Start", "StartWork: " + e.getMessage());;
                }

            }else
            {
                gps.showSettingsAlert();
            }

        }
        if(btnLogout.isPressed())
        {
            //loginDataBaseAdapter.logoutUser(this);
            Intent logOutIntent = new Intent(getApplicationContext(), login.class);
            startActivity(logOutIntent);
            finish();
        }

    }
}
