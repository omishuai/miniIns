package com.app.miniIns.controllers;

import com.app.miniIns.entities.client.ClientPhoto;
import com.app.miniIns.entities.client.PhotoForHomeExplore;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.server.User;
import com.app.miniIns.services.services.CommentService;
import com.app.miniIns.services.services.FileStorageService;
import com.app.miniIns.services.services.PhotoService;
import com.app.miniIns.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/photo/{photoId}/like")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void addLike(@PathVariable("photoId") String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);
        photoService.likedByUser(user, UUID.fromString(pid));
    }


    @PostMapping("/photo/{photoId}/unlike")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void removeLike(@PathVariable("photoId")  String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);
        photoService.unlikedByUser(user, UUID.fromString(pid));
    }


    @PostMapping(path="/photo/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientPhoto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        String u = (String) auth.getPrincipal();

        // This will make the join with photo, since photos in User is OneToMany with Lazy fetch, meaning
        // photos will not be loaded unless calling the method as following:
        // System.out.println(user.getPhotos());

        // findByUsername by default has the fetch: LAZY, so mapping that tagged lazy will not be queried
        User user = userService.findByUsername(u);

        //In case the destination becomes disk, we make controller unaware of s3 bucket
        Photo photo = new Photo(user, file.getOriginalFilename());

        URL url =  fileStorageService.upload(photo.getS3Key(), file);
        photo = photoService.addPhoto(photo);

        return  new ClientPhoto(user.getUsername(), url, photo.getUuid());
    }

    @GetMapping("/explore")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<PhotoForHomeExplore> getPhotoPool(@RequestParam("page") int page, @RequestParam("limit") int limit) throws MalformedURLException {
        List<PhotoForHomeExplore> ls = photoService.findAllByCreateDateTimeForExplore(page, limit);
        for (PhotoForHomeExplore p: ls) {
            p.setS3Url(fileStorageService.getUrl(p.getS3Key()));
        }
        return ls;
    }

}
