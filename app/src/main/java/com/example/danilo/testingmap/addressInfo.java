package com.example.danilo.testingmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpRequest;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.UrlHttpClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.AddPermissionRequest;
import com.amazonaws.services.sns.model.CheckIfPhoneNumberIsOptedOutRequest;
import com.amazonaws.services.sns.model.CheckIfPhoneNumberIsOptedOutResult;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.GetPlatformApplicationAttributesRequest;
import com.amazonaws.services.sns.model.GetPlatformApplicationAttributesResult;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.GetSMSAttributesResult;
import com.amazonaws.services.sns.model.GetSubscriptionAttributesRequest;
import com.amazonaws.services.sns.model.GetSubscriptionAttributesResult;
import com.amazonaws.services.sns.model.GetTopicAttributesRequest;
import com.amazonaws.services.sns.model.GetTopicAttributesResult;
import com.amazonaws.services.sns.model.ListEndpointsByPlatformApplicationRequest;
import com.amazonaws.services.sns.model.ListEndpointsByPlatformApplicationResult;
import com.amazonaws.services.sns.model.ListPhoneNumbersOptedOutRequest;
import com.amazonaws.services.sns.model.ListPhoneNumbersOptedOutResult;
import com.amazonaws.services.sns.model.ListPlatformApplicationsRequest;
import com.amazonaws.services.sns.model.ListPlatformApplicationsResult;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsResult;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.OptInPhoneNumberRequest;
import com.amazonaws.services.sns.model.OptInPhoneNumberResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.RemovePermissionRequest;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SetPlatformApplicationAttributesRequest;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;
import com.amazonaws.services.sns.model.SetSMSAttributesResult;
import com.amazonaws.services.sns.model.SetSubscriptionAttributesRequest;
import com.amazonaws.services.sns.model.SetTopicAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
//import com.firebase.client.Firebase;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class addressInfo extends AppCompatActivity {

    Button btnEditAddress;
    String custAddres = "";
    DatabaseHelper myDb;
    Button btnSubmit;
    Button btnComplete1;
    TextView textView;
    EditText editTxt;
    EditText customerName;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String city = "";
    String state = "";
    String country = "";
    String postalCode = "";
    String knownName = "";
    GPSTracker gps;
    DynamoDBMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_info);








        try {
        //myDb = new DatabaseHelper(this);
            Intent intent = getIntent();
            customerName = (EditText) findViewById(R.id.editTxtName);
            editTxt = (EditText) findViewById(R.id.editTextAddress);
            customerName.setEnabled(false);
            editTxt.setEnabled(false);

            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                    Regions.US_EAST_1// Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            final String custFID =  FirebaseInstanceId.getInstance().getToken();

            mapper = new DynamoDBMapper(ddbClient);
            Log.d("TOPIC", "onCreate: " + custFID );


            btnEditAddress = (Button) findViewById(R.id.btnEditAddress);
            btnSubmit = (Button) findViewById(R.id.btnComplete);

            btnEditAddress.setOnClickListener(MyListener);
            btnSubmit.setOnClickListener(MyListener);
            gps = new GPSTracker(addressInfo.this);

    // check if GPS enabled
    if (gps.canGetLocation()) {

        Geocoder geocoder;
        List<Address> addresses;
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        geocoder = new Geocoder(addressInfo.this);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
            //Toast.makeText(getApplicationContext(), city + " " + knownName + " " + state + " " + country, Toast.LENGTH_LONG).show();
            //Intent startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
            //startWorkIntent.putExtra("city",city);
            //startActivity(startWorkIntent);
            //finish();

        }
        catch (Exception e) {

            Log.d("cannot Start", "StartWork: " + e.getMessage());;
        }



        String addressLine = gps.getCompleteAddressString(latitude, longitude);
        editTxt.setText(addressLine);
        custAddres = editTxt.getText().toString();
        if (intent.getStringExtra("custName") != null) {
            customerName.setText(intent.getStringExtra("custName"));
        }

        // \n is for new line
        // Toast.makeText(getApplicationContext(), "Your Location is - " + addressLine, Toast.LENGTH_LONG).show();
    } else {
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }
}catch(Exception e)
{
    Log.d("address error", "onCreate: " + e.getMessage());
}

    }
    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            OrderFinal(v);
        }
    };

    private void OrderFinal(View v)
    {
        try
        {
            if(btnSubmit.isPressed())
            {
                Intent intent1 = getIntent();
                String custName = intent1.getStringExtra("custName");
                String custEmail = intent1.getStringExtra("custEmail");
                String placeToGo = intent1.getStringExtra("placesToGo");
                String whatTheyWant = intent1.getStringExtra("orderValue");
                String custCity = city;
                String custPost = postalCode;
                String custCountry = country;
                final String custFID =  FirebaseInstanceId.getInstance().getToken();


                Intent intent = new Intent(getApplicationContext(), thankYou.class);
                intent.putExtra("custName", custName);
                intent.putExtra("placesToGo",placeToGo);
                intent.putExtra("custEmail",custEmail);
                intent.putExtra("orderValue", whatTheyWant);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //get current date time with Date()
                Date date = new Date();
                final Intent getIntent = getIntent();
                //System.out.println(dateFormat.format(date));

                final ordersDB newOrder = new ordersDB();
                final Customers updateCustomer = new Customers();
                updateCustomer.setEMAIL(custEmail);
                //newCustomer.setUserId(Guid.toString());
                newOrder.setCustomerName(custName);
                newOrder.setEmail(custEmail);
                newOrder.setCity(custCity);
                newOrder.setProvince(state);
                newOrder.setPostalCode(custPost);
                newOrder.setCountry(custCountry);
                newOrder.setFullAddress(custAddres);
                newOrder.setPlaceToGo(placeToGo);
                newOrder.setWhatTheyWant(whatTheyWant);
                newOrder.setCREATED_AT(dateFormat.format(date));
                newOrder.setCustFID(custFID);
                newOrder.setStatus("available");

                Log.d("token", "OrderFinal: " + newOrder.getCustFID());

                //
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        mapper.save(newOrder);

                        String queryString = getIntent.getStringExtra("PID");

                        Condition rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(queryString));

                        DynamoDBQueryExpression<Customers> queryExpression = new DynamoDBQueryExpression<Customers>()
                                .withHashKeyValues(updateCustomer)
                                .withRangeKeyCondition("UID", rangeKeyCondition)
                                .withConsistentRead(false);
                        Customers result = mapper.query(Customers.class, queryExpression).get(0);

                        result.setFID(custFID);


                        /**/
                        //Customers loadedCustomer = mapper.load(Customers.class,updateCustomer,queryString);
                        //Log.d("loadCustomer", "run: " + loadedCustomer.getFIRSTNAME());
                        //loadedCustomer.setFID(custFID);
                        //mapper.save(loadedCustomer);

                        //getResults(result.get(0));
                       mapper.save(result);
                        //Driverdb selectedDriver = mapper.load(Driverdb.class, "5465d4gsdseda");
                        //Log.d("oldDriver", "run: " + selectedDriver.getFirstName() + " " + selectedDriver.getLastName() + " " + selectedDriver.getEmail());
                    }

                    public void getResults(Customers customer)
                    {
                        Log.d("results", "getResults: " + customer.getEMAIL());
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();


                //myDb.InsertNewOrder(custName, custEmail, placeToGo, custCity, custPost, custCountry, custAddres, whatTheyWant);
                Toast.makeText(getApplicationContext(), "order Complete", Toast.LENGTH_LONG).show();
                Intent thankyou = new Intent(getApplicationContext(),thankYou.class);
                startActivity(thankyou);
                finish();
                //Firebase fire = new Firebase("https://fcm.googleapis.com/fcm/send");

                //Firebase newFire = fire.push();
                //MyFirebaseMessagingService fires = new MyFirebaseMessagingService();
                //fires.send(newOrder.getCustFID());
                /*Map<String, String> val = new HashMap<>();
                newFire.setValue(val);
                String UID = newFire.getKey();*/






               // RemoteMessage.Notification notification = new Notification.Builder()
                //HttpClient client = new Cli

                //Intent startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
                //intent.putExtra("city",city);
                //long rowInserted = myDb.InsertNewOrder(custName, custEmail, placeToGo,custAddres, whatTheyWant);
                /*
                Intent intent34 = new Intent();
                PendingIntent pItent = PendingIntent.getActivity(this,0, intent34, 0);
                Notification noti = new Notification.Builder(this)
                        .setTicker("Ticker Title")   //ticker title
                        .setContentTitle("Food Delivering")
                        .setContentText("Customer order " + custName + " " + placeToGo + " " + custEmail + " " + whatTheyWant)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pItent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
                */
                /*
            if(rowInserted != -1)
            {
                //send the notification to owners from here cause new order has been inserted

            }*/
                //Log.d("addressInfo", "order was inserted correctly");
                //startActivity(intent);
                //finish();
            }
            if(btnEditAddress.isPressed())
            {
                editTxt.setEnabled(true);
            }
        }
        catch(Exception e)
        {
            Log.d("AddressInfo", "OrderFinal: " + e.getMessage());
        }
    }

}
