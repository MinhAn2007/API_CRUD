package com.example.demo;

import com.example.demo.entity.User;

import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
    private final com.example.demo.repository.UserRepository userRepository;

    public DemoApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);}
    @Bean
    CommandLineRunner test() {
        return args -> {

            User user1 = new User("teo1", "iuh");
            User user2 = new User("teo2", "iuh");
            User user3 = new User("teo3", "iuh");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

        };
}
}
