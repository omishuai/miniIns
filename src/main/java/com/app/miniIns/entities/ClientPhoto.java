package com.app.miniIns.entities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientPhoto {

    private String username;
    private URL url;
    private UUID uuid;
    private List<ClientUser> likedBy = new ArrayList<>();

    private List<ClientComment> photoComments = new ArrayList<>();


    public ClientPhoto(String username, URL url, UUID uuid) {
        this.username = username;
        this.url = url;
        this.uuid = uuid;
    }

    public ClientPhoto(String username, URL url, UUID uuid, List<ClientUser> likedBy, List<ClientComment> comments) {
        this.username = username;
        this.url = url;
        this.uuid = uuid;
        this.likedBy = likedBy;
        this.photoComments = comments;
    }

    @Override
    public String toString() {
        return "ClientPhoto{" +
                "username='" + username + '\'' +
                ", url=" + url +
                ", uuid=" + uuid +
                ", likedBy=" + likedBy +
                ", comments=" + photoComments +
                '}';
    }


    public List<ClientUser> getLikedBy() {
        return likedBy;
    }

    public List<ClientComment> getPhotoComments() {
        return photoComments;
    }

    public String getUsername() {
        return username;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public UUID getUuid() {
        return uuid;
    }

}
