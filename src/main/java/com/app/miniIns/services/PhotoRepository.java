package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.PhotoForHomeExplore;
import com.app.miniIns.entities.PhotoForHomeExplore;
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

    // Get recent photos for user home
    @Query("select new com.app.miniIns.entities.PhotoForHomeExplore(" +
            "user.username," +
            "photo.s3Key," +
            "photo.uuid," +
            "photo.createDateTime," +
            "count(comments)," +
            "count(likedBy)) " +
            "from Photo photo " +
            "left join photo.user user " +
            "left join photo.comments comments " +
            "left join photo.likedBy likedBy "+
            "where user.id = :userId group by user.id, photo.s3Key order by photo.createDateTime")
    List<PhotoForHomeExplore> findByUserIdForHome(int userId);

    //Just to get a reference without getting related unnecessary entities
    @Query(
            "select new com.app.miniIns.entities.Photo(photo.uuid)" +
                    "from Photo photo where photo.uuid = :id"
    )
    Photo findByUuidSimple(UUID id);

    // Get recent photos for explore
    @Query("select new com.app.miniIns.entities.PhotoForHomeExplore(" +
            "user.username," +
            "photo.s3Key," +
            "photo.uuid," +
            "photo.createDateTime," +
            "count(comments)," +
            "count(likedBy)) " +
            "from Photo photo " +
            "left join photo.user user " +
            "left join photo.comments comments " +
            "left join photo.likedBy likedBy "+
            "where photo.createDateTime >= :from and photo.createDateTime <= :end group by user.id order by photo.createDateTime")
    List<PhotoForHomeExplore> findByCreateDateTimeBetweenForExplore(LocalDateTime from, LocalDateTime end);

    @Query(
            "delete from photo_likedBy_user where photo_id = :uuid and user_id = :id"
    )
    void removeLike(int id, UUID uuid);

}
