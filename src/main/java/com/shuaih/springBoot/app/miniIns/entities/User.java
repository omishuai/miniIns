package com.shuaih.springBoot.app.miniIns.entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;

    private String email;
    private String password;
    private int age;

    /*all vars can be validated*/

    public String getGender() {
        return gender;
    }

    private String gender;

//    @OneToMany(fetch = FetchType.EAGER,mappedBy="user",cascade = CascadeType.ALL)
//    private Set<Photo> photos;

    public User(String username, String email, String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return  String.format("%s: {email: '%s', age:'%s'}", username, email, age);
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
