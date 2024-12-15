package com.apexify.logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.apexifyconnect.logic", "com.apexifyconnect.persistence", "com.apexify.logic.config", "com.apexifyconnect.logic.Controller", "com.apexify"})
@ComponentScan(basePackages = {"com.apexifyconnect.Repository", "com.apexifyconnect.DAO", "com.apexify.logic.Service", "com.apexify.logic.config, com.apexify.logic.Controller"})
@EnableJpaRepositories(basePackages = "com.apexifyconnect.Repository")
@EntityScan(basePackages = "com.apexifyconnect.Model")
public class LogicApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogicApplication.class, args);
    }

}


//test
