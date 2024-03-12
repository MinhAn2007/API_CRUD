package com.example.demo;

import com.example.demo.controller.Controller;
import com.example.demo.entity.User;
import com.example.demo.service.UserSerivce;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSerivce userSerivce;

    @Test
    public void testFindAll() throws Exception {
        User user1 = new User(1L, "John", "Doe");
        User user2 = new User(2L, "Jane", "Doe");
        List<User> userList = Arrays.asList(user1, user2);

        when(userSerivce.findAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/crud/getAll").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("John")).andExpect(jsonPath("$[0].company").value("Doe"));
    }

    @Test
    public void testAdd() throws Exception {
        User user = new User(1L, "John", "Doe");

        when(userSerivce.add(any(User.class))).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/crud/add").content("{ \"name\": \"John\", \"company\": \"Doe\" }").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("John")).andExpect(jsonPath("$.company").value("Doe"));
    }

    @Test
    public void testAddValidateInput() throws Exception {
        User user = new User(1L, "John", "Doe");

        when(userSerivce.add(any(User.class))).thenReturn(user);

        String content = "{ \"name\": \"John\", \"company\": \"Doe\" }";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        String name = jsonNode.get("name").asText();
        String company = jsonNode.get("company").asText();
        assertTrue(name.matches("[a-zA-Z]+"));
        assertTrue(company.matches("[a-zA-Z]+"));

        mockMvc.perform(MockMvcRequestBuilders.post("/crud/add").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("John")).andExpect(jsonPath("$.company").value("Doe"));


    }
    @Test
    public void addThrowsErrorWhenInputIsInvalid() throws Exception {
        User user = new User(1L, "John", "Doe");

        when(userSerivce.add(any(User.class))).thenReturn(user);

        String invalidContent = "{ \"name\": \"John123\", \"company\": \"Doe456\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/crud/add")
                        .content(invalidContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateValidUser() throws Exception {
        User existingUser = new User(1L, "John", "Doe");
        when(userSerivce.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userSerivce.update(any(User.class))).thenReturn(existingUser);

        String content = "{\"id\": 1, \"name\": \"John\", \"company\": \"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/crud/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateInvalidUser() throws Exception {
        when(userSerivce.findById(2L)).thenReturn(Optional.empty());

        String content = "{\"id\": 2, \"name\": \"John\", \"company\": \"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/crud/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateInvalidInput() throws Exception {
        String content = "{\"name\": \"John\", \"company\": \"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/crud/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
