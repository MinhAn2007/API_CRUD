package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.service.UserSerivce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("crud")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    private final UserSerivce userSerivce;

    public Controller(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userSerivce.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        if (!isValid(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(userSerivce.add(user));
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        System.out.println(user.getId());
        Optional<User> findUser = userSerivce.findById(user.getId());
        if (findUser.isPresent()) return ResponseEntity.ok(userSerivce.update(user));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<User> findUser = userSerivce.findById(id);
        if (findUser.isPresent()) {
            userSerivce.delete(findUser.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isValid(User user) {
        return user.getName().matches("[a-zA-Z]+") && user.getCompany().matches("[a-zA-Z]+");
    }
}
