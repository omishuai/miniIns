package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.cucumber.bdd.RestTemplateResponseErrorHandler;
import com.app.miniIns.daos.UserRepository;
import com.app.miniIns.entities.User;

import com.jayway.jsonpath.JsonPath;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
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
        Assert.assertEquals(code, response.getStatusCodeValue());
    }


    @And("Response has value {string} for {string}")
    public void responseHasValueForUsername(String value, String pick) throws JSONException {
        Assert.assertEquals(JsonPath.read(response.getBody(), pick), value);
    }

    @And("Response has value {int} for {string}")
    public void responseHasValueForAge(int attribute, String pick) throws JSONException {
        Assert.assertEquals((int)JsonPath.read(response.getBody(), pick), attribute);
    }


    @Given("empty database")
    public void emptyDatabase() {
        userRepository.deleteAll();
        Iterator<User> user = userRepository.findAll().iterator();
        Assert.assertEquals(false, user.hasNext());
    }


    // login

    @And("User with username {string},password {string}, email {string}, age {int} and gender {string} exists")
    public void userWithUsernamePasswordEmailAgeAndGenderExistsInDatabase(String username, String password, String email, int age, String gender) {
        User u = new User(username, email, password, age, gender);
        userRepository.save(u);

        System.out.println(u + " EXISTS");
        Iterator<User> itr = userRepository.findAll().iterator();
        int c = 0;
        while (itr.hasNext()) {
            c++;
            itr.next();
        }

        Assert.assertEquals(1, c);

    }


    @When("User logins with {string} {string} and {string}")
    public void userLoginsWithAnd(String key, String account, String password) throws URISyntaxException {

        System.out.println(key  + " " + account + "       password: " + password);
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap map = new LinkedMultiValueMap();
        map.add(key, account);
        map.add("password", password);

        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, headers);

        final String baseUrl = "http://localhost:8080/login";
        URI uri = new URI(baseUrl);
        response = restTemplate.postForEntity(uri, request, String.class);
        User found = null;
        Iterator<User> itr = userRepository.findAll().iterator();
        if (itr.hasNext()) found = itr.next();

        System.out.println("RESPONSE: " + response);
        System.out.println("Retrieved from H2: " + found);
    }
}
