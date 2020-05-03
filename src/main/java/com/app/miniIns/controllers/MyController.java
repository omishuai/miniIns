package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.*;
import org.apache.catalina.realm.UserDatabaseRealm;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class MyController {

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
    public ClientUser register(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("age") int age,
                               @RequestParam("gender") String gender) throws Exception {

        User res = userService.addUser(new User(username, email, password, age, gender));
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }

    @PostMapping(path="/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientPhoto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        String u = (String) auth.getPrincipal();
        User user = userService.findByUsername(u);

        //In case the destination becomes disk, we make controller unaware of s3 bucket
        Photo photo = new Photo(user, file.getOriginalFilename());

        URL url =  fileStorageService.upload(photo.getId().toString(), file);

        photoService.addPhoto(photo);

        ClientPhoto clientPhoto = new ClientPhoto(user.getUsername(), url, photo.getId());
        return clientPhoto;
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ClientUser login(@RequestParam("user") String accountName, String password) throws Exception {
        User res = userService.verifyInfo(accountName, password);
        List<String> following = new ArrayList<>();
        for (User usr : res.getFollowingList()) following.add(usr.getUsername());
        List<String> followedBy = new ArrayList<>();
        for (User usr : res.getFollowedList()) followedBy.add(usr.getUsername());

        return new ClientUser(
                res.getUsername(),
                res.getEmail(),
                res.getAge(),
                res.getGender(),
                following,
                followedBy);
    }

    //follow user
    @PostMapping(path = "/follow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRelation follow(@RequestParam("username") String followedUsername) throws Exception {

        // Get the current user in context
        SecurityContext context = SecurityContextHolder.getContext();
        String followerUsername = (String) context.getAuthentication().getPrincipal();

        userService.followUser(followerUsername, followedUsername);

        ClientUser follow = constructClientUserWithFollowingList(userService.findByUsername(followerUsername));
        ClientUser followed = constructClientUserWithFollowingList(userService.findByUsername(followedUsername));

        return new UserRelation(follow, followed);
    }


    private ClientUser constructClientUserWithFollowingList(User user) {

        List<String> following = new ArrayList<>();
        for (User usr : user.getFollowingList()) following.add(usr.getUsername());

        List<String> followedBy = new ArrayList<>();
        for (User usr : user.getFollowedList()) followedBy.add(usr.getUsername());

        return new ClientUser(
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                following,
                followedBy);
    }

    @PostMapping(path = "/unfollow")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRelation unfollow(@RequestParam("username") String unfollowedUsername) {
        
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
        for (User usr : res.getFollowingList()) following.add(usr.getUsername());
        List<String> followedBy = new ArrayList<>();
        for (User usr : res.getFollowedList()) followedBy.add(usr.getUsername());

        ClientUser u =  new ClientUser(
                res.getUsername(),
                res.getEmail(),
                res.getAge(),
                res.getGender(),
                following,
                followedBy);

        List<Photo> serverPhotos = photoService.findByUserId(res.getId());
        List<ClientPhoto>  photos = new ArrayList<>();
        for (Photo p : serverPhotos)
            photos.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getId().toString()), p.getId()));

        return new UserResponse(u, photos);
    }

    @GetMapping("/explore")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ClientPhoto> getPhotoPool() throws MalformedURLException {

        List<ClientPhoto> res = new ArrayList<>();
        List<Photo> ls = photoService.findAllByCreateDateTimeBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        for (Photo p: ls)
            res.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getId().toString()), p.getId()));
        return res;
    }
}
