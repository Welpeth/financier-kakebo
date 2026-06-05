package com.welpeth.kakebo.financier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FinancierApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinancierApplication.class, args);
  }

}
