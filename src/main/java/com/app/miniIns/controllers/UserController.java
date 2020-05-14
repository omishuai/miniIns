package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

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
    public void login(@RequestParam("user") String accountName, String password) throws Exception {
        userService.verifyInfo(accountName, password);
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

    @GetMapping("/user/{user}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ClientUserForHome getGreetingPageForUser(@PathVariable  String user) throws MalformedURLException {

        System.out.println(user  + " loading about data");
        UserForHome res = userService.findByUsernameProjection(user);
        System.out.println(res);
//        User res = userService.findByUsername(user);
        //int id, String gender, String username, int age, String intro, int followsCount, int followedByCount, int photosCount, List<ClientPhoto> photos, String profilePhotoKey

//        List<Photo> serverPhotos = res.getPhotos();
        List<Photo> serverPhotos = photoService.findByUserId(res.getId());
        Collections.sort(serverPhotos);
        List<ClientPhoto>  photos = new ArrayList<>();
        for (Photo p : serverPhotos)
            photos.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getUuid().toString()), p.getUuid()));

//        System.out.printf("%s \n%s \n%s \n%s \n%s \n%s \n%s \n%s \n%s \n%s \n",                res.getId(),
//                res.getGender(),
//                res.getUsername(),
//                res.getAge(),
//                res.getIntro(),
//                res.getFollowsCount(),
//                res.getFollowedByCount(),
//                res.getPhotosCount(),
//                photos,
//                res.getProfilePhotoKey());

        ClientUserForHome userForHome = new ClientUserForHome(
                res.getId(),
                res.getGender(),
                res.getUsername(),
                res.getAge(),
                res.getIntro(),
//                res.getFollows().size(),
//                res.getFollowedBy().size(),
                res.getFollowsCount(),
                res.getFollowedByCount(),
                res.getPhotosCount(),
                photos,
                res.getProfilePhotoKey());
        System.out.println(userForHome);
        return userForHome;

//        List<String> following = new ArrayList<>();
//        for (User usr : res.getFollows()) following.add(usr.getUsername());
//        List<String> followedBy = new ArrayList<>();
//        for (User usr : res.getFollowedBy()) followedBy.add(usr.getUsername());
//
//
//        List<Photo> serverPhotos = res.getPhotos();
//        List<ClientPhoto>  photos = new ArrayList<>();
//        for (Photo p : serverPhotos)
//            photos.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getUuid().toString()), p.getUuid()));
//
//
//        return new UserResponse(
//                res.getUsername(),
//                res.getIntro(),
//                res.getProfilePhotoKey(),
//                photos,
//                res.getFollowedBy().size(),
//                res.getFollows().size(),
//                photos.size()
//                );
    }

    @GetMapping("/feed")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ClientPhoto> getFeedsPageForUser() throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();

        User currentUser = userService.findByUsername(username);
        Set<User> users = currentUser.getFollows();
        users.add(currentUser);

        List<Photo> photos = new ArrayList<>();
        for (User usr : users) {
            photos.addAll(photoService.findRecentPhotosForUser(usr.getId(), LocalDateTime.now().minusDays(1), LocalDateTime.now()));
        }

        Collections.sort(photos);

        List<ClientPhoto> clientPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            clientPhotos.add(new ClientPhoto(photo.getUser().getUsername(), fileStorageService.getUrl(photo.getUuid().toString()), photo.getUuid()));
        }
        return clientPhotos;
    }


}
