package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.cucumber.bdd.*;
import com.app.miniIns.daos.UserRepository;
import com.app.miniIns.entities.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


import java.util.*;

public class RegisterStepdefs {

    //Need a map that maps user to token to simulate front end.
    HashMap userAuthMap = new HashMap ();

    @Autowired
    private UserRepository userRepository;


    RestTemplate restTemplate;
    ResponseEntity<String> response;

    @And("User with username {string},password {string}, email {string}, age {int} and gender {string} exists")
    @When("User registers with username {string},password {string}, email {string}, age {int} and gender {string}")
    public void userRegistersWithUsernamePasswordEmailAgeAndGender(String username, String password, String email, int age, String gender) throws URISyntaxException {

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


    @Given("empty database")
    public void emptyDatabase() {
        userRepository.deleteAll();
        Iterator<ServerUser> user = userRepository.findAll().iterator();
        Assertions.assertEquals(false, user.hasNext());
    }


    @When("User logins with {string} {string} and {string}")
    public void userLoginsWithAnd(String key, String account, String password) throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", account);
        map.add("password", password);

        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, headers);

        final String baseUrl = "http://localhost:8080/login";
        URI uri = new URI(baseUrl);
        response = restTemplate.postForEntity(uri, request, String.class);
    }


    public RegisterStepdefs() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setOutputStreaming(false);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @And("User with {string} {string} is authenticated")
    public void userWithIsAuthenticated(String userinfo, String arg1) throws JsonProcessingException {
        ObjectMapper mapper  = new ObjectMapper();
        String username = (String)JsonPath.read(response.getBody(), "$.username");
        String code = response.getHeaders().get("Authorization").get(0);

        userAuthMap.put(username, code);
        Assertions.assertTrue(code.contains("Bearer"));
    }

    @When("User with username {string} visits page {string}")
    public void userWithUsernameVisitsPage(String user, String page) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String sec = (String)userAuthMap.get(user);
        if (sec != null)
            headers.setBearerAuth(sec);

        // build the request
        HttpEntity request = new HttpEntity(headers);
        final String baseUrl = "http://localhost:8080" + page;

        //make an HTTP GET request with headers
        response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                request,
                String.class,
                user);
    }
}
