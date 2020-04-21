package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.cucumber.bdd.RestTemplateResponseErrorHandler;
import com.app.miniIns.daos.UserRepository;
import com.app.miniIns.entities.User;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


public class RegisterStepdefs {

    @Autowired
    private UserRepository userRepository;


    RestTemplate restTemplate;
    ResponseEntity<String> response;

    @When("User registers with username {string},password {string}, email {string}, age {int} and gender {string}")
    public void userRegistersWithUsernamePasswordEmailAgeAndGender(String username, String password, String email, int age, String gender) throws URISyntaxException {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", username);
        map.add("password", password);
        map.add("age", age);
        map.add("gender", gender);
        map.add("email", email);
        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, headers);

        final String baseUrl = "http://localhost:8080/register";
        URI uri = new URI(baseUrl);
        response = restTemplate.postForEntity(uri, request, String.class);
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        User found = null;
        Iterator<User> itr = userRepository.findAll().iterator();
        if (itr.hasNext()) found = itr.next();

        System.out.println("RESPONSE: " + response);
        System.out.println("Retrieved from H2: " + found);


//        final String baseUrl = "http://localhost:8080/register";
//        URI uri = new URI(baseUrl);
//        User user = new User(username, email, password, age, gender);
//        ResponseEntity<User> response = restTemplate.postForEntity(uri, user, User.class);
//        System.out.println("RESPONSE: " + response);
//        System.out.println("Retrieved from H2: " + found);

    };

    @Then("Response has status code {int}")
    public void verifyResult(int code) {
        Assertions.assertEquals(code, response.getStatusCodeValue());
    }


    @And("Response has value {string} for {string}")
    public void responseHasValueForUsername(String value, String pick) throws JSONException {
        Assertions.assertEquals(JsonPath.read(response.getBody(), pick), value);
    }

    @And("Response has value {int} for {string}")
    public void responseHasValueForAge(int attribute, String pick) throws JSONException {
        Assertions.assertEquals((int)JsonPath.read(response.getBody(), pick), attribute);
    }

    @Given("User with {string} for {string} is inserted to database")
    public void userWithForExistsInDatabase(String attr, String pick) {
        String gender = "male";
        int age = 21;
        String password = "password";
        String username;
        String email;
        if (pick.equals("username")) {
            username  = attr;
            email = "email@serv.com";
        } else {
            username = "usernae";
            email = attr;
        }

        User u = new User(username, email, password, age, gender);
        userRepository.save(u);

        System.out.println(u + " EXISTS");
        Iterator<User> itr = userRepository.findAll().iterator();
        int c = 0;
        while (itr.hasNext()) {
            c++;
            itr.next();
        }

        Assertions.assertEquals(1, c);
    }

    @Given("empty database")
    public void emptyDatabase() {
        userRepository.deleteAll();
        Iterator<User> user = userRepository.findAll().iterator();
        Assertions.assertEquals(false, user.hasNext());
    }



}
