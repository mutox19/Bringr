package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class DriverRegistered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registered);



            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap user = new HashMap();
            user = db.getUserDetails();

            /**
             * Displays the registration details in Text view
             **/

            //final TextView fname = (TextView)findViewById(R.id.fname);
            //final TextView lname = (TextView)findViewById(R.id.lname);
            //final TextView uname = (TextView)findViewById(R.id.uname);
            //final TextView email = (TextView)findViewById(R.id.email);
            //final TextView created_at = (TextView)findViewById(R.id.regat);
            //fname.setText(user.get("fname"));
            //lname.setText(user.get("lname"));
            //uname.setText(user.get("uname"));
            //email.setText(user.get("email"));
            //created_at.setText(user.get("created_at"));

            //Button login = (Button) findViewById(R.id.login);
            //login.setOnClickListener(MyListener);

        }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onReg(v);
        }
    };

    public void onReg(View v)
    {

    }
}
