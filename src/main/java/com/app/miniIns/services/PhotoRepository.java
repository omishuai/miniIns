package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.PhotoForHome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends CrudRepository<Photo, UUID> {


    List<Photo> findByUserId (int userId);
    List<Photo> findAllByCreateDateTimeBetween(LocalDateTime from, LocalDateTime end);
    Photo findByUuid(UUID uuid);
    List<Photo> findByUserIdAndCreateDateTimeBetween(int userId, LocalDateTime from, LocalDateTime end);

    @Query("select new com.app.miniIns.entities.PhotoForHome(" +
            "user.username," +
            "photo.s3Key," +
            "photo.uuid," +
            "photo.createDateTime) " +
            "from Photo photo " +
            "left join photo.user user "+
            "where user.id = :userId")
    List<PhotoForHome> findByUserIdForHome(int userId);


}
