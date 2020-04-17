package com.app.miniIns.controllers;

import com.app.miniIns.entities.User;
import com.app.miniIns.daos.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
public class MyController {

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private UserService userService;


    //Main page
    @GetMapping(path = "/")
    public String getGreetingPage(ModelAndView modelAndView) {
        return "greeting";
    }

    //home for user
    @RequestMapping("/{username}")
    public ModelAndView getGreetingPageForUser(ModelAndView modelAndView, @PathVariable String username) {
        modelAndView.addObject("usernmae", username);
        modelAndView.setViewName("greeting");
        return modelAndView;
    }


    @GetMapping (path = "/register")
    public ModelAndView register(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping(path = "/register")
    public User register(@Valid User user) throws Exception {
        return userService.addUser(user);
    }



}
