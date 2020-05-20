package com.app.miniIns.services.repositories;

import com.app.miniIns.entities.client.PhotoForFeed;
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

    //String username, UUID uuid, int likedByCount, int commentsCount
    @Query (
            "select new com.app.miniIns.entities.client.PhotoForFeed(" +
                    "user.username," +
                    "photo.uuid," +
                    "photo.s3Key," +
                    "photo.createDateTime," +
                    "count(likedBy), " +
                    "count(comments)) " +
                    "from User user " +
                    "left join user.photos " +
                    "left join user.follows " +
                    "left join Photo photo "+
                    "left join photo.comments comments " +
                    "left join photo.likedBy likedBy "+
                    "where user.id = :id " +
                    "group by photo.s3Key "
//
//                    "UNION " +
//
//                    "select new com.app.miniIns.entities.client.PhotoForFeed(" +
//                    "user.username," +
//                    "photo.uuid," +
//                    "photo.s3Key," +
//                    "photo.createDateTime," +
//                    "count(comments)," +
//                    "count(likedBy)) " +
//                    ") " +
//                    "from Photo photo " +
//                    "left join photo.user user "+
//                    "left join photo.comments comments " +
//                    "left join photo.likedBy likedBy "+
//                    "where user.id = :id " +
//                    "group by photo.s3Key"
    )
    List<PhotoForFeed> findByUserIdIn(int id, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "delete from photo_likeed_user where photo_id = :uuid and user_id = :id",
            nativeQuery = true
    )
    void removeLike(int id, UUID uuid);

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO  photo_likeed_user (photo_id, user_id) VALUES(:uuid, :id)",
            nativeQuery = true
    )
    void addlLike(int id, UUID uuid);



}
