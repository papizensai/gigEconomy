package com.example.gigeconomy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    // instantiating login fields and buttons
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginbtn;
    private Button loginRegbtn;
    private ProgressBar loginProgress;


    // auth variable , shortens code
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // making life easier
        mAuth = FirebaseAuth.getInstance();

        // linking fields to buttons and texts
        loginEmailText = findViewById(R.id.regEmail);
        loginPassText = findViewById(R.id.regPassword);
        loginbtn = findViewById(R.id.regBtn);
        loginRegbtn = findViewById(R.id.login_reg_btn);
        loginProgress = findViewById(R.id.progressBar);

            // sends user to register page
            loginRegbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent regIntent = new Intent (LoginActivity.this, RegisterActivity.class);
                    startActivity(regIntent);
                }
            });



            // begins login process on click of login button
            loginbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // on click of login if email and pass are not empty login
                    String loginEmail = loginEmailText.getText().toString();
                    String loginPassword = loginPassText.getText().toString();

                    if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword))
                    {
                        loginProgress.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    sendToMainAct();

                                } else
                                    {
                                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();

                                }

                            }
                        });
                    }




                }
            });


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        sendToMainAct();


    }

    private void sendToMainAct()
    {
        // Checking for user on start if user is signed is, sends to main page
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Intent mainIntent = new Intent (LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
