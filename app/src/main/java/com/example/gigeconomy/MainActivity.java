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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // declaring login fields and buttons
    private Button logout, tripBttn;
    private EditText nameT, totalP;
    private String driver;

    // auth variable , shortens code
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking fields to buttons and texts
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logoutBttn);
        tripBttn = findViewById(R.id.tripBttn);
        nameT = findViewById(R.id.nameT);
        totalP = findViewById(R.id.totalP);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Checks for signed in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        // if no signed in user send to login page, else display name
        if (currentUser == null) {
            // beams user to the login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // not putting this in this else statement was giving me a null pointer and killing me!!
            // coding 101 !!!
            getName();
        }

        tripBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // updatepay();
            }
        });

        // logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


    }



    // signs user out and sends to login
    private void logOut()
    {
        mAuth.signOut();
        Intent loginIntent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }


    // grabs name from database by setting a listener for the database then looking for hash posts
    private void getName()
    {
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            { if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 )
            {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                 if (map.get("Name")!= null)
                 {
                    driver = map.get("Name").toString();
                    nameT.setText(driver);
                 }
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    // method for updating pay in database
    //
    private void updatepay()
    {
        final String payT = totalP.getText().toString();
        double pay = Double.parseDouble(payT);

        if (!TextUtils.isEmpty((payT)))
        {
            // Updates database with name and email
            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

            Map payPost = new HashMap();
            payPost.put("Pay", pay);
            current_user_db.setValue(payPost);
        }
    }



}

