package com.app.miniIns.daos;

import com.app.miniIns.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User addUser(User user) {
        try {
            userRepo.save(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
