package com.shuaih.springBoot.app.miniIns.dao;

import com.shuaih.springBoot.app.miniIns.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    private UserRepository userRepo;


    public User addUser(String username, String password, String email, int age, String gender) {
       try {
           User n = new User(username, password, email, age, gender);
           userRepo.save(n);
           return n;
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }


}
