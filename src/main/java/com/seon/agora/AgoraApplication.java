package com.seon.agora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.seon.agora", "com.seon.common"})
public class AgoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgoraApplication.class, args);
    }

}
