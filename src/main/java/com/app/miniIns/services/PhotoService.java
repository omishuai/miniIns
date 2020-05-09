package com.app.miniIns.services;

import com.app.miniIns.entities.Comment;
import com.app.miniIns.entities.Photo;
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


    public Photo unlikedByUser(User user, UUID id) {
        Photo photo = findById(id);
        List<User> likedBy = photo.getLikedBy();
        if (likedBy != null && likedBy.contains(user)) {
            likedBy.remove(user);
        }
        return photoRepository.save(photo);
    }

    public Photo likedByUser(User user, UUID id) {
        Photo photo = findById(id);

        List<User> likedBy = photo.getLikedBy();

        if (likedBy == null) {
            likedBy = new ArrayList<>();
            photo.setLikedBy(likedBy);
        }

        if (!likedBy.contains(user))
                likedBy.add(user);

        return photoRepository.save(photo);
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

    public List<Photo> findAllByCreateDateTimeBetween(LocalDateTime from, LocalDateTime to) {
        List<Photo> ls = new ArrayList<>();
        Iterator<Photo> itr =  photoRepository.findAllByCreatedDateTimeBetween(from, to).iterator();
        while (itr.hasNext()) ls.add(itr.next());
        return ls;
    }

    public List<Photo> findRecentPhotosForUser(int userId, LocalDateTime from, LocalDateTime to) {
        return photoRepository.findByUserIdAndCreatedDateTimeBetween(userId, from, to);
    }

    public Photo addCommentToPhoto(String text, String commentingUsername ,UUID photoId) throws EmptyInputException {

        if (StringUtils.isEmpty(text)) throw new EmptyInputException("Text Is Empty");
        Comment comment = new Comment(text, commentingUsername);

        Photo photo = photoRepository.findByUuid(photoId);

        photo.addComment(comment);

        System.out.print(comment);

        return photoRepository.save(photo);
    }
}
