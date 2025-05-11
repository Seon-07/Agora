package com.seon.fairin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.seon.fairin", "com.seon.common"})
public class FairInApplication {

    public static void main(String[] args) {
        SpringApplication.run(FairInApplication.class, args);
    }

}
