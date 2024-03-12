package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserSerivce;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserSerivce userService;
    @Spy
    private User user = new User("teo1", "iuh");

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> query;
    @Test
    public void testAdd() {
        when(userRepository.save(user)).thenReturn(user);
        User user1 = userService.add(user);
        Assertions.assertNotNull(user1);
    }

    @Test
    public void testUpdate() {
        user.setCompany("iuh123");
        when(userRepository.save(user)).thenReturn(user);
        User user1 = userService.update(user);
        Assertions.assertEquals("iuh123", user1.getCompany());
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(java.util.List.of(user));
        Assertions.assertEquals(1, userService.findAll().size());
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals("teo1", userService.findById(1L).get().getName());
    }

    @Test
    public void testDelete() {
        userService.delete(user);
        Assertions.assertEquals(0, userService.findAll().size());
    }
    @Test
    public void findByNameIsNotNum_ReturnsEmptyList_WhenNameContainsNumber() {
        String name = "John1";
        List<User> result = userService.findByNameIsNotNum(name);
        assertEquals(0, result.size());
    }

    @Test
    public void findByNameIsNotNum_ReturnsUserList_WhenNameDoesNotContainNumber() {
        String name = "John111";
        User user = new User();
        user.setName(name);
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(user));

        List<User> result = userService.findByNameIsNotNum(name);

        verify(entityManager).createQuery("select u from User u where u.name = :name", User.class);
        verify(query).setParameter("name", name);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }
}
