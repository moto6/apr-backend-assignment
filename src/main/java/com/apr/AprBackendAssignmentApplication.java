package com.apr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AprBackendAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AprBackendAssignmentApplication.class, args);
    }

}
