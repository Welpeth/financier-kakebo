package com.welpeth.kakebo.financier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FinancierApplication {

  public static void main(String[] args) {
    BCryptPasswordEncoder encoder =
        new BCryptPasswordEncoder();

    String hash = encoder.encode("12345678");

    System.out.println(hash);

    SpringApplication.run(FinancierApplication.class, args);
  }

}
