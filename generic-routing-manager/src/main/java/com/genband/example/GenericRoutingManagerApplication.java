package com.genband.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class GenericRoutingManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(GenericRoutingManagerApplication.class, args);

  }
}
