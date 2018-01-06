package com.example.danilo.testingmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.R.attr.password;

public class DriverRegistration extends AppCompatActivity {
// get Instance  of Database Adapter

    EditText editTextUserName,editTextPassword,editTextConfirmPassword, firstName, lastName,editTxtaddress, email, phoneNum;
    Button btnCreateAccount;
    Button backToLogin;
    String deviceId;
    //final Runnable runnable = null;


    DynamoDBMapper mapper;
    FirebaseDatabase firebaseDatabase;
    FirebaseHelper firebaseHelper;
    Driverdb dDB;
    DatabaseReference fireBaseRef;
    //final  static  String  DB_URL = "https://fooddeliver-1294.firebaseio.com/";
    //DatabaseHandler loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);


        try {

            btnCreateAccount = (Button)findViewById(R.id.register);
            backToLogin = (Button)findViewById(R.id.bktologin);

            //editTextUserName = (EditText)findViewById(R.id.uname);
            editTextPassword = (EditText)findViewById(R.id.pword);
            firstName = (EditText)findViewById(R.id.fname);
            lastName = (EditText)findViewById(R.id.editTxtlastName);
            email = (EditText)findViewById(R.id.email);
            editTextConfirmPassword = (EditText)findViewById(R.id.confirmpword);
            editTxtaddress = (EditText)findViewById(R.id.editTxtAddress);


            btnCreateAccount.setOnClickListener(MyListener);
            backToLogin.setOnClickListener(MyListener);


            //new credentials
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                    Regions.US_EAST_1// Region
            );
            //this is the old credntials
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

             mapper = new DynamoDBMapper(ddbClient);
                     //AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
            //mapper.save(newDriver);

        }
        catch (Exception e)
        {
            Log.d("error", "onCreate: " + e.getMessage());
        }

/*
        firebaseDatabase = FirebaseDatabase.getInstance();
         fireBaseRef = firebaseDatabase.getReference("fooddeliver-1294");
        //firebaseDatabase = new
        dDB = new Driverdb();
        firebaseHelper = new FirebaseHelper(fireBaseRef);

*/

        /*
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://localhost:8080/android_login/login.php");
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();

             reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null)
            {
                stringBuffer.append(line);
            }
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally {

            if(connection != null)
            {
                connection.disconnect();
            }

            try {
                if(reader != null)
                {
                    reader.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*
        // get Instance  of Database Adapter
        loginDataBaseAdapter = new DatabaseHandler(this);
        //loginDataBaseAdapter=loginDataBaseAdapter.open();

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Get References of Views
        editTextUserName = (EditText)findViewById(R.id.uname);
        editTextPassword = (EditText)findViewById(R.id.pword);
        firstName = (EditText)findViewById(R.id.fname);
        lastName = (EditText)findViewById(R.id.lname);
        email = (EditText)findViewById(R.id.email);
        editTextConfirmPassword = (EditText)findViewById(R.id.confirmpword);

        btnCreateAccount = (Button)findViewById(R.id.register);
        backToLogin = (Button)findViewById(R.id.bktologin);
        backToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(), login.class);
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
                if(userName.equals("")||password.equals("")||confirmPassword.equals("")||
                        userFirstName.equals("")||userLastName.equals("") || userEmail.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    // Save the Data in Database
                    loginDataBaseAdapter.insertEntry(userFirstName, userLastName, userEmail, userName, confirmPassword, deviceId);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created, you will be redirected to login ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);

                }
            }
        }); */
    }



    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onReg(v);
        }
    };

    public void onReg(View V)
    {
        if(btnCreateAccount.isPressed())
        {
            try {
                    if(editTxtaddress.getText().toString().equals("") || editTextPassword.getText().toString().equals("") ||
                            editTextConfirmPassword.getText().toString().equals("") || firstName.getText().toString().equals("") ||
                            lastName.getText().toString().equals("") || email.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    else if(!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(), "passwords field must match", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                       // UUID Guid = java.util.UUID.randomUUID();
                        //Log.d("ID:", "onCreate: " + Guid);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        //get current date time with Date()
                        Date date = new Date();
                        //System.out.println(dateFormat.format(date));

                        final Driverdb newDriver = new Driverdb();
                        //newDriver.setUserId(Guid.toString());
                        newDriver.setFirstName(firstName.getText().toString());
                        newDriver.setLastName(lastName.getText().toString());
                        //newDriver.setUsername(editTextUserName.getText().toString());
                        newDriver.setEmail(email.getText().toString());
                        //newDriver.setCreatedAt(dateFormat.format(date));
                        newDriver.setUid(editTextPassword.getText().toString());

                        /*Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                mapper.save(newDriver);
                                //Driverdb selectedDriver = mapper.load(Driverdb.class, "5465d4gsdseda");
                                //Log.d("oldDriver", "run: " + selectedDriver.getFirstName() + " " + selectedDriver.getLastName() + " " + selectedDriver.getEmail());
                            }
                        };

                        Thread thread = new Thread(runnable);
                        thread.start();
*/
                        //Toast.makeText(getApplicationContext(), "Driver Registration Complete", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), DriverCarReg.class);
                        intent.putExtra("firstname",firstName.getText().toString());
                        intent.putExtra("lastname",lastName.getText().toString());
                        intent.putExtra("email",email.getText().toString());
                        intent.putExtra("password",editTextPassword.getText().toString());
                        intent.putExtra("address",editTxtaddress.getText().toString());
                        startActivity(intent);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Sorry Insert fail try - again soon", Toast.LENGTH_LONG).show();
                    Log.d("InsertFail", "onReg: " + e.getMessage());
                }


            //this access php scripts.
            /*
            String user = editTextUserName.getText().toString();
            String userPass = editTextPassword.getText().toString();
            String userFirst = firstName.getText().toString();
            String userLast = lastName.getText().toString();
            String userEmail = email.getText().toString();
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, user, userPass, userFirst, userLast, userEmail);
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);*/
        }
        if(backToLogin.isPressed())
        {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
        }

    }


/*
    private void DriverButtons(View v)
    {/*
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        // Get References of Views
        editTextUserName = (EditText)findViewById(R.id.uname);
        editTextPassword = (EditText)findViewById(R.id.pword);
        firstName = (EditText)findViewById(R.id.fname);
        lastName = (EditText)findViewById(R.id.lname);
        email = (EditText)findViewById(R.id.email);
        editTextConfirmPassword = (EditText)findViewById(R.id.confirmpword);

        if(btnCreateAccount.isPressed())
        {
            try
            {
                if(editTextUserName.equals("")||editTextPassword.equals("")||editTextConfirmPassword.equals("")||
                        firstName.equals("")||lastName.equals("") || email.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(editTextPassword.getText().toString() != editTextConfirmPassword.getText().toString())
                {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }else
                {
                    /*dDB.setUsername(editTextUserName.getText().toString());
                    dDB.setFirstName(firstName.getText().toString());
                    dDB.setLastName(lastName.getText().toString());
                    dDB.setEmail(email.getText().toString());
                    dDB.setUid(editTextConfirmPassword.getText().toString());
                    dDB.setCreatedAt(date);

                    //fireBaseRef.child("DriverReg").setValue(dDB);
                    firebaseHelper.SavedDriverData(dDB);

                    Toast.makeText(getApplicationContext(), "Account Successfully Created, you will be redirected to login ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
        if(backToLogin.isPressed())
        {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
        }
    } */
    private  void InitalizeFirebase(){


    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        //loginDataBaseAdapter.close();
    }
}
