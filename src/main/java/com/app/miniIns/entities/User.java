package com.app.miniIns.entities;

import javax.persistence.*;

@Entity
@Table(name="user")
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

    public User() { }

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
