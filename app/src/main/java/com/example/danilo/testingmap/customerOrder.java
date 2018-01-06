package com.example.danilo.testingmap;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Locale;

public class customerOrder extends AppCompatActivity {

    EditText name, address, placetoShop, custemail, order;
    Button btnBack, btnAccept;
    FirebaseHelper firebaseHelper;
    String custFID;
    GPSTracker gps;
    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);

        name = (EditText)findViewById(R.id.editTxtName);
        address = (EditText)findViewById(R.id.editcustAddress);
        placetoShop = (EditText)findViewById(R.id.editTxtShopAt);
        custemail = (EditText)findViewById(R.id.editTxtEmail);
        order = (EditText)findViewById(R.id.editTxtWhatYou);

        name.setEnabled(false);
        address.setEnabled(false);
        placetoShop.setEnabled(false);
        custemail.setEnabled(false);
        order.setEnabled(false);

        btnBack = (Button)findViewById(R.id.btnGoPrevious);
        btnAccept = (Button)findViewById(R.id.btnAcceptJob);

        btnBack.setOnClickListener(MyListener);
        btnAccept.setOnClickListener(MyListener);
        Intent intent = getIntent();


        //load amazon credentials for app
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);

        //get all the customer order details
        if(intent.getStringExtra("customerFirebase") != null)
        {
            custFID = intent.getStringExtra("customerFirebase");
        }
        if(intent.getStringExtra("customerAdd") != null)
        {
            address.setText(intent.getStringExtra("customerAdd"));
        }
        if (intent.getStringExtra("customerName") != null)
        {
            name.setText(intent.getStringExtra("customerName"));
        }
        if (intent.getStringExtra("customerEmail") != null)
        {
            custemail.setText(intent.getStringExtra("customerEmail"));
        }
        if (intent.getStringExtra("customerPlaceToShop") != null)
        {
            placetoShop.setText(intent.getStringExtra("customerPlaceToShop"));
        }
        if(intent.getStringExtra("customerOrder")!= null)
        {
            order.setText(intent.getStringExtra("customerOrder"));
        }
    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            WorksAvailable(v);
        }
    };

    private void WorksAvailable(View v)
    {
        if(btnBack.isPressed())
        {
            Intent btnBack = new Intent(getApplicationContext(), worksAvailable.class);

            gps = new GPSTracker(customerOrder.this);
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
                    //Intent startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
                    btnBack.putExtra("state",state);
                    btnBack.putExtra("city",city);
                    startActivity(btnBack);
                    finish();

                }
                catch (Exception e) {

                    Log.d("cannot Start", "StartWork: " + e.getMessage());;
                }
            }else
            {
                gps.showSettingsAlert();
            }

        }

/*

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Customers ordersToFind = new ordersDB();
                Intent intent2 = getIntent();

                final String emailToSet = intent2.getStringExtra("Email");
                ordersToFind.setProvince(emailToSet);

                final String queryString = intent2.getStringExtra("FID");

                Log.d("Finable", "Customer: " + queryString + " " + emailToSet);

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withS(queryString));

                DynamoDBQueryExpression<ordersDB> queryExpression = new DynamoDBQueryExpression<ordersDB>()
                        .withHashKeyValues(ordersToFind)
                        .withRangeKeyCondition("FID", rangeKeyCondition)
                        .withConsistentRead(false);


            }

        };

        Thread thread = new Thread(runnable);
        thread.start();
        */


        if(btnAccept.isPressed())
        {

            try {
                final Intent intent = new Intent(getApplicationContext(), jobsAcceptance.class);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Intent intentGet = getIntent();
                        //
                        String driverName = "";
                        String orderId = "";
                        if(intentGet.getStringExtra("driverName") != null)
                        {
                            driverName = intentGet.getStringExtra("driverName");
                        }
                        if(intentGet.getStringExtra("order") != null)
                        {
                            orderId = intentGet.getStringExtra("order");
                        }

                        //Customers findCustomer = mapper.load(Customers.class, custemail.getText().toString(), custFID);


                        Customers ordersToFind = new Customers();
                        Intent intent2 = getIntent();

                        final String emailToSet = custemail.getText().toString();
                        ordersToFind.setEMAIL(emailToSet);

                        final String queryString = custFID;

                        Log.d("Finable", "Customer: " + queryString + " " + emailToSet);

                        Condition rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(queryString));

                        DynamoDBQueryExpression<Customers> queryExpression = new DynamoDBQueryExpression<Customers>()
                                .withHashKeyValues(ordersToFind)
                                .withRangeKeyCondition("FID", rangeKeyCondition)
                                .withConsistentRead(false);

                        List<Customers> result = mapper.query(Customers.class, queryExpression);



                        String loadedCustomer = result.get(0).getUserId();
                        Log.d("find", "run: " + loadedCustomer);
                        // findCustomer.getUserId();
                        //load specific item from database
                        final ordersDB item = mapper.load(ordersDB.class, orderId);

                        String driverID =  FirebaseInstanceId.getInstance().getToken();
                        //if there is an item in the database then change the status back to closed
                        //set the driverid to the driver that is accepting the order
                        //save item back to database
                        if(item != null)
                        {
                            item.setStatus("closed");
                            item.setDriverID(driverID);
                            mapper.save(item);
                        }

                        Log.d("ORDERID", "run: " + orderId);

                        //send notification to customer
                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                        backgroundWorker.doInBackground("driverAccept",custFID,driverName, driverID, orderId, loadedCustomer);
                        //backgroundWorker.doInBackground("driverAccept");


                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                //redirect driver to jobs acceptance screen
                startActivity(intent);

            }catch (Exception e)
            {
                Log.d("CustomOrder", "WorksAvailable: " + e.getMessage()  );
            }

        }

    }
}
