package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class driverCompleteDelivery extends AppCompatActivity {

    String customerID, orderID, driverID;
    EditText orderTotal;
    Button completeOrder;
    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;
    String foundCustFID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_complete_delivery);



        //get the order id intent and
        // take the value of the order total and update the order
        //send the notification to the customer so the customer can
        // now pay for the order or report the driver
        //redirect driver to driver main

        Intent intent = getIntent();

        if (intent.getStringExtra("orderID") != null) {
            orderID = intent.getStringExtra("orderID");
        }
        if (intent.getStringExtra("driverID") != null) {
            driverID = intent.getStringExtra("driverID");
        }
        if (intent.getStringExtra("customerID") != null) {
            customerID = intent.getStringExtra("customerID");
        }

        orderTotal = (EditText)findViewById(R.id.editTxtOrderTotal);
        completeOrder = (Button)findViewById(R.id.orderRec);


        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //CreatePlatformEndpointResult result = null;
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);


        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this is where u find the order with the order id and then you update the order total
                // to the total that is in the order total
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        //search the database for the order so you can display the order fpr the driver

                        Intent intent = getIntent();

                        final String order = intent.getStringExtra("orderID");
                        final String cust = intent.getStringExtra("customerID");
                        final String drive = intent.getStringExtra("driverID");

                        final Customers customerToFind = mapper.load(Customers.class, cust);

                        String foundCustFID2 = customerToFind.getFID();

                        final ordersDB orderToFind = mapper.load(ordersDB.class, order);

                        String OrderTotal = orderTotal.getText().toString();
                        orderToFind.setTotal(OrderTotal);
                        mapper.save(orderToFind);

                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                        backgroundWorker.doInBackground("ProcessTotal", drive, cust, order,foundCustFID2, "Order Total has been set", "You can now pay for your order" );
                    }

                };


                Thread thread = new Thread(runnable);
                thread.start();

                // send notification to customer

                //now redirect the driver to driver main

                Intent redirect = new Intent(getApplicationContext(), DriverMain.class );
                startActivity(redirect);
            }
        });

    }

}
