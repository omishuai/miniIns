package com.app.miniIns.entities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoForFeed {

    private String username;
    private URL url;
    private UUID uuid;
    private int likedByCount;
    private int commentsCount;
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

    public String getUsername() {
        return username;
    }

    public URL getUrl() {
        return url;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLikedByCount() {
        return likedByCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public List<String> getLikedByFollows() {
        return likedByFollows;
    }

    public List<ClientComment> getPhotoComments() {
        return photoComments;
    }
}
