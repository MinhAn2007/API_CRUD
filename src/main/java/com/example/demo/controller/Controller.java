package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.service.UserSerivce;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("crud")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    @Autowired
    private UserSerivce userSerivce;

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> optionalUser = userSerivce.findById(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  }
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userSerivce.findAll());
    }
    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user){
        return ResponseEntity.ok(userSerivce.add(user));
    }
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user){
        System.out.println(user.getId());
        Optional<User> findUser = userSerivce.findById(user.getId());
        if (findUser.isPresent())
            return ResponseEntity.ok(userSerivce.update(user));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        User findUser = userSerivce.findById(id).orElseThrow();
        userSerivce.delete(findUser);
    }
}
