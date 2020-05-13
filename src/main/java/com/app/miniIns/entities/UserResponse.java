package com.app.miniIns.entities;

import java.util.List;

//username, intro, profile photo, posts, following count, follower count, and posts count
public class UserResponse {
    private String username;
    private String intro;
    private String profilePhotoUrl;
    private List<ClientPhoto> photos;
    private int followingCount;
    private int followerCount;
    private int postCount;

    public UserResponse(String username,
                        String intro,
                        String profilePhotoUrl,
                        List<ClientPhoto> photos,
                        int followingCount,
                        int followerCount,
                        int postCount
    ) {
        this.username = username;
        this.intro = intro;
        this.profilePhotoUrl = profilePhotoUrl;
        this.photos = photos;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.postCount = postCount;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "username='" + username + '\'' +
                ", intro='" + intro + '\'' +
                ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                ", photos=" + photos +
                ", followingCount=" + followingCount +
                ", followerCount=" + followerCount +
                ", postCount=" + postCount +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getIntro() {
        return intro;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public List<ClientPhoto> getPhotos() {
        return photos;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


}
