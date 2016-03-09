package srai.micro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"srai.common.micro.service.util", "srai.micro.service"})
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  /** Main entry point. */
  public static void main(final String... args) {
    SpringApplication.run(Application.class, args);
  }
}
