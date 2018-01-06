package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class ratings extends AppCompatActivity {

    String orderId, driverId;
    AmazonDynamoDBClient ddbClient;
    Button submit;
    DynamoDBMapper mapper;
    CheckBox itemsDeliveredYes, itemsDeliveredNo , deliveredInTimeYes, deliveredInTimeNo;
    String itemDel, delInTime;
    RatingBar ratingBarVal;
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);


        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:afc790f8-8796-4975-a36e-f7f201dbcc57", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );


        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);
        Intent getIntent = getIntent();

        if(getIntent.getStringExtra("order") != null)
        {
            orderId = getIntent.getStringExtra("order");
        }
        if(getIntent.getStringExtra("driver") != null)
        {
            driverId = getIntent.getStringExtra("driver");
        }

        itemsDeliveredYes = (CheckBox)findViewById(R.id.checkBox2);
        itemsDeliveredYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemsDeliveredNo.isChecked())
                {
                    itemsDeliveredNo.setChecked(false);
                }
            }
        });
        itemsDeliveredNo = (CheckBox)findViewById(R.id.checkBox);
        itemsDeliveredNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemsDeliveredYes.isChecked())
                {
                    itemsDeliveredYes.setChecked(false);
                }
            }
        });


        deliveredInTimeNo = (CheckBox)findViewById(R.id.checkBox3);
        deliveredInTimeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deliveredInTimeYes.isChecked())
                {
                    deliveredInTimeYes.setChecked(false);
                }
            }
        });
        deliveredInTimeYes = (CheckBox)findViewById(R.id.checkBox4);
        deliveredInTimeYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deliveredInTimeNo.isChecked())
                {
                    deliveredInTimeNo.setChecked(false);
                }
            }
        });

        ratingBarVal = (RatingBar)findViewById(R.id.ratingBar);
        description = (EditText)findViewById(R.id.editText2);

        submit = (Button)findViewById(R.id.button);

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


    private  void OrderFinal(View v)
    {
        if(submit.isPressed())
        {

            if(deliveredInTimeNo.isChecked())
            {
                delInTime = "No";
            }
            else if(deliveredInTimeYes.isChecked())
            {
                delInTime = "Yes";
            }

            if(itemsDeliveredYes.isChecked())
            {
                itemDel = "Yes";
            }
            else if(itemsDeliveredNo.isChecked())
            {
                itemDel = "No";
            }
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    //search the database for the order so you can display the order fpr the driver

                    Intent intent = getIntent();

                    String customer = intent.getStringExtra("customer");
                    String driver = intent.getStringExtra("driver");
                    String order = intent.getStringExtra("order");
                    String desc = description.getText().toString();
                    int rating = ratingBarVal.getNumStars();
                    String itemsDelivered = itemDel;
                    String DeliveredInTime = delInTime;


                    RatingsDB newRating = new RatingsDB();
                    newRating.setCustomerID(customer);
                    newRating.setDriverID(driver);
                    newRating.setOrderID(order);
                    newRating.setDescription(desc);
                    newRating.setStarRating(rating);
                    newRating.setDeliveredAllItems(itemsDelivered);
                    newRating.setDeliveredInTime(DeliveredInTime);

                    mapper.save(newRating);


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Ratings Added", Toast.LENGTH_LONG).show();
                            Intent stripeIntent = new Intent(getApplicationContext(), stripePay.class);
                            Intent intent = getIntent();

                            String order = intent.getStringExtra("order");
                            String driver = intent.getStringExtra("driver");
                            String customer = intent.getStringExtra("customer");

                            Log.d("Ratings", "run: " + driver + "order: " + order + "customer: " + customer);
                            stripeIntent.putExtra("driver",driver);
                            stripeIntent.putExtra("order",order);
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
