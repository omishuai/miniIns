package com.app.miniIns.services.services;

import com.app.miniIns.entities.client.PhotoForFeed;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.client.PhotoForHomeExplore;
import com.app.miniIns.entities.server.User;
import com.app.miniIns.services.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.validation.Valid;
import java.util.*;

@Service
public class PhotoService{

    private int PAGE_LIMIT = 15;
    @Autowired
    private PhotoRepository photoRepository;

    public Photo findById(UUID id) {
        return  photoRepository.findByUuid(id);
    }

    public List<PhotoForHomeExplore> findByUserIdForHomePageable(int id, int page, int limit) throws IllegalArgumentException{
        if (limit > PAGE_LIMIT || limit < 0) throw new IllegalArgumentException("Limit " + limit + " Does Not Fall In 0 to " + PAGE_LIMIT);
        return photoRepository.findByUserIdForHomePageable(id, PageRequest.of(page, limit, Sort.by("createDateTime").descending()));

    }

    public List<PhotoForHomeExplore> findAllByCreateDateTimeForExplore(int page, int limit) throws IllegalArgumentException{
        if (limit > PAGE_LIMIT || limit < 0) throw new IllegalArgumentException("Limit " + limit + " Does Not Fall In 0 to " + PAGE_LIMIT);
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

    public List<PhotoForFeed> findRecentPhotosByTime(int id, int pageNumber, int limit) throws IllegalArgumentException{
        if (limit > PAGE_LIMIT || limit < 0) throw new IllegalArgumentException("Limit " + limit + " Does Not Fall In 0 to " + PAGE_LIMIT);
        return photoRepository.findByUserIdIn(id, PageRequest.of(pageNumber, limit, Sort.by("createDateTime").descending()));
    }
}
