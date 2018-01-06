package com.example.danilo.testingmap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class worksAvailable extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView myListView;
    DynamoDBMapper mapper;
    List<ordersDB> ordersss;
    List<ordersDB>newListItems;
    Cursor c;
    //ordersss
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_available);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        try {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                    Regions.US_EAST_1// Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            mapper = new DynamoDBMapper(ddbClient);
            myListView = (ListView) findViewById(R.id.scListView);

            ordersss = new ArrayList<>();
            Intent intent = getIntent();
            String cityCall = intent.getStringExtra("city");
            String stateCall = intent.getStringExtra("state");
            if(stateCall != null && cityCall != null)
            {
                //final String city = intent.getStringExtra("city");
                //final String state = intent.getStringExtra("state");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        ordersDB ordersToFind = new ordersDB();
                        Intent intent2 = getIntent();

                        final String provinceToSet = intent2.getStringExtra("state");
                        ordersToFind.setProvince(provinceToSet);

                        final String queryString = intent2.getStringExtra("city");

                        Log.d("CONDITIONS", "run: " + queryString + " " + provinceToSet);

                        Condition rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(queryString));

                        DynamoDBQueryExpression<ordersDB> queryExpression = new DynamoDBQueryExpression<ordersDB>()
                                .withHashKeyValues(ordersToFind)
                                .withRangeKeyCondition("City", rangeKeyCondition)
                                .withConsistentRead(false);
                                //PaginatedQueryList<ordersDB> result = mapper.query(ordersDB.class, queryExpression);

                        final List<ordersDB> latestReplies = mapper.query(ordersDB.class, queryExpression);

                        //check to see if the query returned any rows
                        if(latestReplies.size() >0)
                        {
                            //add only items that have available as there status
                            for(ordersDB order: latestReplies)
                            {
                                if(order.getStatus().equals("available"))
                                {
                                    ordersss.add(order);
                                }
                            }
                        }

                        if(ordersss.size() >0 )
                        {
                            //getResults(latestReplies);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("UI thread", "I am the UI thread");

                                //ListAdapter scoresAdapter = new CustomAdapter(getApplicationContext(), ordersss);
                                BaseAdapter scoresAdapter = new MyCustomBaseAdapter(getApplicationContext(),ordersss);
                                myListView.setAdapter(scoresAdapter);

                                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        if (ordersss.iterator().hasNext()) {
                                            String address = ordersss.get(position).getFullAddress();
                                            String custName = ordersss.get(position).getCustomerName();
                                            String custEmail = ordersss.get(position).getEmail();
                                            String placeToShop = ordersss.get(position).getPlaceToGo();
                                            String custorder = ordersss.get(position).getWhatTheyWant();
                                            String custFID = ordersss.get(position).getCustFID();
                                            String order = ordersss.get(position).getOrderId();

                                            Intent getIntent = getIntent();
                                            Intent jobIntent = new Intent(getApplicationContext(), customerOrder.class);
                                            jobIntent.putExtra("customerAdd",address);
                                            jobIntent.putExtra("customerName",custName);
                                            jobIntent.putExtra("customerEmail",custEmail);
                                            jobIntent.putExtra("customerPlaceToShop",placeToShop);
                                            jobIntent.putExtra("customerOrder",custorder);
                                            jobIntent.putExtra("order",order);
                                            jobIntent.putExtra("driverName",getIntent.getStringExtra("firstname"));
                                            //jobIntent.putExtra("customerOrderId",custFID);
                                            jobIntent.putExtra("customerFirebase",custFID);

                                            startActivity(jobIntent);
                                            finish();
                                        }

                                    }
                                });

                                /*latit = gps.GetLatFromPost(item.getPostalCode());
                                //String post = ordersList.getText().toString();
                                longit = gps.GetLongFromPost(item.getPostalCode());
                                custNam = item.getCustomerName();
                                LatLng sydney = new LatLng(latit, longit);
                                mMap.addMarker(new MarkerOptions()
                                        .position(sydney).title("Customer "  + custNam + " Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

                                Toast.makeText(getApplicationContext(), "main thread nigga", Toast.LENGTH_LONG).show();*/
                            }
                        });
                        }else
                        {
                            Log.d("LATESTREPLIES", "No Orders available");
                        }






                                //ordersss = mapper.query(ordersDB.class, queryExpression);

                    }
                    void getResults(List<ordersDB> loadedOrders)
                    {
                        for (ordersDB or: loadedOrders)
                        {
                            ordersss.add(or);
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();



            }else
            {
                Toast.makeText(getApplicationContext(), "Error Please refresh, or try again", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Log.d("ecept", "onCreate: "+ e.getMessage());
        }


        //myListView = (ListView) findViewById(R.id.scListView);
        //myListView.setAdapter(scoresAdapter);
        //myListView.setAdapter(new CustomAdapter(this, ordersss));






        }


        /*

        myDb = new DatabaseHelper(this);
        Intent intent = getIntent();


        if(intent.getStringExtra("city") != null)
        {

            String city = intent.getStringExtra("city");
            Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();

            PopulateWorksList(city);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    if (c.moveToPosition(position)) {
                       String address = c.getString(c.getColumnIndex(DatabaseHelper.COL_8));
                        String custName = c.getString(c.getColumnIndex(DatabaseHelper.COL_2));
                        String custEmail = c.getString(c.getColumnIndex(DatabaseHelper.COL_3));
                        String placeToShop = c.getString(c.getColumnIndex(DatabaseHelper.COL_4));
                        String custorder = c.getString(c.getColumnIndex(DatabaseHelper.COL_9));

                        Intent jobIntent = new Intent(getApplicationContext(), customerOrder.class);
                        jobIntent.putExtra("customerAdd",address);
                        jobIntent.putExtra("customerName",custName);
                        jobIntent.putExtra("customerEmail",custEmail);
                        jobIntent.putExtra("customerPlaceToShop",placeToShop);
                        jobIntent.putExtra("customerOrder",custorder);

                        startActivity(jobIntent);
                        finish();
                    }

                }
            });
            /*Cursor c = myDb.GetAllMediumInfo();

            try {

                ListAdapter worksAdapter = new CustomAdapter(this, c);
                ListView myListView = (ListView) findViewById(R.id.scListView);
                myListView.setAdapter(worksAdapter);

            }
            catch (Exception e) {
                Log.d("list", "cannot add list : " + e.getMessage());
            }

        }
*/

    //}


    public List PopulateWorksList(List<ordersDB> loadedOrders) {


      return loadedOrders;
    }


    public void PopulateWorksList1(String city) {

        c = myDb.GetOrders(city);
        //Cursor c = myDb.GetAllMediumInfo();

        try {

            ListAdapter scoresAdapter = new CustomAdapter(this, ordersss);
            myListView = (ListView) findViewById(R.id.scListView);
            myListView.setAdapter(scoresAdapter);
            Log.d("CityTest", "PopulateWorksList: " + city);
        } catch (Exception e) {
            Log.d("list", "cannot add list : " + e.getMessage());
        }
    }

}
