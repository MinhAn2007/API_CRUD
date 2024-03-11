package com.example.demo;

import com.example.demo.controller.Controller;
import com.example.demo.entity.User;
import com.example.demo.service.UserSerivce;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(Controller.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ControllerTest.class })
public class ControllerTest {
    @Autowired
    private  WebTestClient webTestClient;

    @MockBean
    private UserSerivce userService;
    @MockBean
    private Controller controller;

    @Test
    public void findAllReturnsCorrectResponseEntity() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(new User("user1", "address1"), new User("user2", "address2"));
        when(userService.findAll()).thenReturn(expectedUsers);

        // Act
        ResponseEntity<List<User>> response = controller.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }

}
