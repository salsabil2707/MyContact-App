package com.example.mycontactapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddContact extends AppCompatActivity {

    private ImageView imageViewPerson;
    private EditText editTextName, editTextEmail, editTextPhone;
    private Button buttonAddContact;
    private DBHelper dbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        imageViewPerson = findViewById(R.id.imageView_add);
        editTextName = findViewById(R.id.name_edit);
        editTextEmail = findViewById(R.id.email_edit);
        editTextPhone = findViewById(R.id.phone_edit);
        buttonAddContact = findViewById(R.id.btn_addcontact);
        dbHelper = new DBHelper(this);

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });
    }
    private void addContact() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        //controle de saisie
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isInserted = dbHelper.addContact(name, email, phone);
        if (isInserted) {
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
            clearFields();
            finish();
        } else {
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
    }


}
