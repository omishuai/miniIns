package com.app.miniIns.entities;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.*;

@Entity
@Table(name="user")
//@Constraint(validatedBy = UserValidator.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;

    @Email (message = "Invalid Email")
    private String email;

    @Size(min=8, message = "The Password Is Too Short")
    private String password;

    @Min(value=18, message = "Under Age")
    private int age;

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
        return  String.format("{username: '%s', email: '%s', age: '%s', gender: '%s'}", username, email, age, gender);
    }

    @Override
    public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof  User)) return false;
            User u = (User)o;
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
