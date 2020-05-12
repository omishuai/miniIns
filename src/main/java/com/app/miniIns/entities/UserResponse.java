package com.app.miniIns.entities;

import java.util.List;

public class UserResponse {
    private ClientUser user;
    private List<ClientPhoto> photos;

    public UserResponse(ClientUser user, List<ClientPhoto> photos) {
        this.user = user;
        this.photos = photos;
    }

    public String toString() {
        return  String.format("{user: %s, photos: %s}", user, photos);
    }

    public ClientUser getUser() {
        return user;
    }

    public List<ClientPhoto> getPhotos() {
        return photos;
    }


}
