package com.example.mycontactapp;

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

public class EditContact extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPhone;
    private Button buttonUpdateContact;
    private DBHelper dbHelper;
    private int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact);

        editTextName = findViewById(R.id.name_edit);
        editTextEmail = findViewById(R.id.email_edit);
        editTextPhone = findViewById(R.id.phone_edit);
        buttonUpdateContact = findViewById(R.id.btn_editcontact);
        dbHelper = new DBHelper(this);

        contactId = getIntent().getIntExtra("CONTACT_ID", -1);
        String name = getIntent().getStringExtra("CONTACT_NAME");
        String email = getIntent().getStringExtra("CONTACT_EMAIL");
        String phone = getIntent().getStringExtra("CONTACT_PHONE");

        editTextName.setText(name);
        editTextEmail.setText(email);
        editTextPhone.setText(phone);

        buttonUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContact();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private void updateContact() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isUpdated = dbHelper.updateContact(contactId, name, email, phone);
        if (isUpdated) {
            Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show();
        }
    }
}