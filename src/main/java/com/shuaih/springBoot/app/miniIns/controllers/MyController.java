package com.shuaih.springBoot.app.miniIns.controllers;


import com.shuaih.springBoot.app.miniIns.dao.UserDao;
import com.shuaih.springBoot.app.miniIns.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.net.www.MimeTable;

@Controller
public class MyController {

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    private UserDao userDao;


    //log in page  ("/")  ->
        // -> /home if succeed
        // ->  error then stay put, and relogin

    @GetMapping (path = "/")
    public String getGreetingPage() {
        return "greeting";
    }

    @PostMapping (path= "/login")
    public String login() {

    }


//    @RequestParam String username,
//    @RequestParam String password,
//    @RequestParam String email,
//    @RequestParam String gender,
//    @RequestParam int age

    //make User a form to check vars
    @GetMapping (path = "/register")
    public String register(User user) {
        User u = userDao.addUser(username, password, email, age, gender)
        if ( u != null) {
            return u + " Is Saved";
        }
        return "Failed to Save User";
    }

    //register page ("/register")
        // -> /home




}
