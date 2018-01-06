package com.example.danilo.testingmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.firebase.iid.FirebaseInstanceId;

public class finalizeOrder extends AppCompatActivity {

    Button btnChangeDriver, btnConfirm, btnDeleteOrder;

    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_order);


        btnChangeDriver = (Button)findViewById(R.id.btnChangeDriver);
        btnConfirm = (Button)findViewById(R.id.btnConfirmDriver);
        btnDeleteOrder = (Button)findViewById(R.id.btnDeleteOrder);

        btnChangeDriver.setOnClickListener(MyListener);
        btnConfirm.setOnClickListener(MyListener);
        btnDeleteOrder.setOnClickListener(MyListener);


        Intent intentGet = getIntent();
        String orderi = intentGet.getStringExtra("order");

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //CreatePlatformEndpointResult result = null;
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);
        //String driveri = intentGet.getStringExtra("driver");

        //Toast.makeText(getApplicationContext(),orderi,Toast.LENGTH_LONG).show();
        Log.d("FINALIZEORDER", "onCreate: " + orderi);

    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            orderFormBtns(v);
        }
    };

    public void open(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to change driver?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                //dialog.dismiss();
                try
                {
                    //Toast.makeText(finalizeOrder.this, "You Clicked yes", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(finalizeOrder.this, DriverChange.class);
                    finalizeOrder.this.startActivity(i);


                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            final Intent intentFind = getIntent();
                            String driver = "";

                            String queryString = intentFind.getStringExtra("order");
                            Log.d("QUERY6", "run: " + queryString);

                            if(intentFind.getStringExtra("driver") != null)
                            {
                                driver = intentFind.getStringExtra("driver");
                            }
                            //load specific item from database
                            final ordersDB item = mapper.load(ordersDB.class, queryString);

                            //if the is an item in the database then change the status back to available
                            //set the driver to empty string and save the new item back to the database
                            if(item != null)
                            {
                                item.setStatus("available");
                                item.setDriverID("");
                                mapper.save(item);
                            }

                            String orderID = intentFind.getStringExtra("order");
                            String customer = intentFind.getStringExtra("customer");
                            Log.d("ORDERCHANGE", "run: " + driver + " " + orderID);
                            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());

                            backgroundWorker.doInBackground("customerChangeDriver",driver,"Customer Change Driver","The customer has decided not to move forward with you as the delivery driver", customer);

                            //need an intent to move the customer to the specific page
                        }
                    };

                    Thread thread = new Thread(runnable);
                    thread.start();


                }
                catch(Exception e)
                {

                    Log.d("YES ERROR", "onClick: " + e.getMessage());
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessage(String title, String Message, int icon) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(Message);
        alertDialog.setIcon(icon);
        alertDialog.show();
    }
    private void orderFormBtns(View v)
    {
        if(btnChangeDriver.isPressed())
        {
            //just have a dialog box pop up saying if there sure they want to change driver
            // if yes then do next step if no do nothing
            open(v);
            //notify the driver that the customer does not want them as a driver
            //change order status to open then redirectt customer to main

        }
        if(btnConfirm.isPressed())
        {
            //notify driver that the customer has confirm and let customer know
            // they will get notified when they can
            //watch the driver on map
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    final Intent intentFind = getIntent();
                    String driver = "";

                    if(intentFind.getStringExtra("driver") != null)
                    {
                        driver = intentFind.getStringExtra("driver");
                    }

                    String orderID = intentFind.getStringExtra("order");
                    String customer = intentFind.getStringExtra("customer");
                    Log.d("ORDERDRIVER", "run: " + driver + " " + customer);
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());

                    backgroundWorker.doInBackground("customerAcceptDriver",driver,"Customer Acceptance","The customer has confirmed you has there delivery driver", orderID, customer);

                    //send customer to specific page now
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            //Intent intentConfirm = new Intent(getApplicationContext(),CustomerConDriver.class);
            //startActivity(intentConfirm);
            /*Intent intentSignUP=new Intent(getApplicationContext(),finalizeOrder.class);
            //startActivity(intentSignUP);
            PendingIntent pItent = PendingIntent.getActivity(this,0, intentSignUP, 0);
            Notification noti = new Notification.Builder(this)
                    .setTicker("Ticker Title")   //ticker title
                    .setContentTitle("Food Delivering")
                    .setContentText("Customer order ")
                    .setSmallIcon(R.mipmap.yellowtaxi)
                    .setContentIntent(pItent).getNotification();
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, noti);
            */
        }

        if(btnDeleteOrder.isPressed())
        {

            //notify driver that the customer has decided to cancel the order and driver will be redirected
            //delete order completly from database
            //send customer to delete success screen
            //Intent intentDeleteDriver = new Intent(getApplicationContext(),CustomerDelOrder.class);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    final Intent intentFind = getIntent();
                    String driver = "";

                    String queryString = intentFind.getStringExtra("order");
                    Log.d("QUERY6", "run: " + queryString);

                    if(intentFind.getStringExtra("driver") != null)
                    {
                        driver = intentFind.getStringExtra("driver");
                    }
                    //load specific item from database
                    final ordersDB item = mapper.load(ordersDB.class, queryString);

                    //delete the item if there is an item
                    if(item != null)
                    {
                        mapper.delete(item);
                    }

                    String orderID = intentFind.getStringExtra("order");
                    Log.d("DeleteORDER", "run: " + driver + " " + orderID);
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());

                    backgroundWorker.doInBackground("customerDeleteOrder",driver,"Customer Deleted Order","The customer has deleted there order");

                }
            };

            Thread thread = new Thread(runnable);
            thread.start();

            //send customer to specific page
        }
    }

}
