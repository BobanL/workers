package com.example.boban.workers;

public class MyAppUser {
    String fname;
    String lname;
    String email;
    String password;
    String phoneNumber;
    String zipCode;

    public MyAppUser(String fname, String lname, String email, String password, String phoneNumber, String zipCode) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
