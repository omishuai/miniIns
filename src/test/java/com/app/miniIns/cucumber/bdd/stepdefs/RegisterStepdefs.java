package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.daos.UserRepository;
import com.app.miniIns.entities.User;
import com.google.common.base.StandardSystemProperty;
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


public class RegisterStepdefs {

    @Autowired
    private UserRepository userRepository;
    RestTemplate restTemplate;
    ResponseEntity<String> response;

    @When("User registers with username {string},password {string}, email {string}, age {int} and gender {string}")
    public void userRegistersWithUsernamePasswordEmailAgeAndGender(String username, String password, String email, int age, String gender) throws URISyntaxException {
        restTemplate = new RestTemplate();
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
        User found =  userRepository.findByEmail(email);
        System.out.println("RESPONSE: " + response);
        System.out.println("Retrieved from H2: " + found);


        //        User user = new User(username, email, password, age, gender);

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
//        String response = restTemplate.postForObject(uri, user, String.class);

        //        result = restTemplate.postForEntity(uri, user, User.class);


    };

    @Then("Response has status code {int}")
    public void verifyResult(int code) {
        Assert.assertEquals(code, response.getStatusCodeValue());
    }

    @And("Response has value {string} for {string}")
    public void responseHasValueForUsername(String attribute, String pick) throws JSONException {
        System.out.println("body:" + response.getBody());
        JSONObject body = new JSONObject(response.getBody());
        switch (pick) {
            case "/username":
                Assert.assertEquals(body.getString("username"), attribute);
                break;
            case "/password":
                Assert.assertEquals(body.getString("password"), attribute);
                break;
            case "/email":
                Assert.assertEquals(body.getString("email"), attribute);
                break;
            case "/gender":
                Assert.assertEquals(body.getString("gender"), attribute);
                break;
            default:;
        }
    }

    @And("Response has value {int} for {string}")
    public void responseHasValueForAge(int attribute, String pick) throws JSONException {
        JSONObject body = new JSONObject(response.getBody());
        Assert.assertEquals(body.getInt("age"), attribute);
    }
}
