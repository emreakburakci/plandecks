package com.plandecks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlandecksApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlandecksApplication.class, args);
    }

}
