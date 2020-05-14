package com.app.miniIns.entities;

public class UserTemplate {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;

    public UserTemplate(int id, String username, String password, String salt, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", username:'" + username + '\'' +
                ", email:'" + email + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }
}
