package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
public class PhotoService{

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    public void setPhotoRepository(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Autowired
    PhotoRepository photoRepository;

    public List<Photo> findByUserId(int id) {
        return photoRepository.findByUserId(id);
    }


    public Photo addPhoto(@Valid Photo photo){
        return photoRepository.save(photo);
    }

    public List<Photo> findAll() {
        List<Photo> ls = new ArrayList<>();
        Iterator<Photo> itr =  photoRepository.findAll().iterator();
        while (itr.hasNext()) ls.add(itr.next());
        return ls;
    }
}
