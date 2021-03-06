package com.app.miniIns.controllers;

import com.app.miniIns.entities.client.ClientComment;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.server.PhotoComment;
import com.app.miniIns.exceptions.EmptyInputException;
import com.app.miniIns.services.services.CommentService;
import com.app.miniIns.services.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.util.UUID;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/photo/{photoId}/comment")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientComment postComment(@RequestParam String text, @PathVariable("photoId")  String photoId) throws EmptyInputException, MalformedURLException {

        UUID pid = UUID.fromString(photoId);

        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();

        //Assume Initialized already in the class fields; the returned photo is updated and saved to db
        Photo photo = photoService.findById(pid);
        PhotoComment photoComment = commentService.addCommentToPhoto(text, username, photo);

        return new ClientComment(
                photoComment.getId(),
                photoComment.getText(),
                photoComment.getCreateDateTime(),
                photoComment.getFromUser(),
                photoComment.getPhoto().getUuid(),
                photoComment.getToId());
    }

    @PostMapping("/comment/{commentId}/reply")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientComment replyComment(@RequestParam String text, @PathVariable("commentId") int commentId) throws Exception {

        SecurityContext context = SecurityContextHolder.getContext();
        String commentingUsername = (String) context.getAuthentication().getPrincipal();

       PhotoComment photoComment = commentService.replyToComment(commentId, commentingUsername, text);
        return new ClientComment(
                photoComment.getId(),
                photoComment.getText(),
                photoComment.getCreateDateTime(),
                photoComment.getFromUser(),
                photoComment.getPhoto().getUuid(),
                photoComment.getToId());
    }
}
