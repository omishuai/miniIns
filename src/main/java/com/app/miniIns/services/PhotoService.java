package com.app.miniIns.services;

import com.app.miniIns.entities.PhotoComment;
import com.app.miniIns.entities.Photo;
import com.app.miniIns.entities.PhotoForHomeExplore;
import com.app.miniIns.entities.User;
import com.app.miniIns.exceptions.EmptyInputException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Photo> findByUserId(int id) {
        return photoRepository.findByUserId(id);
    }

    public Photo findById(UUID id) {
        return  photoRepository.findByUuid(id);
    }

    public Photo findByIdSimple(UUID id) {
        return  photoRepository.findByUuidSimple(id);
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

    public Photo addCommentToPhoto(String text, String commentingUsername ,UUID photoId) throws EmptyInputException {

        if (StringUtils.isEmpty(text)) throw new EmptyInputException("Text Is Empty");
        PhotoComment photoComment = new PhotoComment(text, commentingUsername);

        Photo photo = photoRepository.findByUuid(photoId);

//        photo.addComment(photoComment);
//
//        System.out.print(photoComment);

        return photoRepository.save(photo);
    }
}
