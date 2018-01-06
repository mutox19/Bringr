package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverCarReg extends AppCompatActivity {

    String firstname , lastname , email, password, address;
    EditText brandOfCar, modelOfCar, plateNum, phoneNum;
    Button btnReg, btnBack;
    DynamoDBMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_reg);

        Intent intent = getIntent();

        firstname = intent.getStringExtra("firstname");
        lastname =intent.getStringExtra("lastname");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        address = intent.getStringExtra("address");
        brandOfCar = (EditText)findViewById(R.id.editTxtBOCar);
        modelOfCar = (EditText)findViewById(R.id.editTxtMOCar);
        plateNum = (EditText)findViewById(R.id.editTxtPlate);
        phoneNum = (EditText)findViewById(R.id.editTxtPhone);

        btnReg = (Button)findViewById(R.id.btnReg);
        btnBack = (Button)findViewById(R.id.btnBack);

        btnReg.setOnClickListener(MyListener);
        btnBack.setOnClickListener(MyListener);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1// Region
        );
        //this is the old credntials
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);
    }


    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            OnDriverReg(v);
        }
    };
    public void OnDriverReg(View v)
    {
        if(btnReg.isPressed())
        {
            //register driver and maybe send notification that the driver needs to verify his account
            //to be able to start getting orders.
            try {
                if(modelOfCar.getText().toString().equals("") || brandOfCar.equals("") || plateNum.equals("") )
                {
                    Toast.makeText(getApplicationContext(), "a field is empty fill out all fields", Toast.LENGTH_LONG).show();
                    //return;
                }
                else
                {
                    //get the registration id from firebase
                    String regid = FirebaseInstanceId.getInstance().getToken();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    //get current date time with Date()
                    Date date = new Date();


                    //create a new driver object
                    final Driverdb newDriver = new Driverdb();

                    newDriver.setFirstName(firstname);
                    newDriver.setLastName(lastname);
                    newDriver.setEmail(email);
                    newDriver.setAddress(address);
                    newDriver.setUid(password);
                    newDriver.setFid(regid);
                    newDriver.setBrandofCar(brandOfCar.getText().toString());
                    newDriver.setCarModel(modelOfCar.getText().toString());
                    newDriver.setPlate(plateNum.getText().toString());
                    newDriver.setPhoneNum(phoneNum.getText().toString());
                    newDriver.setCreatedAt(dateFormat.format(date));
                    newDriver.setVerified(false);

                    Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                //save the new driver to the database
                                mapper.save(newDriver);
                            }
                        };

                    //run the background thread
                        Thread thread = new Thread(runnable);
                        thread.start();

                    //go to driver registered screen on success
                    Intent intent = new Intent(getApplicationContext(), driverReg.class);
                    startActivity(intent);
                }

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Sorry Insert fail try - again soon", Toast.LENGTH_LONG).show();
                Log.d("InsertFail", "onReg: " + e.getMessage());
            }
        }
        if(btnBack.isPressed())
        {
            Intent intent = new Intent(getApplicationContext(), DriverRegistration.class);
            intent.putExtra("firstname",firstname);
            intent.putExtra("lastname",lastname);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("address",address);
            startActivity(intent);
        }
    }
    ///this is the page that will actuall register the driver and in time we can implement a notification
    // to let driver know that he must send in a picture of his license to be able to start working

}
