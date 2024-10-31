package com.example.mycontactapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contactList;
    private DBHelper dbHelper;


    // Constructor to accept a list of contacts
    public ContactAdapter(List<Contact> contactList, DBHelper dbHelper) {

        this.contactList = contactList;
        this.dbHelper=dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtContact.setText(contact.getPhone());

        holder.btn_delete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this contact ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call delete function from DBHelper
                        if (dbHelper.deleteContact(contact.getId())) {
                            // Remove contact from list and notify adapter
                            contactList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, contactList.size());
                            Toast.makeText(holder.itemView.getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Failed to delete contact", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null) // Just dismiss the dialog
                    .show();
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditContact.class);
                intent.putExtra("CONTACT_ID", contact.getId());
                intent.putExtra("CONTACT_NAME", contact.getName());
                intent.putExtra("CONTACT_EMAIL", contact.getEmail());
                intent.putExtra("CONTACT_PHONE", contact.getPhone());
                v.getContext().startActivity(intent);
            }
        });
        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(contact.getPhone(), v.getContext());
            }
        });

    }
    private void makeCall(String phoneNumber, Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if it hasn't been granted
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        context.startActivity(callIntent);
    }

    @Override
    public int getItemCount() {
        return contactList.size(); // Return the size of the contact list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView txtName;
        public TextView txtContact;
        public ImageView btn_delete;
        public ImageView btn_edit;
        public ImageView btn_call;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img1);
            txtName=itemView.findViewById(R.id.txt1);
            txtContact=itemView.findViewById(R.id.txt2);
            btn_delete=itemView.findViewById(R.id.btn_delete);
            btn_edit=itemView.findViewById(R.id.btn_edit);
            btn_call=itemView.findViewById(R.id.btn_call);
        }
    }
}
