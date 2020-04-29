package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends CrudRepository<Photo, UUID> {

    List<Photo> findByUserId (int userId);

}