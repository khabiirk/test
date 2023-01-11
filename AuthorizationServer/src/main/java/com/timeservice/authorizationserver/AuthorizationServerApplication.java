package com.timeservice.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Launch Spring and make sure the annotated controllers are detected.
 */
@SpringBootApplication
public class AuthorizationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthorizationServerApplication.class, args);
  }

}
