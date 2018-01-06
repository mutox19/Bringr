package com.example.danilo.testingmap;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.util.Tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerLogin extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    EditText editEmail, editPass;

    DatabaseCustomer customerHandler;
    DynamoDBMapper mapper;
    AmazonDynamoDBClient ddbClient;
    static Object customerUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);


        try {

/*


*/
            // Get The Refference Of Buttons
            btnSignIn = (Button) findViewById(R.id.login);
            btnSignUp = (Button) findViewById(R.id.registerbtn);
            editEmail = (EditText) findViewById(R.id.email);
            editPass = (EditText) findViewById(R.id.pword);
            btnSignIn.setOnClickListener(MyListener);


            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                    Regions.US_EAST_1// Region
            );


            ddbClient = new AmazonDynamoDBClient(credentialsProvider);


            mapper = new DynamoDBMapper(ddbClient);


            // Set OnClick Listener on SignUp button
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    /// Create Intent for SignUpActivity  abd Start The Activity
                    Intent intentSignUP = new Intent(getApplicationContext(), customerRegistration.class);
                    startActivity(intentSignUP);
                }
            });
        } catch (Exception e) {
            Log.d("Login", "onCreate: " + e.getMessage());
        }

    }

    private View.OnClickListener MyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn(v);
        }
    };

    public void signIn(View V) {

        try {


            final Dialog dialog = new Dialog(CustomerLogin.this);
            dialog.setContentView(R.layout.activity_customer_login);
            dialog.setTitle("Login");

            // get the Refferences of views
            final EditText editTextUserEmail = (EditText) dialog.findViewById(R.id.email);
            final EditText editTextPassword = (EditText) dialog.findViewById(R.id.pword);

            Button btnSignIn = (Button) dialog.findViewById(R.id.login);

            Button btnSignUP = (Button) dialog.findViewById(R.id.registerbtn);
            editTextUserEmail.setText(editEmail.getText().toString());
            editTextPassword.setText(editPass.getText().toString());

            editTextUserEmail.setEnabled(false);
            editTextPassword.setEnabled(false);
            // Set On ClickListener
            btnSignIn.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            // get The User name and Password
                            final String userEmail = editTextUserEmail.getText().toString();
                            final String password = editTextPassword.getText().toString();

                            try {


// Do something with result.
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {

                                        Customers customerToFind = new Customers();
                                        customerToFind.setEMAIL(userEmail);

                                        String queryString = password;

                                        Condition rangeKeyCondition = new Condition()
                                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                                .withAttributeValueList(new AttributeValue().withS(queryString));

                                        DynamoDBQueryExpression<Customers> queryExpression = new DynamoDBQueryExpression<Customers>()
                                                .withHashKeyValues(customerToFind)
                                                .withRangeKeyCondition("UID", rangeKeyCondition)
                                                .withConsistentRead(false);
                                        PaginatedQueryList<Customers> result = mapper.query(Customers.class, queryExpression);


                                        //if(result != null)
                                        // {
                                        // do the runnable method on the front end to display a dialog box to user about login
                                        // }

                                        getResults(result);

                                    }

                                    public void getResults(PaginatedQueryList<Customers> loadedCustomer) {
                                        Intent intent = new Intent(getApplicationContext(), customerMain.class);
                                        //Log.d("loaded", "getResults: " + loadedCustomer.get(0).getFIRSTNAME());
                                        intent.putExtra("firstname", loadedCustomer.get(0).getFIRSTNAME());
                                        intent.putExtra("lastname", loadedCustomer.get(0).getLASTNAME());
                                        //intent.putExtra("username",loadedCustomer.get(0).getUSERNAME());
                                        intent.putExtra("PID", loadedCustomer.get(0).getUID());
                                        intent.putExtra("email", loadedCustomer.get(0).getEMAIL());
                                        startActivity(intent);
                                    }
                                };


                                Thread thread = new Thread(runnable);
                                thread.start();

/*
                                if (customerUID != null || customerUID != "") {
                                    String storedPassword = customerUID.toString();
                                    if (password.equals(storedPassword)) {
                                        Toast.makeText(CustomerLogin.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();

                                        Intent intentlogin = new Intent(getApplicationContext(), customerMain.class);
                                        startActivity(intentlogin);
                                        finish();
                                    } else {
                                        Toast.makeText(CustomerLogin.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                    }
                                }*/

                            } catch (Exception e)

                            {
                                Log.d("loggingError", "onClick: " + e.getMessage());
                            }


                /*
                // fetch the Password form database for respective user name
                String storedPassword = customerHandler.getSingleEntry(userEmail);


                    // check if the Stored password matches with  Password entered by user
                    if (password.equals(storedPassword)) {
                        Toast.makeText(CustomerLogin.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        HashMap currentUser = customerHandler.getSingleUserInfo(userEmail);

                        dialog.dismiss();
                        Intent intentlogin = new Intent(getApplicationContext(), customerMain.class);
                        intentlogin.putExtra("currentEmail", userEmail);
                        intentlogin.putExtra("userCurrent", currentUser);
                        startActivity(intentlogin);
                        finish();
                    } else {
                        Toast.makeText(CustomerLogin.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    }
*/

                        }
                    }

            );


            btnSignUP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    /// Create Intent for SignUpActivity  abd Start The Activity
                    Intent intentSignUP = new Intent(getApplicationContext(), customerRegistration.class);
                    startActivity(intentSignUP);
                }
            });

            dialog.show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
