package com.app.miniIns.services.repositories;

import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.client.PhotoForHomeExplore;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends CrudRepository<Photo, UUID> {


    Photo findByUuid(UUID uuid);

    @Query("select new com.app.miniIns.entities.client.PhotoForHomeExplore(" +
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
            "group by user.id, photo.s3Key order by photo.createDateTime")
    List<PhotoForHomeExplore> findAllByCreateDateTimeForExplore(Pageable pageable);

    @Query("select new com.app.miniIns.entities.client.PhotoForHomeExplore(" +
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
            "where user.id = :userId group by user.id, photo.s3Key")
    List<PhotoForHomeExplore> findByUserIdForHomePageable(int userId, Pageable pageable);

    // Directly accessing the joined table without entity
    @Transactional
    @Modifying
    @Query(
            value = "delete from photo_likeed_user where photo_id = :uuid and user_id = :id",
            nativeQuery = true
    )
    void removeLike(int id, UUID uuid);


    // Directly accessing the joined table without entity
    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO  photo_likeed_user (photo_id, user_id) VALUES(:uuid, :id)",
            nativeQuery = true
    )
    void addlLike(int id, UUID uuid);

    List<Photo> findByUserIdIn(List<Integer> ids, Pageable pageable);

}
