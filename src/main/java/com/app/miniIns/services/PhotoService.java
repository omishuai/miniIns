package com.app.miniIns.services;

import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.PhotoForHomeExplore;
import com.app.miniIns.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private PhotoRepository photoRepository;

    public Photo findById(UUID id) {
        return  photoRepository.findByUuid(id);
    }

    public List<PhotoForHomeExplore> findByUserIdForHomePageable(int id, int page, int size) {
        return photoRepository.findByUserIdForHomePageable(id, PageRequest.of(page, size, Sort.by("createDateTime").descending()));
    }

    public List<PhotoForHomeExplore> findAllByCreateDateTimeForExplore(int page, int limit) {
        return photoRepository.findAllByCreateDateTimeForExplore(PageRequest.of(page, limit, Sort.by("createDateTime").descending()));
    }

    public void unlikedByUser(User user, UUID id) {
        photoRepository.removeLike(user.getId(), id);
    }

    public void likedByUser(User user, UUID id) {
        photoRepository.addlLike(user.getId(), id);
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

    public List<Photo> findRecentPhotosByTime(List<Integer> ids, int pageNumber, int pageLimit) {
        return photoRepository.findByUserIdIn(ids, PageRequest.of(pageNumber, pageLimit, Sort.by("createDateTime").descending()));
    }
}
