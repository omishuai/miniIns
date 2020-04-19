package com.app.miniIns.cucumber.bdd;

import com.app.miniIns.MiniInsApplication;
import com.app.miniIns.daos.UserRepository;
import com.app.miniIns.entities.User;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To run cucumber test.
 */

//@SpringBootTest (classes = MiniInsApplication.class)
//@RunWith(SpringRunner.class)


//Run with a cucumber runner
@RunWith(Cucumber.class)
//config the directory that contains features and output plugin
@CucumberOptions(features = "classpath:features", plugin = {"pretty", "html:target/cucumber"})
public class RegisterTest {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void test() {
//        User user =  userRepository
//                .save(new User("usernmae", "shuai9532@gmail.com", "password", 21, "male"));
//        User foundUser = userRepository.findByEmail("shuai9532@gmail.com");
//
//        assertThat(foundUser).isNotNull();
//        assertThat(user).isEqualTo(foundUser);
//    }

}
