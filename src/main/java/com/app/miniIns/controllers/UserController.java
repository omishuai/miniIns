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

    @GetMapping("/user/{user}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ClientUserForHome getGreetingPageForUser(@PathVariable  String user) throws MalformedURLException {

        System.out.println("\n userService.findByUsernameProjection:");
        UserForHome res = userService.findByUsernameProjection(user);

        System.out.println("\n photoService.findByUserIdForHome:");
        List<PhotoForHome> serverPhotos = photoService.findByUserIdForHome(res.getId());
        Collections.sort(serverPhotos);
        List<ClientPhoto>  photos = new ArrayList<>();
        for (PhotoForHome p : serverPhotos)
            photos.add(new ClientPhoto(p.getUsername(), fileStorageService.getUrl(p.getS3Key()), p.getUuid()));

        ClientUserForHome userForHome = new ClientUserForHome(
                res.getId(),
                res.getGender(),
                res.getUsername(),
                res.getAge(),
                res.getIntro(),
                res.getFollowsCount(),
                res.getFollowedByCount(),
                res.getPhotosCount(),
                photos,
                res.getProfilePhotoKey());
        System.out.println(userForHome);
        return userForHome;
    }


    //////////////////////////////////////////////////////////////////////////////

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
    public List<PhotoForFeed> getFeedsPageForUser() throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();

        System.out.println("\n FEED: user.findByUsername()");
        User currentUser = userService.findByUsername(username);

        System.out.println("\n FEED: user.getFollows()");
        Set<User> users = currentUser.getFollows();
        users.add(currentUser);

        List<Photo> photos = new ArrayList<>();
        for (User usr : users) {
            System.out.println("\n FEED: photoService.findRecentPhotosForSUser");
            photos.addAll(photoService.findRecentPhotosForUser(usr.getId(), LocalDateTime.now().minusDays(1), LocalDateTime.now()));
        }

        Collections.sort(photos);

        List<PhotoForFeed> clientPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            System.out.println("\n FEED: photo.getLikedBy() photo.getComments():");
            List<User> likedBy = photo.getLikedBy();
            List<PhotoComment> comments = photo.getComments();

            List<ClientComment> sample = new ArrayList<>();
            for (PhotoComment photoComment : comments.subList(0,comments.size() > 5 ? 5 : comments.size())) {
                sample.add(new ClientComment(
                        photoComment.getId(),
                        photoComment.getText(),
                        photoComment.getCreateDateTime(),
                        photoComment.getFromUser(),
                        photoComment.getPhoto().getUuid(),
                        photoComment.getToId()));
            }


            List<String> likedByFollows = new ArrayList<>();
            for (User user : likedBy) {
                if (users.contains(user)) {
                    likedByFollows.add(user.getUsername());
                }
            }


            clientPhotos.add(
                    new PhotoForFeed(
                            photo.getUser().getUsername(),
                            fileStorageService.getUrl(photo.getS3Key()),
                            photo.getUuid(),
                            likedBy.size(),
                            comments.size(),
                            likedByFollows, sample));
        }
        return clientPhotos;
    }


}
