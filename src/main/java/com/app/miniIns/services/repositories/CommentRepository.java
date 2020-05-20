package com.app.miniIns.services.repositories;

import com.app.miniIns.entities.client.ClientComment;
import com.app.miniIns.entities.server.PhotoComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends CrudRepository<PhotoComment, Integer> {
    PhotoComment findById(int id);

    @Query("select new com.app.miniIns.entities.client.ClientComment(" +
            "comment.id, " +
            "comment.text, " +
            "comment.createDateTime, " +
            "comment.fromUser, " +
            "photo.uuid, " +
            "comment.toId) " +
            "From PhotoComment comment  left join comment.photo photo " +
            "WHERE photo.uuid = :uuid ")
    List<ClientComment> findByPhotoUuidOrderByCreateDateTime(UUID uuid, Pageable pageable);




}
