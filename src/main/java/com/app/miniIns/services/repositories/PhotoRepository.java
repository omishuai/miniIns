package com.app.miniIns.services.repositories;

import com.app.miniIns.entities.client.PhotoForFeed;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.client.PhotoForHomeExplore;
import com.app.miniIns.entities.server.User;
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
    @Query("select new com.app.miniIns.entities.client.PhotoForFeed(" +
                    "user.username," +
                    "photo.uuid," +
                    "photo.s3Key," +
                    "count(likedBy), " +
                    "count(comments)) " +

                    "from Photo photo " +
            "left join photo.user user " +
            "left join photo.comments comments " +
            "left join photo.likedBy likedBy "+

                    "where user in (" +
                    "select user " +
                    "from User user " +
                    "left join user.followedBy fb " +
                    "where user.id = :id or fb.id = :id) " +

                    "group by photo.s3Key")
    List<PhotoForFeed> findByUserIdIn(int id, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "delete from photo_liked_by_user where photo_id = :uuid and user_id = :id",
            nativeQuery = true
    )
    void removeLike(int id, UUID uuid);

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO  photo_liked_by_user (photo_id, user_id) VALUES(:uuid, :id)",
            nativeQuery = true
    )
    void addlLike(int id, UUID uuid);

    @Query(
                    "select user from User user " +
                        "left join user.followedBy fb " +
                    "where fb.id = :userId " +
                        "and user.id in (" +
                            "select likedBy.id " +
                            "from Photo photo " +
                                "left join photo.likedBy likedBy " +
                            "where photo.id = :photoId) ")
    List<User>findByPhotoUuidAndUserIdAndFollowsForFeed(int userId, UUID photoId);

}
