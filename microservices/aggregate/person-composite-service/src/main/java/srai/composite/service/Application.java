package srai.composite.service;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@ComponentScan({"srai.common.micro.service.util", "srai.composite.service"})
@EnableDiscoveryClient
@EnableCircuitBreaker
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
  @Bean
  HystrixMetricsPublisher hystrixMetricsPublisher(MetricRegistry metricRegistry) {
    HystrixCodaHaleMetricsPublisher publisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);
    HystrixPlugins.getInstance().registerMetricsPublisher(publisher);
    return publisher;
  }

  //  @Bean
  //  public ConsoleReporter consoleReporter(MetricRegistry metricRegistry) {
  //    ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
  //        .convertRatesTo(TimeUnit.SECONDS)
  //        .convertDurationsTo(TimeUnit.MILLISECONDS)
  //        .build();
  //    reporter.start(1, TimeUnit.SECONDS);
  //    return reporter;
  //  }

  @Bean
  public GraphiteReporter graphiteReporter(MetricRegistry metricRegistry) {
    final GraphiteReporter reporter = GraphiteReporter
        .forRegistry(metricRegistry)
        .prefixedWith("composite_service")
        .build(graphite());
    reporter.start(1, TimeUnit.SECONDS);
    return reporter;
  }

  @Bean
  GraphiteSender graphite() {
    return new Graphite(new InetSocketAddress("grafana", 2003));
  }

  @Bean
  @Primary
  public DropwizardMetricServices dropwizardMetricServices(MetricRegistry metricRegistry){
    return new DropwizardMetricServices(metricRegistry);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /** Main entry point. */
  public static void main(final String... args) {
    SpringApplication.run(Application.class, args);
  }
}
