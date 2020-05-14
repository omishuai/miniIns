package com.app.miniIns.services;

import com.app.miniIns.entities.User;
import com.app.miniIns.entities.UserByProjection;
import com.app.miniIns.entities.UserForHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
//            "user.photos," +
            "user.profilePhotoKey) " +
            "from User user " +
            "left join user.follows follows " +
            "left join user.followedBy followedBy " +
            "left join user.photos photos " +
            "where user.username = :username group by user.id")
    UserForHome findByUsernameProjection(String username);

    <T> T findByUsername(String username, Class<T> tclass);
//    User findByUsername(String username);
//    UserByProjection findByUsername(String username);

}
