package com.app.miniIns.entities;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;

public interface UserByProjection {


    String getGender();

    int getId();

    String getUsername();

    int getAge();

    String getIntro();

    List<Photo> getPhotos();

    String getProfilePhotoKey();

    @Value("#{follows.size()}")
    int getFollowsCount();

    @Value("#{followedBy.size()}")
    int getFollowedByCount();

    @Value("#{photos.size()}")
    int getPhotosCount();

}
