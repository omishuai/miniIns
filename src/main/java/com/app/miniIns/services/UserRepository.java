package com.app.miniIns.services;

import com.app.miniIns.entities.ServerUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<ServerUser, Integer> {
    ServerUser findByEmail(String email);
    ServerUser findByUsername(String username);
}
