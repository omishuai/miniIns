package com.app.miniIns.services;

import com.app.miniIns.entities.PhotoComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends CrudRepository<PhotoComment, Integer> {
    PhotoComment findById(int id);

    List<PhotoComment> findByPhotoUuidOrderByCreateDateTime(UUID uuid);
}
