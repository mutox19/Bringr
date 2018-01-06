package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class customerRegistration extends AppCompatActivity {

    EditText editTextUserName,editTextPassword,editTextConfirmPassword, firstName, lastName, email;
    Button btnCreateAccount;
    Button backToLogin;
    String deviceId;

    DynamoDBMapper mapper;
    DatabaseCustomer customerDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);



        try {

            //new
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                    Regions.US_EAST_1// Region
            );

            //this is the old credentials
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            //this is the new way to do it
            mapper = new DynamoDBMapper(ddbClient);
                    //AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                    //

            //testing getting the device id
            //deviceId = Settings.Secure.getString(this.getContentResolver(),
            //      Settings.Secure.ANDROID_ID);
            //Toast.makeText(this, deviceId, Toast.LENGTH_SHORT).show();
            //customerDatabaseHandler = new DatabaseCustomer(this);

            editTextUserName = (EditText) findViewById(R.id.uname);
            editTextPassword = (EditText) findViewById(R.id.pword);
            firstName = (EditText) findViewById(R.id.fname);
            lastName = (EditText) findViewById(R.id.lname);
            email = (EditText) findViewById(R.id.email);
            editTextConfirmPassword = (EditText) findViewById(R.id.confirmpword);

            btnCreateAccount = (Button) findViewById(R.id.register);
            backToLogin = (Button) findViewById(R.id.bktologin);

            backToLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                    startActivity(intent);
                }
            });

            btnCreateAccount.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String userName = editTextUserName.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String confirmPassword = editTextConfirmPassword.getText().toString();
                    String userFirstName = firstName.getText().toString();
                    String userLastName = lastName.getText().toString();
                    String userEmail = email.getText().toString();


                    // check if any of the fields are vaccant
                    if (userName.equals("") || password.equals("") || confirmPassword.equals("") ||
                            userFirstName.equals("") || userLastName.equals("") || userEmail.equals("")) {
                        Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    // check if both password matches
                    else if (!password.equals(confirmPassword)) {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                        //return;
                    } else {

                        // Save the Data in Database
                    /*customerDatabaseHandler.insertEntry(userFirstName, userLastName, userEmail, userName, confirmPassword, deviceId);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created, you will be redirected to login ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                    startActivity(intent);


                    */

                        try {

                            String regid = FirebaseInstanceId.getInstance().getToken();

                            //UUID Guid = java.util.UUID.randomUUID();
                            //Log.d("ID:", "onCreate: " + Guid);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            //get current date time with Date()
                            Date date = new Date();
                            //System.out.println(dateFormat.format(date));

                            final Customers newCustomer = new Customers();
                            //newCustomer.setUserId(Guid.toString());
                            newCustomer.setFIRSTNAME(firstName.getText().toString());
                            newCustomer.setLASTNAME(lastName.getText().toString());
                            //newCustomer.setUSERNAME(editTextUserName.getText().toString());
                            newCustomer.setEMAIL(email.getText().toString());
                            newCustomer.setUID(password);
                            newCustomer.setFID(regid);
                            newCustomer.setCREATED_AT(dateFormat.format(date));



                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    mapper.save(newCustomer);
                                    //Log.d("oldDriver", "run: " + selectedDriver.getFirstName() + " " + selectedDriver.getLastName() + " " + selectedDriver.getEmail());
                                }
                            };

                            Thread thread = new Thread(runnable);
                            thread.start();

                            Toast.makeText(getApplicationContext(), "Customer Registration Complete", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            Log.d("error", "onClick: " + e.getMessage());
                        }

                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.d("custFail", "onCreate: " + e.getMessage());
        }
    }



}
