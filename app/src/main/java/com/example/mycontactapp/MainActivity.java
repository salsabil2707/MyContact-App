package com.example.mycontactapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        checkUserSession(); // Check if the user is already signed in

        setContentView(R.layout.activity_main);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Sign_up.class);
                startActivity(i);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Sign_in.class);
                startActivity(i);
            }
        });
    }

    private void checkUserSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("remember_me", false);

        if (isRemembered) {
            // If the user chose to be remembered, go directly to Accueil
            Intent intent = new Intent(MainActivity.this, Accueil.class);
            startActivity(intent);
            finish(); // Close the MainActivity
        }
    }
}
