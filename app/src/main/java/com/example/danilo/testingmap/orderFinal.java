package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class orderFinal extends AppCompatActivity {

    Button btnSubmit;
    Button btnGoBack;

    EditText editName;
    EditText editEmail;
    EditText editPlaceToGo;
    EditText editwhatTheyWant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_final);


        btnGoBack = (Button) findViewById(R.id.btnStartOver);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);


        editName = (EditText) findViewById(R.id.editTxtName);
        editEmail = (EditText) findViewById(R.id.editTxtEmail);
        editPlaceToGo = (EditText) findViewById(R.id.editTxtShopAt);
        editwhatTheyWant = (EditText) findViewById(R.id.editTxtWhatYou);

        editName.setEnabled(false);
        editEmail.setEnabled(false);
        editPlaceToGo.setEnabled(false);
        editwhatTheyWant.setEnabled(false);

        btnGoBack.setOnClickListener(MyListener);
        btnSubmit.setOnClickListener(MyListener);

        Intent intent = getIntent();
        String custName = intent.getStringExtra("custName");
        String custEmail = intent.getStringExtra("custEmail");
        String placeToGo = intent.getStringExtra("placesToGo");
        String whatTheyWant = intent.getStringExtra("orderValue");


        if(intent.getStringExtra("custName") != null)
        {
            editName.setText(custName);
        }
        if(intent.getStringExtra("custEmail") != null)
        {
            editEmail.setText(custEmail);
        }
        if(intent.getStringExtra("placesToGo") != null)
        {
            editPlaceToGo.setText(placeToGo);
        }
        if(intent.getStringExtra("orderValue") != null)
        {
            editwhatTheyWant.setText(whatTheyWant);
        }

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
        try
        {
            if(btnSubmit.isPressed())
            {
                Intent intent1 = getIntent();
                String custName = intent1.getStringExtra("custName");
                String custEmail = intent1.getStringExtra("custEmail");
                String placeToGo = intent1.getStringExtra("placesToGo");
                String whatTheyWant = intent1.getStringExtra("orderValue");

                Intent intent = new Intent(getApplicationContext(), addressInfo.class);
                intent.putExtra("PID", intent1.getStringExtra("PID"));
                intent.putExtra("custName", custName);
                intent.putExtra("placesToGo",placeToGo);
                intent.putExtra("custEmail",custEmail);
                intent.putExtra("orderValue",whatTheyWant);
                startActivity(intent);
            }
            if(btnGoBack.isPressed())
            {

            }
        }
        catch(Exception e)
        {

        }
    }
}
