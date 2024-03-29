package org.eoghancorp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.eoghancorp"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}