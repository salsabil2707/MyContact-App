package com.example.mycontactapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Accueil extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private DBHelper dbHelper;
    private List<Contact> contactList;
    private List<Contact> filteredContactList; // List for filtered contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Initialize DBHelper and RecyclerView
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recycler1);
        SearchView searchView = findViewById(R.id.search_view);

        // Set up the RecyclerView with an empty list initially
        contactList = new ArrayList<>();
        filteredContactList = new ArrayList<>(); // Initialize filtered list
        adapter = new ContactAdapter(filteredContactList, dbHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Button to add a new contact
        Button btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(view -> {
            Intent intent = new Intent(Accueil.this, AddContact.class);
            startActivity(intent);
        });

        // Sign out button
        Button btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(v -> signOut());

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });
    }

    private void signOut() {
        // Clear saved user preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to MainActivity
        Intent intent = new Intent(Accueil.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void filterContacts(String query) {
        filteredContactList.clear();
        if (query.isEmpty()) {
            filteredContactList.addAll(contactList);
        } else {
            for (Contact contact : contactList) {
                if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredContactList.add(contact);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fetch the contacts again when the activity is resumed
        contactList.clear(); // Clear the old data
        contactList.addAll(dbHelper.getAllContacts()); // Fetch updated contact list
        filteredContactList.clear();
        filteredContactList.addAll(contactList); // Initialize the filtered list
        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}
