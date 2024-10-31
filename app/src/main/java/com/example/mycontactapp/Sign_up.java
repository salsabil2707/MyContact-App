package com.example.mycontactapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycontactapp.R;
import com.example.mycontactapp.Sign_in;

public class Sign_up extends AppCompatActivity {

    private Button btn_signup;
    private EditText edmail, ednom, edpassword;  // Note: Declare as private
    private Button btn_signin;
    DBHelper dbHelper = new DBHelper(this);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        btn_signup = findViewById(R.id.btn_signup_su);
        btn_signin = findViewById(R.id.btn_connecter);
        edmail = findViewById(R.id.email_signup);
        ednom = findViewById(R.id.name_signup);
        edpassword = findViewById(R.id.password_signup); // Fixed initialization

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_up.this, Sign_in.class);
                startActivity(i);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move variable assignments here
                String email = edmail.getText().toString().trim();
                String nom = ednom.getText().toString().trim();
                String password = edpassword.getText().toString().trim();

                if (validateInput(nom, email, password)) {  // Pass variables
                    boolean inserted = dbHelper.addUser(nom, password, email);
                    if (inserted) {
                        Toast.makeText(Sign_up.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Sign_up.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Validate input with parameters
            private boolean validateInput(String nom, String email, String password) {
                if (TextUtils.isEmpty(nom)) {
                    ednom.setError("Please enter your name!");
                    return false;
                } else if (TextUtils.isEmpty(password)) {
                    edpassword.setError("Please enter your password!");
                    return false;
                } else if (!isValidEmail(email)) {
                    edmail.setError("Please check your email!");
                    return false;
                }
                return true;
            }

            private boolean isValidEmail(String email) {
                return email.contains("@") && email.contains(".");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
