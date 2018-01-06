package com.example.danilo.testingmap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpRequest;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Danilo on 2016-04-29.
 */
public class login extends Activity
{
    Button btnSignIn,btnSignUp;
    EditText editEmail, editPass;
    DynamoDBMapper mapper;
    AmazonDynamoDBClient ddbClient;
    DatabaseHandler loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

// Get The Refference Of Buttons
        btnSignIn=(Button)findViewById(R.id.login);
        btnSignUp=(Button)findViewById(R.id.registerbtn);
        editEmail=(EditText)findViewById(R.id.email);
        editPass=(EditText)findViewById(R.id.pword);
        btnSignIn.setOnClickListener(MyListener);
        btnSignUp.setOnClickListener(MyListener);




        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1// Region
        );


        ddbClient = new AmazonDynamoDBClient(credentialsProvider);


        mapper = new DynamoDBMapper(ddbClient);
        //credentialsProvider.getCredentials();
        //
        /*
        try{
        // create a instance of SQLite Database
        loginDataBaseAdapter = new DatabaseHandler(this);
        //loginDataBaseAdapter=loginDataBaseAdapter.open();



        // Set OnClick Listener on SignUp button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intentSignUP=new Intent(getApplicationContext(),DriverRegistration.class);
                startActivity(intentSignUP);
            }
        });
        }
        catch(Exception e)
        {
            Log.d("Login", "onCreate: " + e.getMessage());
        }*/
    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onLogin(v);
        }
    };


    public void onLogin(View V)
    {
        if(btnSignIn.isPressed())
        {
            String userEmail = editEmail.getText().toString();
            String password = editPass.getText().toString();

           signIn(V);
        }
        if(btnSignUp.isPressed())
        {
            Intent intentSignUP=new Intent(getApplicationContext(),DriverRegistration.class);
            startActivity(intentSignUP);
        }

    }
    public void signIn(View V)
    {
        final Dialog dialog = new Dialog(login.this);
        dialog.setContentView(R.layout.activity_login);
        dialog.setTitle("Login");

        // get the Refferences of views
        final  EditText editTextUserEmail=(EditText)dialog.findViewById(R.id.email);
        final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.pword);

        Button btnSignIn=(Button)dialog.findViewById(R.id.login);

        Button btnSignUP =(Button)dialog.findViewById(R.id.registerbtn);
        editTextUserEmail.setText(editEmail.getText().toString());
        editTextPassword.setText(editPass.getText().toString());

        editTextUserEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                // get The User name and Password
                final String userEmail = editTextUserEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                try {
// Do something with result.
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            Driverdb driverToFind = new Driverdb();
                            driverToFind.setEmail(userEmail);

                            String queryString = password;

                            Condition rangeKeyCondition = new Condition()
                                    .withComparisonOperator(ComparisonOperator.EQ.toString())
                                    .withAttributeValueList(new AttributeValue().withS(queryString));

                            DynamoDBQueryExpression<Driverdb> queryExpression = new DynamoDBQueryExpression<Driverdb>()
                                    .withHashKeyValues(driverToFind)
                                    .withRangeKeyCondition("UID", rangeKeyCondition)
                                    .withConsistentRead(false);
                            PaginatedQueryList<Driverdb> result = mapper.query(Driverdb.class, queryExpression);

                            //if(result != null)
                            // {
                                    // do the runnable method on the front end to display a dialog box to user about login
                            // }

                            getResults(result);


                        }
                        public void getResults(PaginatedQueryList<Driverdb> loadedDriver) {
                            Intent intent = new Intent(getApplicationContext(), DriverMain.class);
                            //Log.d("loaded", "getResults: " + loadedCustomer.get(0).getFIRSTNAME());
                            intent.putExtra("firstname", loadedDriver.get(0).getFirstName());
                            intent.putExtra("lastname", loadedDriver.get(0).getLastName());
                            //intent.putExtra("username", loadedDriver.get(0).getUsername());
                            intent.putExtra("email", loadedDriver.get(0).getEmail());
                            startActivity(intent);
                        }
                    };


                    Thread thread = new Thread(runnable);
                    thread.start();
                    //dialog.dismiss();
                }
                catch (Exception e)
                {
                    Log.d("login Fail:", "onClick: " + e.getMessage());
                }
/*

                                             // fetch the Password form database for respective user name
                                             String storedPassword = loginDataBaseAdapter.getSingleEntry(userEmail);

                                             // check if the Stored password matches with  Password entered by user
                                             if (password.equals(storedPassword)) {
                                                 Toast.makeText(login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                                                 SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                 SharedPreferences.Editor editor = settings.edit();
                                                 HashMap currentUser = loginDataBaseAdapter.getSingleUserInfo(userEmail);
                                                 //currentUser.get("")
                                                 //Bundle ser = new Bundle();
                                                 //ser.putSerializable("ser",currentUser);

                                                 //loginDataBaseAdapter.getSingleUserInfo(userEmail);
                                                 //Toast.makeText(login.this, loginDataBaseAdapter.getSingleUserInfo(userEmail)., Toast.LENGTH_LONG).show();
                                                 //editor.putString("currentUser", currentUser);

                                                 dialog.dismiss();
                                                 Intent intentlogin = new Intent(getApplicationContext(), DriverMain.class);
                                                 intentlogin.putExtra("currentEmail", userEmail);
                                                 intentlogin.putExtra("userCurrent", currentUser);
                                                 startActivity(intentlogin);
                                                 finish();
                                             } else {
                                                 Toast.makeText(login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                             }*/

                                         }
                                     }

        );


            btnSignUP.setOnClickListener(new View.OnClickListener()

                                         {
                                             public void onClick(View v) {
                                                 // TODO Auto-generated method stub
                                                 /// Create Intent for SignUpActivity  abd Start The Activity
                                                 Intent intentSignUP = new Intent(getApplicationContext(), DriverRegistration.class);
                                                 startActivity(intentSignUP);
                                             }
                                         }

            );

        dialog.show();
        }


    }
