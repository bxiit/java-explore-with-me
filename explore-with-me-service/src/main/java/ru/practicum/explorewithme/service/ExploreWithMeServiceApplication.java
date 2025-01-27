package ru.practicum.explorewithme.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.explorewithme")
public class ExploreWithMeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeServiceApplication.class, args);
    }

}
