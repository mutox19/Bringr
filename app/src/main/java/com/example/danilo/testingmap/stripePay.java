package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.wallet.Cart;
import com.google.firebase.iid.FirebaseInstanceId;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.CardException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

public class stripePay extends AppCompatActivity {

    final String PUBLISHABLE_STRIPE_KEY = "pk_test_azvANk7rAPMzhIJYyDOw5QO3";
    String driver, customer, order;
    EditText month, year, cvc, cardNumber;
    Button submit;
    String TAG = "stripePay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_pay);


        Intent getIntent = getIntent();

        if(getIntent.getStringExtra("order") != null)
        {
            order = getIntent.getStringExtra("order");
        }
        if(getIntent.getStringExtra("customer") != null)
        {
            driver = getIntent.getStringExtra("customer");
        }
        if(getIntent.getStringExtra("driver") != null)
        {
            customer = getIntent.getStringExtra("driver");
        }

        month = (EditText)findViewById(R.id.month);
        year = (EditText)findViewById(R.id.year);
        cvc = (EditText)findViewById(R.id.cvc);
        submit = (Button)findViewById(R.id.submitButton);
        cardNumber = (EditText)findViewById(R.id.cardNumber);

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
        if (submit.isPressed())
        {

            String cardNum = cardNumber.getText().toString();
            int monthInt = Integer.parseInt(month.getText().toString());
            int yearInt = Integer.parseInt(year.getText().toString());
            String cvcString = cvc.getText().toString();


            final Card card = new Card(cardNum,monthInt,yearInt,cvcString);
            boolean validation = card.validateCard();

            //Stripe stripe = new Stripe("pk_test_azvANk7rAPMzhIJYyDOw5QO3");
            if (validation) {
                //startProgress();

                new Stripe().
                        createToken(
                        card,
                        PUBLISHABLE_STRIPE_KEY,
                        new TokenCallback() {
                            public void onSuccess(final Token token) {
                                // you can get transcation token

                                Log.d(TAG, "onSuccess: CARD CHARGED SUCCESSFULLY"  + token.getId());




                                //IN HERE YOU CAN SEND A MESSAGE TO USE SAYING THERE CARD WAS CHARGED
                                //THEN YOU CAN RETURN USERS TO CUSTOMER MAIN PAGE
                                //ALSO IN HERE YOU NEED TO SEND NOTIFICATION TO DRIVER SAYING THAT THE
                                //CUSTOMER WAS CHARGED SUCCESSFULLY
                                //BackgroundWorker backgroundWorker = new BackgroundWorker(stripePay.this);
                                //backgroundWorker.execute("StripePaySuccess",driverFind,customerFind,orderFind,customerToken, "Card was charged", "Customers Card was charged success");
                                //also updae the order status to complete.
                                //redirec customer to customer main


                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent backgroundIntent = getIntent();


                                        String customerToken = token.getId();
                                        String driverFind = backgroundIntent.getStringExtra("driver");
                                        String customerFind = backgroundIntent.getStringExtra("customer");
                                        String orderFind = backgroundIntent.getStringExtra("order");
                                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                                        //Log.d(TAG, "run: " + driverFind + "customer: " + customerFind + "order: " + orderFind + "token: " + customerToken);
                                        backgroundWorker.doInBackground("StripePaySuccess",driverFind,customerFind,orderFind,customerToken, "Card was charged", "Customers Card was charged success");

                                    }
                                };

                                Thread thread = new Thread(runnable);
                                thread.start();

                            }
                            public void onError(Exception error) {
                                handleError(error.getLocalizedMessage());
                                Log.d(TAG, "onError: CARD NOT CHARGED ERROR!!");

                                //in here you can notify customer on screen that the cardwas not a success
                                //also you need to send notification to driver that the card was not charged successfully
                                //BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                                //backgroundWorker.doInBackground("StripePayFailure", "driver", "customer", "orderid","Card Not Charged", "Customers Card failed");
                                //finishProgress();
                            }
                        });
            } else if (!card.validateNumber()) {
                handleError("The card number that you entered is invalid");
            } else if (!card.validateExpiryDate()) {
                handleError("The expiration date that you entered is invalid");
            } else if (!card.validateCVC()) {
                handleError("The CVC code that you entered is invalid");
            } else {
                handleError("The card details that you entered are invalid");
            }
        }
    }

    private void handleError(String errorMsg)
    {
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
    }
}
