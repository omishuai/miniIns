package com.app.miniIns.entities.client;
import java.util.*;
public class ClientUser {

    private String username;
    private String email;
    private int age;
    private String gender;
    private List<String> follows;
    private List<String> followedBy;

    public ClientUser(String username, String email, int age, String gender) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public ClientUser(String username, String email, int age, String gender, List<String> follows, List<String> followedBy) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.followedBy = followedBy;
        this.follows = follows;
    }

    public String toString() {
        return  String.format("{username: '%s', email: '%s', age: %d, gender: '%s', follows: %s, followedBy: %s}",
                username,
                email,
                age,
                gender,
                follows,
                followedBy
        );
    }

    public List<String> getFollows() {
        return follows;
    }

    public List<String> getFollowedBy() {
        return followedBy;
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
