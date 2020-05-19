package com.app.miniIns.services;

import com.app.miniIns.entities.User;
import com.app.miniIns.entities.UserForHome;
import com.app.miniIns.entities.UserTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    @Query("select new com.app.miniIns.entities.UserForHome(" +
            "user.id," +
            "user.gender," +
            "user.username," +
            "user.age," +
            "user.intro," +
            "count(follows)," +
            "count(followedBy)," +
            "count(photos)," +
            "user.profilePhotoKey) " +
            "from User user " +
            "left join user.follows follows " +
            "left join user.followedBy followedBy " +
            "left join user.photos photos " +
            "where user.username = :username group by user.id")
    UserForHome findByUsernameProjection(String username);

    <T> T findByUsername(String username, Class<T> tclass);


    @Query("select new com.app.miniIns.entities.UserTemplate(" +
            "user.id, " +
            "user.username, " +
            "user.password, " +
            "user.salt, " +
            "user.email)" +
            " from User user where user.username = :username")
    UserTemplate findByUsernameByProjection(String username);

    @Query("select new com.app.miniIns.entities.UserTemplate(" +
            "user.id, " +
            "user.username, " +
            "user.password, " +
            "user.salt, " +
            "user.email)" +
            " from User user where user.email = :email")
    UserTemplate findByEmailByProjection(String email);

}
