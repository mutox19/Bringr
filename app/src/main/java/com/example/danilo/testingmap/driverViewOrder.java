package com.example.danilo.testingmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class driverViewOrder extends AppCompatActivity {

    String orderId, driverId;
    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;
    Button btnBack, btnProccess, btnCall;
    EditText customername, customeremail, placeToShop, whatCustomerWants, custAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        if (intent.getStringExtra("order") != null) {
            orderId = intent.getStringExtra("order");
        }
        if (intent.getStringExtra("driver") != null) {
            driverId = intent.getStringExtra("driver");
        }


        customername = (EditText)findViewById(R.id.editTxtName);
        customeremail = (EditText)findViewById(R.id.editTxtEmail);
        placeToShop = (EditText)findViewById(R.id.editTxtShopAt);
        whatCustomerWants = (EditText)findViewById(R.id.editTxtWhatYou);
        custAdd = (EditText)findViewById(R.id.editcustAddress);

        btnBack = (Button) findViewById(R.id.btnGoPrevious);
        btnCall = (Button) findViewById(R.id.btnCallCustomer);
        btnProccess = (Button) findViewById(R.id.btnAcceptJob);

        btnBack.setOnClickListener(MyListener);
        btnCall.setOnClickListener(MyListener);
        btnProccess.setOnClickListener(MyListener);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //CreatePlatformEndpointResult result = null;
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);



        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //search the database for the order so you can display the order fpr the driver

                Intent intent = getIntent();

                final String order = intent.getStringExtra("order");

                final ordersDB orderToFind = mapper.load(ordersDB.class, order);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        customername.setText(orderToFind.getCustomerName());
                        customeremail.setText(orderToFind.getEmail());
                        placeToShop.setText(orderToFind.getPlaceToGo());
                        whatCustomerWants.setText(orderToFind.getWhatTheyWant());
                        custAdd.setText(orderToFind.getFullAddress());


                        customername.setEnabled(false);
                        customeremail.setEnabled(false);
                        placeToShop.setEnabled(false);
                        whatCustomerWants.setEnabled(false);
                        custAdd.setEnabled(false);
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom());
                        //Toast.makeText(getApplicationContext(), "Calling Customer", Toast.LENGTH_LONG).show();
                    }
                });
            }

        };

        //mainHandler.post(runnable);
        Thread thread = new Thread(runnable);
        thread.start();

    }


    private View.OnClickListener MyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderFinal(v);

        }
    };

    private void OrderFinal(View v) {
        try {
            if (btnBack.isPressed()) {

                //return driver back to the viewer map
                Intent intent = new Intent(getApplicationContext(), viewer.class);

                //pass driver credentials and order credentials
                intent.putExtra("order", orderId);
                intent.putExtra("driver", driverId);
                startActivity(intent);
                finish();
            }
            if (btnCall.isPressed()) {
                //this button will call the number that is stored with the order that we find
                //double longit = 0.0;
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        //search the database for the order so you can display the order fpr the driver
                        //ordersDB orderToFind = new ordersDB();
                        Intent newin = new Intent(getApplicationContext(), viewer.class);

                        Intent intent = getIntent();
                        //orderToFind.setOrderId(intent.getStringExtra("order"));
                        String order = intent.getStringExtra("order");
                        String customer = intent.getStringExtra("customer");
                        Log.d("QUERY6", "run: " + customer);

                        final Customers customerToFind = mapper.load(Customers.class, customer);

                        final String customerPhone = customerToFind.getPhoneNum();

                        //ordersList.setText(item.getPostalCode());
                        //getResults(item.getPostalCode(), item.getCustomerName());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                String mobileNo = customerPhone;
                                String uri = "tel:" + mobileNo.trim();
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse(uri));
                                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom());
                                //Toast.makeText(getApplicationContext(), "Calling Customer", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                };

                //mainHandler.post(runnable);
                Thread thread = new Thread(runnable);
                thread.start();
            }
            if (btnProccess.isPressed())
            {
                // go to the driver complete delivery activity.
                //pass the order id, driverid, customer id
                Intent intent = getIntent();
                String customerId = intent.getStringExtra("customer");
                Intent process = new Intent(getApplicationContext(), driverCompleteDelivery.class);
                process.putExtra("orderID", orderId);
                process.putExtra("driverID", driverId);
                process.putExtra("customerID",customerId);

                startActivity(process);
            }
        } catch (Exception e) {
            Log.d("DriverViewOrder", "OrderFinal: " + e.getMessage());
        }
    }

}
