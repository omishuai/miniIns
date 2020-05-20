package com.app.miniIns.entities.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoForFeed {

    private String username;
    private URL url;
    private String s3Key;
    private UUID uuid;
    private long likedByCount;
    private long commentsCount;
    List<String> likedByFollows = new ArrayList<>();
    private List<ClientComment> photoComments = new ArrayList<>();


    public PhotoForFeed(String username, URL url, UUID uuid, int likedByCount, int commentsCount, List<String> likedByFollows, List<ClientComment> photoComments) {
        this.username = username;
        this.url = url;
        this.uuid = uuid;
        this.likedByCount = likedByCount;
        this.commentsCount = commentsCount;
        this.likedByFollows = likedByFollows;
        this.photoComments = photoComments;
    }


    public PhotoForFeed(String username, UUID uuid, String s3Key, long likedByCount, long commentsCount) {
        this.username = username;
        this.uuid = uuid;
        this.s3Key = s3Key;
        this.likedByCount = likedByCount;
        this.commentsCount = commentsCount;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getUsername() {
        return username;
    }

    public URL getUrl() {
        return url;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getLikedByCount() {
        return likedByCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLikedByCount(long likedByCount) {
        this.likedByCount = likedByCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setLikedByFollows(List<String> likedByFollows) {
        this.likedByFollows = likedByFollows;
    }

    public void setPhotoComments(List<ClientComment> photoComments) {
        this.photoComments = photoComments;
    }

    public List<String> getLikedByFollows() {
        return likedByFollows;
    }

    public List<ClientComment> getPhotoComments() {
        return photoComments;
    }
}
