package com.welpeth.kakebo.financier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancierApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinancierApplication.class, args);
  }

}
