package com.app.miniIns.services;

import com.app.miniIns.entities.PhotoComment;
import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.PhotoForHomeExplore;
import com.app.miniIns.entities.User;
import com.app.miniIns.exceptions.EmptyInputException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
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

    public List<PhotoForHomeExplore> findByUserIdForHome(int id) {
        return photoRepository.findByUserIdForHome(id);
    }

    public List<PhotoForHomeExplore> findAllByCreateDateTimeBetweenForExplore(LocalDateTime from, LocalDateTime to) {
        return photoRepository.findByCreateDateTimeBetweenForExplore(from, to);
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

    public List<Photo> findRecentPhotosForUser(int userId, LocalDateTime from, LocalDateTime to) {
        return photoRepository.findByUserIdAndCreateDateTimeBetween(userId, from, to);
    }

    public List<Photo> findRecentPhotosByTime(List<Integer> ids, int pageNumber, int pageLimit) {
        return photoRepository.findByUserIdIn(ids, (Pageable) PageRequest.of(pageNumber, pageLimit, Sort.by("createDateTime").descending()));
    }
}
