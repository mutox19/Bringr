package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class driverReg extends AppCompatActivity {

    Button login, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg);


        login = (Button)findViewById(R.id.btnMainMenu);
        exit = (Button)findViewById(R.id.btnExit);

        login.setOnClickListener(MyListener);
        exit.setOnClickListener(MyListener);
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
        if(login.isPressed())
        {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        if(exit.isPressed())
        {
            finish();
        }
    }

}
