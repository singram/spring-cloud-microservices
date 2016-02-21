
package srai.micro.service.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableAutoConfiguration
//@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbine
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
