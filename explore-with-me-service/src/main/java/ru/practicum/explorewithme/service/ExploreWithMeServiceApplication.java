package ru.practicum.explorewithme.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ExploreWithMeServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExploreWithMeServiceApplication.class, args);
//        CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);
//        Category category = Category.builder().name("test1").build();
//        categoryRepository.save(category);
//
//        Category category1 = Category.builder().name("test1").build();
//        categoryRepository.save(category1);
    }

}
