package com.app.miniIns.daos;

import com.app.miniIns.entities.User;
import com.app.miniIns.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
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

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User addUser(@Valid User user) throws Exception {
        if (findByEmail(user.getEmail()) != null) throw new DuplicateDataException("Existing Email");
        if (findByUsername(user.getUsername()) != null) throw new DuplicateDataException("Existing Username");
        return userRepo.save(user);
    }

    public User verifyInfo(User user) throws Exception {
        String email =  user.getEmail();
        String password =  user.getPassword();
        String username =  user.getUsername();

        System.out.println("email: " + email + "  username: " +username + "  password: " +password);
        if ((email == null ||email.equals("")) && (username == null || username.equals(""))) throw new EmptyInputException("Please Enter Username or Email");
        if (password == null || password.equals("")) throw new EmptyInputException("Please Enter Password");

        User u;
        if (email != null && !email.equals("")) {
            u = findByEmail(email);
            if (u == null) throw new VerificationFailureException("Unregistered " + email);

        } else {
            u = findByUsername(username);
            if (u == null) throw new VerificationFailureException("Unregistered " + username);
        }

        if (u.getPassword().equals(password)) return u;
        throw new VerificationFailureException("Incorrect Password");
    }

}
