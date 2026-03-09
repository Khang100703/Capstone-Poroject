package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com") // <--- THÊM DÒNG NÀY (Để tìm Repository)
@EntityScan(basePackages = "com")            // <--- THÊM DÒNG NÀY (Để tìm Entity Recipe)
@EnableScheduling
public class PlcwebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlcwebApplication.class, args);
    }
}