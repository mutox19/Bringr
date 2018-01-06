package com.example.danilo.testingmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class viewer extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    Button processOrder, driverMain;
    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;
    EditText ordersList;
    double latit = 0.0;
    double longit = 0.0;
    String custNam = "";
    LocationManager locationManager;
    String Name, Email, Login, Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.viewer);
        mapFragment.getMapAsync(viewer.this);
        gps = new GPSTracker(viewer.this);

        //var options = FirebaseInstanceId.getInstance().getToken();

        processOrder = (Button) findViewById(R.id.btnViewOrder);

        processOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display the order for the driver so the driver can view order and complete order.

                Intent intent = new Intent(getApplicationContext(), driverViewOrder.class);

                //get credentials needed to be passed
                Intent getIntent = getIntent();
                String order = getIntent.getStringExtra("order");
                String customer = getIntent.getStringExtra("customer");

                String driver = getIntent.getStringExtra("driver");
                Log.d("VIEWER", "onClick: " + customer + " driver: " + driver + " order: " + order);
                //pass order and driver credentials
                intent.putExtra("order", order );
                intent.putExtra("customer", customer);
                intent.putExtra("driver", driver);


                startActivity(intent);
            }
        });


        //old cognito with new cred
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //CreatePlatformEndpointResult result = null;
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);

        String regid = FirebaseInstanceId.getInstance().getToken();

        Log.d("TokenFireBase", "onCreate:  viewerMap");


        ordersList = (EditText) findViewById(R.id.editTxtOrdersList);


        //double longit = 0.0;
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //search the database for the order so you can display the order fpr the driver
                //ordersDB orderToFind = new ordersDB();
                Intent newin = new Intent(getApplicationContext(), viewer.class);

                Intent intent = getIntent();
                //orderToFind.setOrderId(intent.getStringExtra("order"));
                String queryString = intent.getStringExtra("order");
                Log.d("QUERY6", "run: " + queryString);

                final ordersDB item = mapper.load(ordersDB.class, queryString);

                ordersList.setText(item.getPostalCode());
                //getResults(item.getPostalCode(), item.getCustomerName());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("UI thread", "I am the UI thread");
                        latit = gps.GetLatFromPost(item.getPostalCode());
                        //String post = ordersList.getText().toString();
                        longit = gps.GetLongFromPost(item.getPostalCode());
                        custNam = item.getCustomerName();
                        LatLng sydney = new LatLng(latit, longit);
                        mMap.addMarker(new MarkerOptions()
                                .position(sydney).title("Customer " + custNam + " Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blkhouse)));

                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom());
                        Toast.makeText(getApplicationContext(), "main thread nigga", Toast.LENGTH_LONG).show();
                    }
                });

            }

            void getResults(String res, String name) {

                SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("postal", res);
                editor.putString("name", name);
                editor.commit();
            }
        };

        //mainHandler.post(runnable);
        Thread thread = new Thread(runnable);
        thread.start();

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


        gps = new GPSTracker(viewer.this);

        if (gps.canGetLocation()) {
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
                final Marker driverMarker = mMap.addMarker(new MarkerOptions()
                        .position(sydney).title("Driver Danilo is in " + city).icon(BitmapDescriptorFactory.fromResource(R.mipmap.yellowdriver)));
                //mMap.addMarker(new MarkerOptions()
                       // .position(sydney).title("Driver Danilo is in " + city).icon(BitmapDescriptorFactory.fromResource(R.mipmap.yellowdriver)));

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 5000, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        driverMarker.remove();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
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
                });


            } catch (Exception e) {

                Log.d("cannot Start", "StartWork: " + e.getMessage());
                ;
            }

        } else {
            gps.showSettingsAlert();
        }
    }
}
