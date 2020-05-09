package com.app.miniIns.services;

import com.app.miniIns.entities.Comment;
import com.app.miniIns.entities.Photo;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface CommentRepository extends CrudRepository<Comment, Integer> {
    Comment findById(int id);
}
