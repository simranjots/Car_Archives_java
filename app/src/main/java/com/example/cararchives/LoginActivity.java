package com.example.cararchives;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button user,admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.user);
        admin = findViewById(R.id.admin);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(getApplicationContext(), UserLoginActivity.class);
                startActivity(i);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(getApplicationContext(), LoginDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}
