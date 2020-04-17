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


//    public User addUser(String username, String password, String email, int age, String gender) {
//        try {
//            User n = new User(username, password, email, age, gender);
//            userRepo.save(n);
//            return n;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User addUser(User user) throws Exception {
        //validate the user:
        if (findByEmail(user.getEmail()) != null) throw new Exception("Existing User");
        if (user.getPassword().length() < 8) throw new Exception("Password Is Less Than 8 Characters");
        if (user.getAge() < 18) throw new Exception("Under Age 18");
        return userRepo.save(user);
    }

}
