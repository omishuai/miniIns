package com.app.miniIns;


import com.app.miniIns.controllers.MyController;
import static org.assertj.core.api.Assertions.*;
import com.app.miniIns.daos.UserService;
import com.app.miniIns.entities.User;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class registerTest {

    @Test
    public void registerSuccess() throws Exception {
        MyController controller = new MyController();
        User u = new User("shh", "hs13706717787", "this is the email", 29, "male");
        // Mock the behavior of UserService
        UserService userService = mock(UserService.class);
        controller.setUserService(userService);

        when(userService.findByEmail("hs13706717787")).thenReturn(null);
        when(userService.addUser(u)).thenReturn(u);

        User returnedU = controller.register(u);

        assertThat(returnedU.getUsername()).isEqualTo("shh");
        assertThat(returnedU.getPassword()).isEqualTo("this is the email");
        assertThat(returnedU.getEmail()).isEqualTo("hs13706717787");
        assertThat(returnedU.getAge()).isEqualTo(29);
        assertThat(returnedU.getGender()).isEqualTo("male");

        // verify findByEmail and addUser() is only called once
        verify(userService, times(0)).findByEmail("hs13706717787");
        verify(userService, times(1)).addUser(u);
    }

    @Test
    public void registerFailDuplicate() throws Exception {
        MyController controller = new MyController();
        UserService userService = mock(UserService.class);
        controller.setUserService(userService);

        User u = new User("shh", "hs13706717787", "this is the email", 29, "male");
        when(userService.findByEmail("hs13706717787")).thenReturn(u);
        when(userService.addUser(u)).thenThrow(new Exception("Existing User"));
        // duplicate
        try {
            controller.register(u);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Existing User");
            verify(userService, times(1)).addUser(u);
        }
    }
    @Test
    public void registerFailPassword() throws Exception {

        MyController controller = new MyController();
        UserService userService = mock(UserService.class);
        controller.setUserService(userService);

        User u = new User("shh", "hs13706717787", "12", 29, "male");

        when(userService.findByEmail("hs13706717787")).thenReturn(null);
        when(userService.addUser(u)).thenThrow(new Exception("Password Is Less Than 8 Characters"));
        // short password

        try {
            controller.register(u);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Password Is Less Than 8 Characters");
            verify(userService, times(1)).addUser(u);
        }

    }

    @Test
    public void registerFailAge() throws Exception {

        MyController controller = new MyController();
        UserService userService = mock(UserService.class);
        controller.setUserService(userService);

        // under age
        User u = new User("shh", "hs13706717787", "1dsdsdsddssss2", 10, "male");
        when(userService.addUser(u)).thenThrow(new Exception("Under Age 18"));

        try {
            controller.register(u);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Under Age 18");
            verify(userService, times(1)).addUser(u);
        }
    }
}
