package com.example.mycontactapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CONTACTS = "contacts";

    // Users Table Columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Contacts Table Columns
    public static final String COLUMN_CONTACT_ID = "id";
    public static final String COLUMN_CONTACT_NAME = "name";
    public static final String COLUMN_CONTACT_EMAIL = "email";
    public static final String COLUMN_CONTACT_PHONE = "phone_number";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the tables when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);

        // Create Contacts Table
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + " ("
                + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
                + COLUMN_CONTACT_EMAIL + " TEXT, "
                + COLUMN_CONTACT_PHONE + " TEXT)";
        db.execSQL(createContactsTable);
    }

    // Handle db upgrades
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Add a new user to the database
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Add a new contact to the database
    public boolean addContact(String name, String email, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_EMAIL, email);
        values.put(COLUMN_CONTACT_PHONE, phoneNumber);

        long result = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return result != -1;
    }
    public boolean deleteContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts", "id = ?", new String[]{String.valueOf(contactId)}) > 0;
    }


    // Get all users from the database
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Get all contacts from the database
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_EMAIL)));
                contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_PHONE)));  // Updated here if necessary
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contactList;
    }
    public boolean updateContact(int id, String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_EMAIL, email);
        values.put(COLUMN_CONTACT_PHONE, phone);

        int rowsUpdated = db.update(TABLE_CONTACTS, values, COLUMN_CONTACT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated > 0;
    }

    public Contact getContactById(int contactId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, COLUMN_CONTACT_ID + " = ?", new String[]{String.valueOf(contactId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact contact = new Contact();
            contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_ID)));
            contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NAME)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_EMAIL)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_PHONE)));
            cursor.close();
            return contact;
        }
        return null;
    }



    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        // If the cursor has results, the user exists
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}
