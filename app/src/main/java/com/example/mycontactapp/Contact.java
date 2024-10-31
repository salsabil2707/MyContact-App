package com.example.mycontactapp;

public class Contact {
    private int id;
    private String email;
    private String phone;
    private String name;

    public Contact() {
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
