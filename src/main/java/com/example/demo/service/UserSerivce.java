package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserSerivce {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public User add(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public List<User> findByNameIsNotNum(String name) {
        if (name.matches(".*\\d.*")) {
            return Collections.emptyList();
        } else {
            return entityManager.createQuery("select u from User u where u.name = :name", User.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }


    public void delete(User user) {
        userRepository.delete(user);
    }
}
