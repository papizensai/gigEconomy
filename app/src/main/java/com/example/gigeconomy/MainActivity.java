package com.example.gigeconomy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private Button logout;
    private EditText name;
    private Button tripBttn;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logoutBttn);

        tripBttn = findViewById(R.id.tripBttn);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //Checks for signed in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // no signed in user send to login page
        if(currentUser == null)
        {
            // beams user to the login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // temp logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logOut();
            }
        });

        tripBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tripIntent = new Intent (MainActivity.this, TripActivity.class);
                startActivity(tripIntent);
            }
        });
    }


    // signs user outs and sends to login
    private void logOut()
    {
        mAuth.signOut();
        Intent loginIntent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
}

