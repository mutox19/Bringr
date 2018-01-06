package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class orderForm extends AppCompatActivity {


    Button startOver;
    Button submit;
    EditText name;
    EditText email;
    EditText placeToShop;
    EditText whatTheyWant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);


        startOver = (Button) findViewById(R.id.btnStartOver);
        submit = (Button) findViewById(R.id.btnSubmit);



        Intent intent = getIntent();
        name = (EditText) findViewById(R.id.editTxtName);
        email = (EditText) findViewById(R.id.editTxtEmail);
        placeToShop = (EditText) findViewById(R.id.editTxtShopAt);
        whatTheyWant = (EditText) findViewById(R.id.editTxtWhatYou);


        name.setText(intent.getStringExtra("firstname") + " " + intent.getStringExtra("lastname"));
        email.setText(intent.getStringExtra("email"));
        startOver.setOnClickListener(MyListener);
        submit.setOnClickListener(MyListener);
        name.setOnClickListener(MyListener);
        email.setOnClickListener(MyListener);
        placeToShop.setOnClickListener(MyListener);
        whatTheyWant.setOnClickListener(MyListener);

    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            orderFormBtns(v);
        }
    };


    private void orderFormBtns(View v)
    {
        if(startOver.isPressed())
        {
            clearOrder(v);
        }
        if(submit.isPressed())
        {
            getOrderDetails(v);

        }
    }

    private void clearOrder(View v)
    {
        try
        {
            if (startOver.isPressed()) {
                String custName = name.getText().toString();
                if (custName.length() != 0 || !custName.isEmpty()) {
                    name.setText(" ");
                }
                String custEmail = email.getText().toString();
                if (custEmail.length() != 0 || !custEmail.isEmpty()) {
                    email.setText(" ");
                }
                String placesToGo = placeToShop.getText().toString();
                if (placesToGo.length() != 0 || !placesToGo.isEmpty()) {
                    placeToShop.setText(" ");
                }

                String orderValue = whatTheyWant.getText().toString();
                if (orderValue.length() != 0 || !orderValue.isEmpty()) {
                    whatTheyWant.setText(" ");
                }
            }
        }
        catch(Exception e)
        {
            Log.d("ClearOrder", "clearOrder: " + e.getMessage());
        }
    }
    private void getOrderDetails(View v)
    {
        try {
            if (submit.isPressed()) {

                boolean errors = false;
                Intent intent = new Intent(getApplicationContext(), orderFinal.class);
                Intent getIntent = getIntent();
                String custName = name.getText().toString();
                String custEmail = email.getText().toString();

                if (custName.length() == 0 || custName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Name Cannot be empty",
                            Toast.LENGTH_LONG).show();
                    name.requestFocus();
                     return;
                }
                /*else
                {
                    intent.putExtra("custName", custName);
                    errors = false;
                }*/
                String placesToGo = placeToShop.getText().toString();
                if (placesToGo.length() == 0 || placesToGo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sorry you have to enter where to shop",
                            Toast.LENGTH_LONG).show();
                    //editText = (EditText)findViewById(R.id.myTextViewId);
                    //editText.requestFocus();
                        errors = true;
                    placeToShop.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return;
                }/*
                else
                {
                    errors = false;
                    intent.putExtra("placesToGo",placesToGo);
                }*/

                String orderValue = whatTheyWant.getText().toString();
                if (orderValue.length() == 0 || orderValue.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sorry you have to enter what you want to buy",
                            Toast.LENGTH_LONG).show();
                    //editText = (EditText)findViewById(R.id.myTextViewId);
                    //editText.requestFocus();
                        errors = true;
                    whatTheyWant.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return;
                }
                else
                {
                    //errors = false;
                    intent.putExtra("PID",getIntent.getStringExtra("PID"));
                    intent.putExtra("orderValue", orderValue);
                    intent.putExtra("custEmail",custEmail);
                    intent.putExtra("custName", custName);
                    intent.putExtra("placesToGo",placesToGo);

                    startActivity(intent);
                }

            }
        }catch(Exception e)
        {
            Log.d("OrderForm", "getOrderDetails: " + e.getMessage());
        }
    }


}
