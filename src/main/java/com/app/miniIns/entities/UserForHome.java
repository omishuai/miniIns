package com.app.miniIns.entities;

import java.io.Serializable;
import java.util.List;

public class UserForHome implements Serializable {

    private int id;
    private String gender;
    private String username;
    private int age;
    private String intro;
    private int followsCount;
    private int followedByCount;
    private int photosCount;
    private List<Photo> photos;
    private String ProfilePhotoKey;

    public UserForHome(int id, String gender, String username, int age, String intro, int followsCount, int followedByCount, int photosCount,  String profilePhotoKey) {
        this.id = id;
        this.gender = gender;
        this.username = username;
        this.age = age;
        this.intro = intro;
        this.followsCount = followsCount;
        this.followedByCount = followedByCount;
        this.photosCount = photosCount;
//        this.photos = photos;
        ProfilePhotoKey = profilePhotoKey;
    }

    @Override
    public String toString() {
        return "UserForHome{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", intro='" + intro + '\'' +
                ", followsCount=" + followsCount +
                ", followedByCount=" + followedByCount +
                ", photosCount=" + photosCount +
//                ", photos=" + photos +
                ", ProfilePhotoKey='" + ProfilePhotoKey + '\'' +
                '}';
    }


    public int getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getIntro() {
        return intro;
    }

    public long getFollowsCount() {
        return followsCount;
    }

    public long getFollowedByCount() {
        return followedByCount;
    }

    public long getPhotosCount() {
        return photosCount;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getProfilePhotoKey() {
        return ProfilePhotoKey;
    }
}