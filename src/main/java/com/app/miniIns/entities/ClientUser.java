package com.app.miniIns.entities;
import java.util.*;
public class ClientUser {

    private String username;
    private String email;
    private int age;
    private String gender;
    private List<String> following;
    private List<String> followedBy;

    public ClientUser(String username, String email, int age, String gender) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public ClientUser(String username, String email, int age, String gender, List<String> following, List<String> followedBy) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.followedBy = followedBy;
        this.following = following;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public void setFollowedBy(List<String> followedBy) {
        this.followedBy = followedBy;
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

    public String toString() {
        return  String.format("{username: '%s', email: '%s', age: %d, gender: '%s', following: %s, followedBy: %s}",
                username,
                email,
                age,
                gender,
                following,
                followedBy
                );
    }
}
