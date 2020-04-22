package com.app.miniIns.entities;
public class ClientUser {

    private String username;
    private String email;
    private int age;
    private String gender;

    public ClientUser(String username, String email, int age, String gender) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
