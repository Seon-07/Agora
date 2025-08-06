package com.seon.moca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.seon.moca", "com.seon.common"})
public class MocaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MocaApplication.class, args);
    }

}
