package com.app.miniIns.daos;

import com.app.miniIns.entities.ServerUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<ServerUser, Integer> {
    ServerUser findByEmail(String email);
    ServerUser findByUsername(String username);
}
