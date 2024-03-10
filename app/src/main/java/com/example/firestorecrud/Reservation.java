package com.example.firestorecrud;


import com.google.firebase.Timestamp;

public class Reservation {
    private String id, firstName, lastName, comment, phone;
    private Timestamp date;
    private long guestCount;


    public Reservation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return comment;
    }

    public void setBio(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(long guestCount) {
        this.guestCount = guestCount;
    }
}
