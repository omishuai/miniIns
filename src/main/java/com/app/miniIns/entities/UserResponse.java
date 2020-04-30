package com.app.miniIns.entities;

import java.util.List;

public class UserResponse {
    private ClientUser user;

    public UserResponse(ClientUser user, List<ClientPhoto> photos) {
        this.user = user;
        this.photos = photos;
    }

    public ClientUser getUser() {
        return user;
    }


    public List<ClientPhoto> getPhotos() {
        return photos;
    }

    private List<ClientPhoto> photos;

    public String toString() {
        return  String.format("{user: %s, photos: %s}", user, photos);
    }
}
