package com.app.miniIns.services;

import com.app.miniIns.entities.PhotoComment;
import com.app.miniIns.entities.Photo;
import com.app.miniIns.exceptions.EmptyInputException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<PhotoComment> findByPhotoIdByOrderByTime(UUID photoId) {
        return commentRepository.findByPhotoUuid(photoId);
    }

    public PhotoComment addCommentToPhoto(String text,String fromUser, Photo photo) throws EmptyInputException {

        //comment that has to have a message

//        return commentRepository.save(comment);
        PhotoComment photoComment = new PhotoComment(text, fromUser);
        photoComment.setPhoto(photo);

        System.out.println(photoComment);

        return commentRepository.save(photoComment);
    }

    public PhotoComment addReplyingCommentToComment(String text, String fromUser, int commentId, Photo photo) throws EmptyInputException {
        //comment that has to have a message
        if (StringUtils.isEmpty(text)) throw new EmptyInputException("Text Is Empty");
        PhotoComment photoComment = new PhotoComment(text, fromUser, commentId);
        photoComment.setPhoto(photo);
        return commentRepository.save(photoComment);
    }

    public PhotoComment findById(int id) {
        return commentRepository.findById(id);
    }


}
