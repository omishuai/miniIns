package com.app.miniIns.controllers;


import static org.assertj.core.api.Assertions.*;
import com.app.miniIns.services.UserService;
import com.app.miniIns.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class MyControllerTest {
    MyController controller;
    UserService userService;
    @BeforeEach
    public void setUp(){
        controller = new MyController();
        userService = mock(UserService.class);
        controller.setUserService(userService);
    }

    @Test public void registerSuccess() throws Exception {

        ServerUser u = new ServerUser("shh", "hs13706717787", "this is the email", 29, "male");
        when(userService.findByEmail("hs13706717787")).thenReturn(null);
        when(userService.addUser(u)).thenReturn(u);

        ClientUser returnedU = controller.register(u);
        assertThat(returnedU.getUsername()).isEqualTo("shh");
        assertThat(returnedU.getEmail()).isEqualTo("hs13706717787");
        assertThat(returnedU.getAge()).isEqualTo(29);
        assertThat(returnedU.getGender()).isEqualTo("male");

        //addUser() is only called once
        verify(userService).addUser(u);
        verifyNoMoreInteractions(userService);
    }

    @Test public void registerFail() throws Exception {
        ServerUser u = new ServerUser("shh", "hs13706717787", "this is the email", 29, "male");
        when(userService.findByEmail("hs13706717787")).thenReturn(u);
        when(userService.addUser(u)).thenThrow(new Exception("Existing Email"));
        try {
            controller.register(u);
            fail("Expected Exception");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Existing Email");
            verify(userService).addUser(u);
            verifyNoMoreInteractions(userService);
        }
    }
}
