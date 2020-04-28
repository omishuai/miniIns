package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.ServerUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends CrudRepository<Photo, UUID> {

    public List<Photo> findByUserId (int userId);

}
