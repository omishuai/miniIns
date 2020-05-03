package com.app.miniIns.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table (name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull (message = "Please Enter Username")
    @Pattern(regexp = "^[A-Z0-9a-z]+$", message = "Username Only Allows Alphanumeric Characters")
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

    private String salt;

    @ManyToMany
    @JoinTable(
            name = "relationship",
            joinColumns = { @JoinColumn(name = "follower_id") },
            inverseJoinColumns = { @JoinColumn(name = "followed_id") })
    private Set<User> followingList = new HashSet<>();

    @ManyToMany(mappedBy = "followingList")
    private Set<User> followedList = new HashSet<>();


    public User(String username, String email, String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public User() { }


    public void follow(User user) {
        this.followingList.add(user);
        user.getFollowedList().add(this);
    }

    public void stopFollow(User u) {
        this.followingList.remove(u);
        u.getFollowedList().remove(this);
    }

    public Set<User> getFollowingList() {
        return followingList;
    }

    public Set<User> getFollowedList() {
        return followedList;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

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

    @Override
    public String toString() {
        return
                String.format("{username: '%s', password:  '%s', email: '%s', age: %d, gender: '%s', salt: '%s'}",
                        username,
                        password,
                        email,
                        age,
                        gender,
                        salt);
    }

    @Override
    public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof User)) return false;
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
