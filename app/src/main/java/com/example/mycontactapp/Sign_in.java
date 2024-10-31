package com.example.mycontactapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sign_in extends AppCompatActivity {
    EditText edmail, edpassword;
    private Button btn_signin;
    private Button btn_signup;
    DBHelper dbHelper;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        btn_signin = findViewById(R.id.btn_signin_si);
        btn_signup = findViewById(R.id.btn_signup_si);
        edmail = findViewById(R.id.email_signin);
        edpassword = findViewById(R.id.password_signin);
        checkBox = findViewById(R.id.checkbox_signin);

        dbHelper = new DBHelper(this);

        // Load saved credentials
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);
        boolean isRemembered = sharedPreferences.getBoolean("remember_me", false);

        if (isRemembered && savedEmail != null && savedPassword != null) {
            edmail.setText(savedEmail);
            edpassword.setText(savedPassword);
            // Automatically log in
            proceedToNextActivity();
        }

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edmail.getText().toString().trim();
                String password = edpassword.getText().toString().trim();

                if (validateInput(email, password)) {
                    // Check if the user exists in the database
                    if (dbHelper.checkUser(email, password)) {
                        Toast.makeText(Sign_in.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                        // Save credentials if checkbox is checked
                        if (checkBox.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.putBoolean("remember_me", true);
                            editor.apply();
                        } else {
                            // Clear saved credentials if checkbox is unchecked
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                        }

                        // Proceed to the next activity
                        proceedToNextActivity();
                    } else {
                        Toast.makeText(Sign_in.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean validateInput(String email, String password) {
                if (TextUtils.isEmpty(email)) {
                    edmail.setError("Please enter your email!");
                    return false;
                } else if (TextUtils.isEmpty(password)) {
                    edpassword.setError("Please enter your password!");
                    return false;
                }
                return true;
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_in.this, Sign_up.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void proceedToNextActivity() {
        Intent intent = new Intent(Sign_in.this, Accueil.class);
        startActivity(intent);
        finish(); // Close the sign-in activity
    }
}
