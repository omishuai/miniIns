package com.app.miniIns.cucumber.bdd.stepdefs;

import com.app.miniIns.cucumber.bdd.*;
import com.app.miniIns.cucumber.bdd.WebsocketMessages;
import com.app.miniIns.services.*;
import com.app.miniIns.entities.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class RegisterStepdefs {

    static Log log = LogFactory.getLog(RegisterStepdefs.class.getName());

    //Need a map that maps user to token to simulate front end.
    HashMap userAuthMap = new HashMap ();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    MessageService messageService;

    RestTemplate restTemplate;
    ResponseEntity<String> response;

    private WebsocketMessages websocketMessages =  new WebsocketMessages();

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

    @And("Response contains value for {string}")
    public void responseContainsValueFor(String key) {
        Assertions.assertNotNull(JsonPath.read(response.getBody(), key));
    }

    @Given("empty database")
    public void emptyDatabase() throws IOException {
        userAuthMap.clear();

        for (WebSocketSession webSocketSession : webSocketSessionHashMap.values())
            webSocketSession.close();

        webSocketSessionHashMap.clear();
        websocketMessages.clear();

        messageRepository.deleteAll();
        Iterator<Message> message= messageRepository.findAll().iterator();
        Assertions.assertFalse(message.hasNext());


        commentRepository.deleteAll();
        Iterator<PhotoComment> comment = commentRepository.findAll().iterator();
        Assertions.assertFalse(comment.hasNext());

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
        log.info(response.getBody());
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

        log.info(response.getBody());
    }

    String uploadedPhotoId;
    @When("User with username {string} uploads file {string}")
    public void userWithUsernameUploadsFile(String username, String filepath) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);

        log.info(headers);

        final String baseUrl = "http://localhost:8080/photo/upload";
        FileSystemResource resource = new FileSystemResource(filepath);

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());
        if (response.getStatusCodeValue() == 201) {
            uploadedPhotoId = JsonPath.read(response.getBody(), "$.uuid");
        }

    }


    @When("User with username {string} \\(un)follows {string} through {string}")
    public void userWithUsernameUnfollows(String u1, String u2, String url) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(u1);
        if (sec != null)
            headers.setBearerAuth(sec);

        final String baseUrl = "http://localhost:8080"+ url;

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("username", u2);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterStepdefs.class);


    Map<String, WebSocketSession> webSocketSessionHashMap = new ConcurrentHashMap<>();
    Map<String, Queue<String>> msgMap = new Hashtable<>();

    String currentMessage = "";

    @And("User with username {string} opens a socket to {string} named {string}")
    public void userWithUsernameOpensASocketToNamed(String username, String endpoint, String websocket) throws ExecutionException, InterruptedException {

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String sec = (String) userAuthMap.get(username);
        if (sec != null) headers.setBearerAuth(sec);

        WebSocketClient webSocketClient = new StandardWebSocketClient();

        // Client Side Handler that handles the message from server, and it is client side code
        WebSocketSession webSocketSession = webSocketClient.doHandshake(
                new TextWebSocketHandler() {
                    @Override
                    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException {
                        LOGGER.info("received message - " + message.getPayload() + " from " + websocket);
                        websocketMessages.addMessage(websocket, message.getPayload());
                    }

                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws InterruptedException {
                        LOGGER.info("established connection - " + session);
                    }
                    }, headers, URI.create("ws://localhost:8080" + endpoint)
        ).get();
        webSocketSessionHashMap.put(websocket, webSocketSession);
        websocketMessages.openSocket(websocket);

    }
    Exception exception;

    @When("User sends message {string} to {string} through socket {string}")
    public void userSendsMessageTypeMessageToThroughSocket(String message, String receiver, String websocket) {

        try {
            TextMessage textMessage = new TextMessage(String.format("{type: \"message\", message: \"%s\", receiver: \"%s\"}", message, receiver));
            webSocketSessionHashMap.get(websocket).sendMessage(textMessage);
            LOGGER.info("sent message - " + textMessage.getPayload() + " through "+ websocket);
        } catch (Exception e) {
            LOGGER.error("Exception while sending a message", e);
            exception = e;
        }
    }


    @Then("consume message from websocket {string}")
    public void consumeMessageFromWebsocket(String websocket) throws InterruptedException {
        currentMessage = websocketMessages.pollMessage(websocket);
    }

    @Then("User sends ack to websocket {string}")
    public void userSendsAckToWebsocket(String websocket) throws IOException {
        int id = JsonPath.read(currentMessage, "$.messageId");
        TextMessage textMessage = new TextMessage(String.format("{type: \"ack\", message: \"%s\", messageId: %d}", "Received", id));
        webSocketSessionHashMap.get(websocket).sendMessage(textMessage);
        LOGGER.info("send ack message - " + textMessage.getPayload() + " through socket " + websocket);
    }


    @Then("User sends ack to websocket {string} with wrong id {int}")
    public void userSendsAckToWebsocketWithWrongId(String websocket, int id) throws IOException {
        TextMessage textMessage = new TextMessage(String.format("{type: \"ack\", message: \"%s\", messageId: %d}", "Received", id));
        webSocketSessionHashMap.get(websocket).sendMessage(textMessage);
        LOGGER.info("send ack message - " + textMessage.getPayload() + " through socket " + websocket);
    }

    @Then("message has {string} for {string}")
    public void websocketReturnsFor(String value, String path) {
        Assertions.assertEquals(JsonPath.read(currentMessage, path), value);
    }

    @And("{string} disconnects")
    public void disconnects(String websocket) throws IOException {
        webSocketSessionHashMap.get(websocket).close();
    }

    @And("User with username {string} registers and logs in")
    public void userWithUsernamePasswordEmailAgeAndGenderRegistersAndLogsIn(String username) throws URISyntaxException {
        userRegistersWithUsernamePasswordEmailAgeAndGender(username, "password",username+"@server.com", 21, "male");
        userLoginsWithAnd(username, "password");
        userWithIsAuthenticated();
    }

    @Then("{string} is close")
    public void isClose(String websocket) {
        Assertions.assertFalse(webSocketSessionHashMap.get(websocket).isOpen());
    }
    @Then("{string} is open")
    public void isOpen(String websocket) {
        Assertions.assertTrue(webSocketSessionHashMap.get(websocket).isOpen());
    }

    @And("exception has message {string}")
    public void exceptionHasMessage(String message) {
        Assertions.assertEquals(exception.getMessage(), message);
    }

    @Then("there is no message")
    public void thereIsNoMessage() {
        Assertions.assertEquals(currentMessage, "");
    }



    @When("User {string} likes a photo")
    public void userClicksLikeOnAPhoto(String username) {
        HttpHeaders headers = new HttpHeaders();
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);

        final String baseUrl = "http://localhost:8080/photo/"+uploadedPhotoId+"/like";
        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("pid", uploadedPhotoId);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);
        log.info(response.getBody());
    }

    @When("User {string} unlikes a photo")
    public void userUnclicksLikeOnAPhoto(String username) {
        HttpHeaders headers = new HttpHeaders();
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);

        final String baseUrl = "http://localhost:8080/photo/"+uploadedPhotoId+"/unlike";
        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("pid", uploadedPhotoId);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);
        log.info(response.getBody());
    }


    int commentId;
    @When("User {string} comments {string} on photo")
    public void userCommentsOnPhoto(String username, String comment) {

        //post comment
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);


        final String baseUrl = "http://localhost:8080/photo/" + uploadedPhotoId + "/comment";
        log.info(baseUrl);

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("text", comment);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());
        if (response.getStatusCodeValue() == 201)
            commentId = JsonPath.read(response.getBody(), "$.id");
    }

    @When("User {string} responds {string} to comment")
    public void userRespondsToComment(String username, String respondingComment) {

        //Reply to a comment
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);


        final String baseUrl = "http://localhost:8080/comment/"+ commentId +"/reply";
        log.info(baseUrl);

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("text", respondingComment);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());

    }

    @When("User {string} responds {string} to comment with id {int}")
    public void userRespondsToCommentWithWrongId(String username, String respondingComment, int id) {

        //Reply to a comment
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String sec = (String)userAuthMap.get(username);
        if (sec != null)
            headers.setBearerAuth(sec);


        final String baseUrl = "http://localhost:8080"+ "/comment/" + id+"/reply";
        log.info(baseUrl);

        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("text", respondingComment);

        HttpEntity<MultiValueMap> requestEntity
                = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(baseUrl,HttpMethod.POST, requestEntity,
                String.class);

        log.info(response.getBody());

    }

    @And("Response responds to a comment with id for {string}")
    public void responseRespondsToAComment(String path) {
        Assertions.assertEquals((int)JsonPath.read(response.getBody(), path), commentId);
        if (response.getStatusCodeValue() == 201) {
            commentId = JsonPath.read(response.getBody(), "$.id");
        }
    }

    @When("User with username {string} visits page {string} with page {int} size {int}")
    public void userWithUsernameVisitsPageWithPageSize(String user, String page, int pageNumber, int pageLimit) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String sec = (String)userAuthMap.get(user);
        if (sec != null)
            headers.setBearerAuth(sec);

//        MultiValueMap<String, Integer> map = new LinkedMultiValueMap();
//        map.add("pageLimit", pageLimit);
//        map.add("pageNumber", pageNumber);

        HttpEntity request = new HttpEntity<>(headers);


        // build the request
        final String baseUrl = "http://localhost:8080" + page+ "?page="+pageNumber+"&limit="+pageLimit;
        URI uri = new URI(baseUrl);
        //make an HTTP GET request with headers
        response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                String.class);

        log.info(response.getBody());



    }
}
