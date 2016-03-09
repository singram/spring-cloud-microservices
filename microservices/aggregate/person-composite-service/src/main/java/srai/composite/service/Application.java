package srai.composite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"srai.common.micro.service.util", "srai.composite.service"})
@EnableDiscoveryClient
@EnableCircuitBreaker
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private LoadBalancerClient loadBalancer;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /** Main entry point. */
  public static void main(final String... args) {
    SpringApplication.run(Application.class, args);
  }
}
