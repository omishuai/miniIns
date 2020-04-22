package com.app.miniIns.entities;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.*;

@Entity
@Table(name="Users")
public class ServerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull (message = "Please Enter Username")
    private String username;

    @NotNull (message = "Please Enter Email")
    @Email (message = "Invalid Email")
    private String email;

    @NotNull (message = "Please Enter Password")
    @Size(min=8, message = "The Password Is Too Short")
    private String password;

    @NotNull (message = "Please Enter Age")
    @Min(value=18, message = "Under Age")
    private int age;

    @NotNull (message = "Please Enter Gender")
    private String gender;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private String salt;


    public String getGender() {
        return gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



//    @OneToMany(fetch = FetchType.EAGER,mappedBy="user",cascade = CascadeType.ALL)
//    private Set<Photo> photos;

    public ServerUser(String username, String email, String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public ServerUser() { }

    @Override
    public String toString() {
        return  String.format("{username: '%s', password:  '%s', email: '%s', age: '%s', gender: '%s'}", username, password, email, age, gender);
    }

    @Override
    public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof  ServerUser)) return false;
            ServerUser u = (ServerUser)o;
            return username.equals(u.username) && email.equals(u.email);
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }


}
