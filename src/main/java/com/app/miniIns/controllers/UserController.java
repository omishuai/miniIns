package com.app.miniIns.controllers;

import com.app.miniIns.entities.client.*;
import com.app.miniIns.entities.server.Photo;
import com.app.miniIns.entities.server.PhotoComment;
import com.app.miniIns.entities.server.User;
import com.app.miniIns.services.services.CommentService;
import com.app.miniIns.services.services.FileStorageService;
import com.app.miniIns.services.services.PhotoService;
import com.app.miniIns.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("age") int age,
            @RequestParam("gender") String gender) throws Exception {

        userService.addUser(new User(username, email, password, age, gender));
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void login(@RequestParam("username") String accountName, @RequestParam("password") String password) throws Exception {
        userService.verifyInfo(accountName, password);
    }

    @GetMapping("/user/{user}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ClientUserForHome getGreetingPageForUser(@PathVariable  String user, @RequestParam("page") int page, @RequestParam("limit") int limit ) throws MalformedURLException {

        System.out.println("\n userService.findByUsernameProjection:");
        UserForHome res = userService.findByUsernameProjection(user);

        System.out.println("\n photoService.findByUserIdForHome:" + page + " "+ limit);
        List<PhotoForHomeExplore> serverPhotos = photoService.findByUserIdForHomePageable(res.getId(), page, limit);
        for (PhotoForHomeExplore p : serverPhotos)
            p.setS3Url(fileStorageService.getUrl(p.getS3Key()));

        ClientUserForHome userForHome = new ClientUserForHome(
                res.getId(),
                res.getGender(),
                res.getUsername(),
                res.getAge(),
                res.getIntro(),
                res.getFollowsCount(),
                res.getFollowedByCount(),
                res.getPhotosCount(),
                serverPhotos,
                res.getProfilePhotoKey());
        return userForHome;
    }

    //follow user
    @PostMapping(path = "/user/{username}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void follow(@PathVariable("username") String followedUsername) {

        // Get the current user in context
        SecurityContext context = SecurityContextHolder.getContext();
        String followerUsername = (String) context.getAuthentication().getPrincipal();

        userService.followUser(followerUsername, followedUsername);
    }

    @PostMapping(path = "/user/{username}/unfollow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void unfollow(@PathVariable("username") String unfollowedUsername) {
        
        // Get the current user in context
        SecurityContext context = SecurityContextHolder.getContext();
        String followerUsername = (String) context.getAuthentication().getPrincipal();

        userService.stopFollowUser(followerUsername, unfollowedUsername);
    }


    @GetMapping("/feed")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<PhotoForFeed> getFeedsPageForUser(@RequestParam("limit") int pageLimit, @RequestParam ("page") int pageNumber) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();

        //query
        User currentUser = userService.findByUsername(username);
//        Set<User> users = currentUser.getFollows();
//        users.add(currentUser);
//
//        List<Integer> ids = new ArrayList<>();
//        for (User usr : users) {
//            ids.add(usr.getId());
//        }

        // Find Photos for Users where user names should be passed in as a list
        List<PhotoForFeed> photos = photoService.findRecentPhotosByTime(currentUser.getId(), pageNumber, pageLimit);

        List<PhotoForFeed> clientPhotos = new ArrayList<>();
        for (PhotoForFeed photo : photos) {

            //one query for
            int sampleSize = 5;
            List<ClientComment> sample = commentService.findByPhotoUuidForFeed(photo.getUuid(), sampleSize);
//            List<PhotoComment> comments = photo.getComments();
//            List<ClientComment> sample = new ArrayList<>();
//            for (PhotoComment photoComment : comments.subList(0,Math.min(comments.size(), 5))) {
//                sample.add(new ClientComment(
//                        photoComment.getId(),
//                        photoComment.getText(),
//                        photoComment.getCreateDateTime(),
//                        photoComment.getFromUser(),
//                        photoComment.getPhoto().getUuid(),
//                        photoComment.getToId()));
//            }

//            List<User> likedBy = photo.getLikedBy();
//            List<String> likedByFollows = new ArrayList<>();
//            for (User user : likedBy) {
//                if (users.contains(user)) {
//                    likedByFollows.add(user.getUsername());
//                }
//            }
            List<String> likedByFollows = commentService.findByPhotoUuidAndUserIdAndFollowsForFeed(currentUser.getId(), photo.getUuid());

            URL url = fileStorageService.getUrl(photo.getS3Key());
            photo.setUrl(url);
            photo.setLikedByFollows(likedByFollows);
            photo.setPhotoComments(sample);
        }
        return clientPhotos;
    }


}
