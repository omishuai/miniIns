package com.app.miniIns.entities.client;

import java.util.List;

public class ClientUserForHome {
    private int id;
    private String gender;
    private String username;
    private int age;
    private String intro;
    private long followsCount;
    private long followedByCount;
    private long photosCount;
    private List<PhotoForHomeExplore> photos;
    private String ProfilePhotoKey;

    public ClientUserForHome(int id, String gender, String username, int age, String intro, long followsCount, long followedByCount, long photosCount, List<PhotoForHomeExplore> photos, String profilePhotoKey) {
        this.id = id;
        this.gender = gender;
        this.username = username;
        this.age = age;
        this.intro = intro;
        this.followsCount = followsCount;
        this.followedByCount = followedByCount;
        this.photosCount = photosCount;
        this.photos = photos;
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
                ", photos=" + photos +
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

    public List<PhotoForHomeExplore> getPhotos() {
        return photos;
    }

    public String getProfilePhotoKey() {
        return ProfilePhotoKey;
    }

}
