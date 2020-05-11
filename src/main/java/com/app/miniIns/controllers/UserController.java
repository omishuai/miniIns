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

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public PhotoService getPhotoService() {
        return photoService;
    }
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }
    public FileStorageService getFileStorageService() {
        return fileStorageService;
    }
    public void setFileStorageService(FileStorageService fileStorageService) { this.fileStorageService = fileStorageService; }

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientUser register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("age") int age,
            @RequestParam("gender") String gender) throws Exception {

        User res = userService.addUser(new User(username, email, password, age, gender));
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ClientUser login(@RequestParam("user") String accountName, String password) throws Exception {
        User res = userService.verifyInfo(accountName, password);
        return constructClientUserWithFollowingList(res);
    }

    //follow user
    @PostMapping(path = "/user/{username}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRelation follow(@PathVariable("username") String followedUsername) throws Exception {

        // Get the current user in context
        SecurityContext context = SecurityContextHolder.getContext();
        String followerUsername = (String) context.getAuthentication().getPrincipal();

        userService.followUser(followerUsername, followedUsername);

        ClientUser follower = constructClientUserWithFollowingList(userService.findByUsername(followerUsername));
        ClientUser followed = constructClientUserWithFollowingList(userService.findByUsername(followedUsername));

        return new UserRelation(follower, followed);
    }

    @PostMapping(path = "/user/{username}/unfollow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRelation unfollow(@PathVariable("username") String unfollowedUsername) {
        
        // Get the current user in context
        SecurityContext context = SecurityContextHolder.getContext();
        String followerUsername = (String) context.getAuthentication().getPrincipal();

        userService.stopFollowUser(followerUsername, unfollowedUsername);

        ClientUser follower = constructClientUserWithFollowingList(userService.findByUsername(followerUsername));
        ClientUser unfollowed = constructClientUserWithFollowingList(userService.findByUsername(unfollowedUsername));

        return new UserRelation(follower, unfollowed);
    }

    @GetMapping("/user/{user}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getGreetingPageForUser(@PathVariable  String user) throws MalformedURLException {

        User res = userService.findByUsername(user);
        List<String> following = new ArrayList<>();
        for (User usr : res.getFollows()) following.add(usr.getUsername());
        List<String> followedBy = new ArrayList<>();
        for (User usr : res.getFollowedBy()) followedBy.add(usr.getUsername());

        ClientUser u =  new ClientUser(
                res.getUsername(),
                res.getEmail(),
                res.getAge(),
                res.getGender(),
                following,
                followedBy);

        List<Photo> serverPhotos = photoService.findByUserId(res.getId());
        Collections.sort(serverPhotos);

        List<ClientPhoto>  photos = new ArrayList<>();
        for (Photo p : serverPhotos)
            photos.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getUuid().toString()), p.getUuid()));


        return new UserResponse(u, photos);
    }

    @GetMapping("/feed")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getFeedsPageForUser() throws MalformedURLException {
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

        ClientUser clientUser = constructClientUserWithFollowingList(currentUser);

        return new UserResponse(clientUser, clientPhotos);
    }

    public  ClientUser constructClientUserWithFollowingList(User user) {

        List<String> following = new ArrayList<>();
        for (User usr : user.getFollows()) following.add(usr.getUsername());

        List<String> followedBy = new ArrayList<>();
        for (User usr : user.getFollowedBy()) followedBy.add(usr.getUsername());

        return new ClientUser(
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                following,
                followedBy);
    }

}
