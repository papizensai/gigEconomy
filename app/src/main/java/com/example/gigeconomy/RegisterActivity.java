package com.example.gigeconomy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity
{
    // Instantiating text and button fields
    private EditText nameText;
    private EditText regEmail;
    private EditText regPass;
    private EditText regConfirmPass;
    private Button regBtn;
    private Button reg_login_btn;

    // auth variable , shortens code
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //auth variable
        mAuth = FirebaseAuth.getInstance();

        // linking fields to buttons and texts
        nameText = findViewById(R.id.nameText);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPassword);
        regConfirmPass = findViewById(R.id.regConfirmPass);
        regBtn = findViewById(R.id.regBtn);
        reg_login_btn = findViewById(R.id.reg_login_btn);

        // sends user to login page
        reg_login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent loginIntent = new Intent (RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });


        // registering on button click
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { // grabbing fields and turning them to strings
                String name = nameText.getText().toString();
                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String confirmPass = regConfirmPass.getText().toString();

                // checking to make sure all fields are populated and passwords match
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(confirmPass))
                // if passwords match create user
                {if (pass.equals(confirmPass)){
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error: " + errorMessage , Toast.LENGTH_LONG).show();


                            }

                        }
                    });

                } else {
                        Toast.makeText(RegisterActivity.this, "Confirm Password and Password field match!", Toast.LENGTH_LONG).show();
                }

                }

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // on start check for signed in user, if user send to main
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            sendToMain();
        }
    }

    private void sendToMain()
    {
        // sends to main
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
