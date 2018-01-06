package com.example.danilo.testingmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    Button customerMain, driverMain;
    AmazonDynamoDBClient ddbClient;
    LocationManager locationManager;
    String Name, Email, Login, Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gps = new GPSTracker(this);

        //var options = FirebaseInstanceId.getInstance().getToken();



        //old cognito with new cred
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //CreatePlatformEndpointResult result = null;
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        String regid = FirebaseInstanceId.getInstance().getToken();

        Log.d("TokenFireBase", "onCreate: " + regid);



        //register device with amazon
        //PushManager pushManager = AWSMobileClient.defaultMobileClient().getPushManager();
        //pushManager.registerDevice();
        // if registration succeeded.
        /*if (pushManager.isRegistered())
        {
            pushManager.setPushEnabled(true);
        }
        else {
            // ... handle error, likely due to no network available ...
        }
*/


/*
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String regid = FirebaseInstanceId.getInstance().getToken();
                BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                backgroundWorker.doInBackground("notification",regid, "hollaa", "what ma nigg");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

*/

            /*

            //Log.d("RESULTS", "onCreate: " + result.getEndpointArn());
        }
        /*
        try
        {
            String platformApplicationArn = "arn:aws:sns:us-east-1:833775920729:app/GCM/Food";
            //AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJ3FSDAJRVJNR5G4Q", "M6ofgX/9IU2PJdFoGfd4p6DZ1yh4N3NjQhYCn1j4");
            AWSCredentials cred = new AWSCredentials() {
                @Override
                public String getAWSAccessKeyId() {
                    return "AKIAJ3FSDAJRVJNR5G4Q";
                }

                @Override
                public String getAWSSecretKey() {
                    return "9IU2PJdFoGfd4p6DZ1yh4N3NjQhYCn1j4";
                }
            };
            AmazonSNSClient pushClient = new AmazonSNSClient(cred);
            Log.d("LIST", "onCreate: " + pushClient.listPlatformApplications());
            pushClient.setRegion(Region.getRegion(Regions.US_WEST_1));
//probably no need for this
            String customPushData = "dan_307@hotmail.com";

            CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();

            platformEndpointRequest.setCustomUserData(customPushData);
            platformEndpointRequest.setToken(regid);
            platformEndpointRequest.setPlatformApplicationArn(platformApplicationArn);


             result = pushClient.createPlatformEndpoint(platformEndpointRequest);
        }catch (Exception e)
        {
            Log.d("EXCEPTION", "onCreate: " + e.getMessage());
        }


*/


        //to send the notification
       // Endpoint end = new Endpoint();
        //end.setEndpointArn(regid);
        //PublishRequest publish = new PublishRequest();
        //publish.setTargetArn(end.getEndpointArn());
        //publish.setMessage("this is the messgae");

        //PublishResult resu = new PublishResult();
       // resu.getMessageId();

        //sns.publish(publish);
            //Content content = createContent(regid);
            //Post2GCM.post("AIzaSyCMwLEulWK0pY4v-bUQ-sI-tqCrQNj21v8",content);



        //testing getting the device id
       /* String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Toast.makeText(this, deviceId, Toast.LENGTH_SHORT).show();*/

        customerMain = (Button)findViewById(R.id.btnMapDemo);
        customerMain.setOnClickListener(MyListener);

        //customerMain.setBackgroundColor(getResources().getColor(R.color.wallet_dim_foreground_inverse_holo_dark));
        //customerMain.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));




        driverMain = (Button)findViewById(R.id.btnMapMain);
        driverMain.setOnClickListener(MyListener);
        driverMain.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
        driverMain.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));


    }


    public void onReg(View v){

        String regid = FirebaseInstanceId.getInstance().getToken();
        String title = "Delivery";
        String message = "my new magg";
        String type = "notification";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,regid,title,message);

    }
    public static Content createContent(String regId){

        Content c = new Content();

        c.addRegId(regId);
        c.createData("this is the title","this is the message");

        return c;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            OrderFinal(v);

        }
    };


    // Create GetText Metod
    public  void  GetText(View v)  throws UnsupportedEncodingException
    {

        if(customerMain.isPressed())
        {



            //this is for calling notifications
            /*
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    BackgroundWorker background = new BackgroundWorker(getBaseContext());
                    background.doInBackground("test","dan","dan_12@hotmail.com","tye@tye.com","sheea is sexy");


                   /* if(isOnline(getApplicationContext()))
                    {
                        // Get user defined values
                        Name = "dan";
                        Email   = "dan_12@hotmail.com";
                        Login   = "tye@tye.com";
                        Pass   = "123";

                        // Create data variable for sent values to server

                        String data = null;
                        try {
                            data = URLEncoder.encode("title", "UTF-8")
                                    + "=" + URLEncoder.encode(Name, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        try {
                            data += "&" + URLEncoder.encode("message", "UTF-8") + "="
                                    + URLEncoder.encode(Email, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                /*data += "&" + URLEncoder.encode("user", "UTF-8")
                        + "=" + URLEncoder.encode(Login, "UTF-8");

                data += "&" + URLEncoder.encode("pass", "UTF-8")
                        + "=" + URLEncoder.encode(Pass, "UTF-8");*/

                        //String text = "";
                       // BufferedReader reader=null;

                        // Send data
                    /*
                        try
                        {

                            // Defined URL  where to send data
                            URL url = new URL("http://192.168.5.3:8080/firebase/notification.php");
                            //testURL();
                            // Send POST data request

                            URLConnection conn = url.openConnection();
                            conn.connect();
                            conn.setDoOutput(true);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            wr.write( data );
                            wr.flush();




                            // Get the server response

                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;


                            // Read Server Response
                            while((line = reader.readLine()) != null)
                            {
                                // Append server response in string
                                //sb.append(line + "\n");
                                text += line + "\n";
                            }


                            //text = sb.toString();
                        }
                        catch(Exception ex)
                        {

                        }
                        finally
                        {
                            try
                            {

                                reader.close();
                            }

                            catch(Exception ex) {}
                        }
                        // Show response on activity
                        //content.setText( text  );
                        Log.d("PHPRESULTS", "GetText: " + text);
                        returntext(text);
                        //Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"SORRY COULD NOT CONNECT TO INTERNET",Toast.LENGTH_LONG).show();
                    }
*/
               /* }

            };

            Thread thread = new Thread(runnable);
            //thread.run();
            thread.start();

*/
        }


    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }
    public void testURL() {
        String strUrl = "http://127.0.0.1:8080/deliveryservice/send_notifications.php";

        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            int status = urlConn.getResponseCode();
            String retStr ="";
            if(status != HttpURLConnection.HTTP_OK)
            {
                retStr = "failed";
                Log.d("FAILED", "testURL: " + retStr);
            }else
            {
                retStr ="connecte";
                Log.d("CONNECTED", "testURL: " + retStr);
            }
            if(urlConn == null)
            {
                Log.d("gee", "testURL: " + "failed geee");
            }else{Log.d("gee", "testURL: " + "passed geee");}

            //Log.d("RESPONSE", "testURL: " + urlConn.getResponseMessage());
        } catch (IOException e) {
            Log.d("CAPPED", "testURL: Error creating HTTP connection");
            e.printStackTrace();

        }
    }
    private void OrderFinal(View v)
    {
        try
        {
            if(customerMain.isPressed())
            {

                Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                startActivity(intent);
                finish();
            }
            if(driverMain.isPressed())
            {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        }
        catch(Exception e)
        {
            Log.d("MapButtons", "OrderFinal: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(getApplicationContext(), "Location Change: " + location.getLatitude() + location.getLongitude() , Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),gps.getCompleteAddressString(gps.getLatitude(), gps.getLongitude()),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        gps = new GPSTracker(MapsActivity.this);

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
                //String state = addresses.get(0).getAdminArea();
                //String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName();
                //Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(sydney).title("Driver Danilo is in " + city).icon(BitmapDescriptorFactory.fromResource(R.mipmap.yellowdriver)));

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                //LatLng loc2 = new LatLng(43.437397, -80.48637);
               // mMap.addMarker(new MarkerOptions()
                 //       .position(loc2).title("Marker in " + city));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc2));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 13.0f));
                //Intent startWorkIntent = new Intent(getApplicationContext(), worksAvailable.class);
                //startWorkIntent.putExtra("city",city);
                //startActivity(startWorkIntent);
                //finish();
               /* if(gps.getDist(latitude, longitude, 43.437397, -80.48637) < 3000)
                {
                    Toast.makeText(getApplicationContext(), "this dis is " + gps.getCompleteAddressString(gps.getLatitude(),gps.getLongitude()), Toast.LENGTH_LONG).show();

                }*/
                //onLocationChanged(location);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 5000, locationListener);




            }
            catch (Exception e) {

                Log.d("cannot Start", "StartWork: " + e.getMessage());;
            }



        }
        else
        {
            gps.showSettingsAlert();
        }
    }
}
