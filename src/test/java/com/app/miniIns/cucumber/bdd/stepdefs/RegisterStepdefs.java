package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.cucumber.bdd.*;
import com.app.miniIns.services.PhotoRepository;
import com.app.miniIns.services.UserRepository;
import com.app.miniIns.entities.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

    static Log log = LogFactory.getLog(RegisterStepdefs.class.getName());

    //Need a map that maps user to token to simulate front end.
    HashMap userAuthMap = new HashMap ();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;


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
    }

    @Then("Response has status code {int}")
    public void verifyResult(int code) {
        Assertions.assertEquals(code, response.getStatusCodeValue());
    }


    @And("Response has value {string} for {string}")
    public void responseHasValueForUsername(String value, String pick) {
        Assertions.assertEquals(JsonPath.read(response.getBody(), pick), value);
    }

    @And("Response has value {int} for {string}")
    public void responseHasValueForAge(int attribute, String pick) {

        Assertions.assertEquals((int)JsonPath.read(response.getBody(), pick), attribute);
    }


    @Given("empty database")
    public void emptyDatabase() {
        photoRepository.deleteAll();
        Iterator<Photo> photo = photoRepository.findAll().iterator();
        Assertions.assertFalse(photo.hasNext());

        userRepository.deleteAll();
        Iterator<User> user = userRepository.findAll().iterator();
        Assertions.assertFalse(user.hasNext());


    }


    @When("User logins with {string} and {string}")
    public void userLoginsWithAnd(String account, String password) throws URISyntaxException {

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

    @And("User is authenticated")
    public void userWithIsAuthenticated() {
        String username = JsonPath.read(response.getBody(), "$.username");
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
                String.class);
    }

    @When("User with username {string} uploads file {string}")
    public void userWithUsernameUploadsFile(String username, String filepath) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);

        final String baseUrl = "http://localhost:8080/upload";
        FileSystemResource resource = new FileSystemResource(filepath);

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());
    }

    @And("Response contains value for {string}")
    public void responseContainsValueFor(String key) {
        Assertions.assertNotNull(JsonPath.read(response.getBody(), key));
    }

    @Then("Response contains {int} images")
    public void responseContainsImages(int count) throws JSONException {
        JSONArray arr = new JSONArray(response.getBody());
        Assertions.assertEquals(count, arr.length());
    }

    @And("Response contains {int} photos for {string}")
    public void responseContainsPhotosFor(int count, String key) {
        List<ClientPhoto> photos = JsonPath.read(response.getBody(), key);
        Assertions.assertEquals(photos.size(), count);
    }
}
