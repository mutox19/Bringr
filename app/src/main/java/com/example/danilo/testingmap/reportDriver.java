package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class reportDriver extends AppCompatActivity {

    AmazonDynamoDBClient ddbClient;
    Button submit;
    DynamoDBMapper mapper;
    EditText subject, complaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_driver);


        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );


        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);

        subject = (EditText) findViewById(R.id.editText);
        complaint = (EditText) findViewById(R.id.editTextAddress);
        submit = (Button) findViewById(R.id.btnComplete);

        submit.setOnClickListener(MyListener);
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
        if(submit.isPressed())
        {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    //search the database for the order so you can display the order fpr the driver

                    Intent intent = getIntent();

                    String customer = intent.getStringExtra("customer");
                    String driver = intent.getStringExtra("driver");
                    String order = intent.getStringExtra("order");
                    String customeSubject = subject.getText().toString();
                    String customerComplaint = complaint.getText().toString();


                    ReportDriversDB newReport = new ReportDriversDB();

                    newReport.setOrderID(order);
                    newReport.setDriverID(driver);
                    newReport.setCustomerID(customer);
                    newReport.setComplaint(customerComplaint);
                    newReport.setSubject(customeSubject);

                    mapper.save(newReport);


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                           Toast.makeText(getApplicationContext(), "Report Added", Toast.LENGTH_LONG).show();
                            Intent stripeIntent = new Intent(getApplicationContext(), reportSuccess.class);
                            Intent intent = getIntent();

                            String order = intent.getStringExtra("order");
                            String driver = intent.getStringExtra("driver");
                            String customer = intent.getStringExtra("customer");

                            //stripeIntent.putExtra("driver",driver);
                            //stripeIntent.putExtra("order",order);
                            stripeIntent.putExtra("customer",customer);

                            startActivity(stripeIntent);
                        }
                    });
                }

            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}
