package com.app.miniIns.services.services;

import com.app.miniIns.entities.client.ClientComment;
import com.app.miniIns.entities.client.ClientUser;
import com.app.miniIns.entities.server.PhotoComment;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.exceptions.EmptyInputException;
import com.app.miniIns.services.repositories.CommentRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public PhotoComment addCommentToPhoto(String text,String fromUser, Photo photo) throws EmptyInputException {

        if (StringUtils.isEmpty(text))
            throw new EmptyInputException("Text Is Empty");

        PhotoComment photoComment = new PhotoComment(text, fromUser);
        photoComment.setPhoto(photo);

        return commentRepository.save(photoComment);
    }

    public PhotoComment replyToComment(int commentId, String commentingUsername, String text) throws InvalidAttributeValueException, EmptyInputException {

        PhotoComment photoComment = findById(commentId);

        // Assuming that the photo itself is valid
        // Make sure the photoComment to be commented on exists
        if (photoComment == null) {
            throw new InvalidAttributeValueException("The comment is not found");
        }
        Photo photo = photoComment.getPhoto();

        // check if commenting user is owner of the photo
        if (commentingUsername.equals(photo.getUser().getUsername())) {
            return  addReplyingCommentToComment(text, commentingUsername, commentId, photo);
        } else {

            // If commenting user is not the owner, then can only respond to the comment on commenter's comment.
            // Meaning, A writes commentA on commentB from B, then only B can write a comment on commentA
            // Check if B, the commenting user comments on correct message
            // commentA is what this current message is commenting on
            int prevCommentId = photoComment.getToId(); //the comment that commentA comments on, which is commentB
            String username = findById(prevCommentId).getFromUser(); //commentB is from user B
            if (commentingUsername.equals(username)) {

                // B is who writes commentB, so B can comment back A on commentA
                return addReplyingCommentToComment(text, commentingUsername, commentId, photo);
            } else {
                throw new InvalidAttributeValueException(commentingUsername + " Cannot Comment on comment: " + photoComment.getText() + " from " + photoComment.getFromUser());
            }
        }
    }

    public PhotoComment addReplyingCommentToComment(String text, String fromUser, int commentId, Photo photo) throws EmptyInputException {
        //comment that has to have a message
        if (StringUtils.isEmpty(text)) throw new EmptyInputException("Text Is Empty");
        PhotoComment photoComment = new PhotoComment(text, fromUser, commentId);
        photoComment.setPhoto(photo);
        return commentRepository.save(photoComment);
    }

    public List<ClientComment> findByPhotoUuidForFeed(UUID photoId, int limit) {
        return commentRepository.findByPhotoUuidOrderByCreateDateTime(photoId, PageRequest.of(0, limit, Sort.by("createDateTime").descending()));
    }

    public PhotoComment findById(int id) {
        return commentRepository.findById(id);
    }




}
