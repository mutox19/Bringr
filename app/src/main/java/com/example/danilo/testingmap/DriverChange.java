package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DriverChange extends AppCompatActivity {

    Button btnCustomerMain, btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btnCustomerMain = (Button)findViewById(R.id.btnCustMain);
        btnExit = (Button)findViewById(R.id.btnExit);


        btnCustomerMain.setOnClickListener(MyListener);
        btnExit.setOnClickListener(MyListener);
    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            orderFormBtns(v);
        }
    };

    private void orderFormBtns(View v)
    {
        if(btnCustomerMain.isPressed())
        {
            //redirect customer to main page
            Intent intentCustomerMain = new Intent(getApplicationContext(),customerMain.class);
            startActivity(intentCustomerMain);

        }
        if(btnExit.isPressed())
        {
            //close the app
            finish();
            System.exit(0);

        }
    }

}
