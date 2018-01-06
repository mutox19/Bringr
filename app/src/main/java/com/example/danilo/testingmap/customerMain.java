package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class customerMain extends AppCompatActivity {

    Button btnLogout, btnProcessOrder, btnChangePass;
    EditText customerInfo, firstname, lastname, email;
    TextView txtDay;
    DatabaseCustomer customerDatabase;
    String pid;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        customerDatabase = new DatabaseCustomer(this);
        btnProcessOrder = (Button) findViewById(R.id.btnStartWork);
        btnChangePass = (Button) findViewById(R.id.btnchangepass);
        txtDay = (TextView) findViewById(R.id.txtDay);
        btnLogout =  (Button)findViewById(R.id.logout);
        customerInfo = (EditText)findViewById(R.id.txtInfo);
        firstname = (EditText)findViewById(R.id.fname);
        lastname = (EditText)findViewById(R.id.lname2);
        email = (EditText)findViewById(R.id.email);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        intent = getIntent();
        pid = intent.getStringExtra("PID");
        btnLogout.setOnClickListener(MyListener);
        btnChangePass.setOnClickListener(MyListener);
        btnProcessOrder.setOnClickListener(MyListener);

        txtDay.setText(dateFormat.format(date));
        customerInfo.setText(intent.getStringExtra("firstname") + " " + intent.getStringExtra("lastname") + " information");
        customerInfo.setEnabled(false);
        customerInfo.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));
        firstname.setText(intent.getStringExtra("firstname"));
        firstname.setEnabled(false);
        lastname.setText(intent.getStringExtra("lastname"));
        lastname.setEnabled(false);
        email.setText(intent.getStringExtra("email"));
        email.setEnabled(false);

    }


    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            CustomerChoice(v);
        }
    };

    private void CustomerChoice(View v)
    {
        if(btnLogout.isPressed())
        {
            //loginDataBaseAdapter.logoutUser(this);
            Intent logOutIntent = new Intent(getApplicationContext(), CustomerLogin.class);
            startActivity(logOutIntent);
            finish();
        }

        if(btnProcessOrder.isPressed())
        {
            //loginDataBaseAdapter.logoutUser(this);
            Intent  processOrder = new Intent(getApplicationContext(), orderForm.class);
            processOrder.putExtra("firstname",intent.getStringExtra("firstname"));
            processOrder.putExtra("lastname",intent.getStringExtra("lastname"));
            processOrder.putExtra("email",intent.getStringExtra("email"));
            processOrder.putExtra("PID",pid);
            startActivity(processOrder);
            finish();
        }
        if(btnChangePass.isPressed())
        {
            Intent changePass = new Intent(getApplicationContext(), orderForm.class);
            changePass.putExtra("firstname",intent.getStringExtra("firstname"));
            changePass.putExtra("lastname",intent.getStringExtra("lastname"));
            changePass.putExtra("email",intent.getStringExtra("email"));
            changePass.putExtra("PID",pid);
            startActivity(changePass);
            finish();
        }

    }
}
