package com.app.miniIns.services;

import com.app.miniIns.entities.User;
import com.app.miniIns.entities.UserByProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    <T> T findByUsername(String username, Class<T> tclass);
//    User findByUsername(String username);
//    UserByProjection findByUsername(String username);

}
