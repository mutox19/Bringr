package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class customerCompleteOptions extends AppCompatActivity {

    Button rateDriver, reportDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complete_options);

        rateDriver = (Button)findViewById(R.id.btnMainMenu);
        reportDriver = (Button)findViewById(R.id.btnExitOut);

        Intent getIntent = getIntent();

        final String orderId = getIntent.getStringExtra("order");
        final String driverId = getIntent.getStringExtra("driver");
        final String customerId = getIntent.getStringExtra("customer");

        rateDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rateIntent = new Intent(getApplicationContext(), ratings.class);

                rateIntent.putExtra("order",orderId);
                rateIntent.putExtra("driver",driverId);
                rateIntent.putExtra("customer",customerId);

                startActivity(rateIntent);
            }
        });

        reportDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reportIntent = new Intent(getApplicationContext(),reportDriver.class);

                reportIntent.putExtra("order", orderId);
                reportIntent.putExtra("driver",driverId);
                reportIntent.putExtra("customer",customerId);
                startActivity(reportIntent);
            }
        });

    }

}
