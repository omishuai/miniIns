package com.app.miniIns.services;

import com.app.miniIns.entities.Comment;
import com.app.miniIns.entities.Photo;
import com.app.miniIns.exceptions.EmptyInputException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

//    public Comment addComment(String text,String fromUser, Photo photo) throws EmptyInputException {
//
//        //comment that has to have a message
//
////        return commentRepository.save(comment);
//    }

    public Comment addReplyingCommentToComment(String text,String fromUser, int commentId, Photo photo) throws EmptyInputException {
        //comment that has to have a message
        if (StringUtils.isEmpty(text)) throw new EmptyInputException("Text Is Empty");
        Comment comment = new Comment(text, fromUser, commentId);
        comment.setPhoto(photo);
        return commentRepository.save(comment);
    }

    public Comment findById(int id) {
        return commentRepository.findById(id);
    }


}
