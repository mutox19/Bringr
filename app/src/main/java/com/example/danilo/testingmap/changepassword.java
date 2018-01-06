package com.example.danilo.testingmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class changepassword extends AppCompatActivity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    EditText newpass;
    TextView alert;
    Button changepass;
    Button cancel;
    DynamoDBMapper mapper;
    AmazonDynamoDBClient ddbClient;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changepassword);

        cancel = (Button) findViewById(R.id.btncancel);
        changepass = (Button)findViewById(R.id.btnchangepass);

        newpass = (EditText) findViewById(R.id.newpass);



        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1// Region
        );

        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);

        final Intent getIntent = getIntent();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent customerMain = new Intent(getApplicationContext(), customerMain.class);

                customerMain.putExtra("firstname",getIntent.getStringExtra("firstname"));
                customerMain.putExtra("lastname",getIntent.getStringExtra("lastname"));
                customerMain.putExtra("email",getIntent.getStringExtra("email"));
                customerMain.putExtra("PID",getIntent.getStringExtra("PID"));
                startActivity(customerMain);
                finish();
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        String newPass = newpass.getText().toString();

                        Customers ordersToFind = new Customers();
                        Intent intent2 = getIntent();

                        final String emailToSet = intent2.getStringExtra("email");
                        ordersToFind.setEMAIL(emailToSet);

                        final String queryString = intent2.getStringExtra("PID");

                        Condition rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(queryString));

                        DynamoDBQueryExpression<Customers> queryExpression = new DynamoDBQueryExpression<Customers>()
                                .withHashKeyValues(ordersToFind)
                                .withRangeKeyCondition("UID", rangeKeyCondition)
                                .withConsistentRead(false);

                        List<Customers> result = mapper.query(Customers.class, queryExpression);

                        //load specific item from database
                        //if there is an item in the database then change the status back to closed
                        //set the driverid to the driver that is accepting the order
                        //save item back to database
                        if(result != null)
                        {
                            result.get(0).setUID(newPass);
                            mapper.save(result);
                            Log.d("PassSuccess", "Password success change");
                        }
                        //send notification to customer
                        //BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                        //backgroundWorker.doInBackground("driverAccept",custFID,driverName, driverID, orderId, loadedCustomer);
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

    }
}
