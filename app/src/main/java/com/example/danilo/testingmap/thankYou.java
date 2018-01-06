package com.example.danilo.testingmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class thankYou extends AppCompatActivity {

    Button mainMenu, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        mainMenu = (Button) findViewById(R.id.btnMainMenu);
        exit = (Button) findViewById(R.id.btnExitOut);

        mainMenu.setOnClickListener(MyListener);
        exit.setOnClickListener(MyListener);

    }

    private View.OnClickListener MyListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ThankYouButtons(v);
        }
    };

    private void ThankYouButtons(View v)
    {
        if(mainMenu.isPressed())
        {
            //loginDataBaseAdapter.logoutUser(this);
            Intent mainMenuIntent = new Intent(getApplicationContext(), customerMain.class);
            startActivity(mainMenuIntent);
            finish();
        }
        if(exit.isPressed())
        {
            finish();
        }

    }

}
