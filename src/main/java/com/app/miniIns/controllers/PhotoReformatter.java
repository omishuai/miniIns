package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.CommentService;
import com.app.miniIns.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


@Component
public class PhotoReformatter {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CommentService commentService;

    public FileStorageService getFileStorageService() {
        return fileStorageService;
    }

    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public ClientPhoto constructClientPhoto(Photo photo) throws MalformedURLException {

        List<PhotoComment> comments = commentService.findByPhotoIdByOrderByTime(photo.getUuid());

        List<ClientUser> likedBy = new ArrayList<>();
        for (User u : photo.getLikedBy()) {
            likedBy.add(new ClientUser(u.getUsername(), u.getEmail(), u.getAge(), u.getGender()));
        }

        List<ClientComment> clientComments = new ArrayList<>();
        for (PhotoComment comment : comments) {
            clientComments.add(new ClientComment(
                    comment.getId(),
                    comment.getText(),
                    comment.getCreateDateTime(),
                    comment.getFromUser(),
                    comment.getPhoto().getUuid(),
                    comment.getToId()
            ));
        }
        return new ClientPhoto(photo.getUser().getUsername(), fileStorageService.getUrl(photo.getUuid().toString()),photo.getUuid(), likedBy, clientComments);
    }
}
