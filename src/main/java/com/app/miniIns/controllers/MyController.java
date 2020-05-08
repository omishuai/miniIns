package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.*;
import com.mysql.cj.xdevapi.Client;
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
import java.util.*;

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

        URL url =  fileStorageService.upload(photo.getUuid().toString(), file);

        photo = photoService.addPhoto(photo);

        ClientPhoto clientPhoto = new ClientPhoto(user.getUsername(), url, photo.getUuid());
        return clientPhoto;
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ClientUser login(@RequestParam("user") String accountName, String password) throws Exception {
        User res = userService.verifyInfo(accountName, password);
        List<String> following = new ArrayList<>();
        for (User usr : res.getFollows()) following.add(usr.getUsername());
        List<String> followedBy = new ArrayList<>();
        for (User usr : res.getFollowedBy()) followedBy.add(usr.getUsername());

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

        ClientUser follower = constructClientUserWithFollowingList(userService.findByUsername(followerUsername));
        ClientUser followed = constructClientUserWithFollowingList(userService.findByUsername(followedUsername));

        return new UserRelation(follower, followed);
    }


    private ClientUser constructClientUserWithFollowingList(User user) {

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

    @PostMapping("/like")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientPhoto addLike(@RequestParam String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);

        Photo photo = photoService.likedByUser(user, UUID.fromString(pid));

        List<ClientUser> likedBy = new ArrayList<>();
        for (User u : photo.getLikedBy()) {
            likedBy.add(new ClientUser(u.getUsername(), u.getEmail(), u.getAge(), u.getGender()));
        }
        return new ClientPhoto(photo.getUser().getUsername(), fileStorageService.getUrl(pid),photo.getUuid(), likedBy);
    }


    @PostMapping("/unlike")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientPhoto removeLike(@RequestParam String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);

        Photo photo = photoService.unlikedByUser(user, UUID.fromString(pid));

        List<ClientUser> likedBy = new ArrayList<>();
        for (User u : photo.getLikedBy()) {
            likedBy.add(new ClientUser(u.getUsername(), u.getEmail(), u.getAge(), u.getGender()));
        }
        return new ClientPhoto(photo.getUser().getUsername(), fileStorageService.getUrl(pid),photo.getUuid(), likedBy);
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

        List<String> following = new ArrayList<>();
        for (User usr : currentUser.getFollows()) following.add(usr.getUsername());
        List<String> followedBy = new ArrayList<>();
        for (User usr : currentUser.getFollowedBy()) followedBy.add(usr.getUsername());

        ClientUser clientUser = new ClientUser(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getAge(),
                currentUser.getGender(),
                following,
                followedBy);
        return new UserResponse(clientUser, clientPhotos);
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

    @GetMapping("/explore")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ClientPhoto> getPhotoPool() throws MalformedURLException {

        List<ClientPhoto> res = new ArrayList<>();
        List<Photo> ls = photoService.findAllByCreateDateTimeBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        for (Photo p: ls)
            res.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getUuid().toString()), p.getUuid()));
        return res;
    }


}
