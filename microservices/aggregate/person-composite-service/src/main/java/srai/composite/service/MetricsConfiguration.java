package srai.composite.service;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;

import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfiguration {

  @Bean
  HystrixMetricsPublisher hystrixMetricsPublisher(MetricRegistry metricRegistry) {
    HystrixCodaHaleMetricsPublisher publisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);
    HystrixPlugins.getInstance().registerMetricsPublisher(publisher);
    return publisher;
  }

  @Bean
  public MetricRegistry metricRegistry() {
    final MetricRegistry metricRegistry = new MetricRegistry();

    //jvm metrics
    metricRegistry.register("jvm.gc",new GarbageCollectorMetricSet());
    metricRegistry.register("jvm.mem",new MemoryUsageGaugeSet());
    metricRegistry.register("jvm.thread-states",new ThreadStatesGaugeSet());

    return metricRegistry;
  }
  @Bean
  public ConsoleReporter consoleReporter(MetricRegistry metricRegistry) {
    ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
    reporter.start(1, TimeUnit.SECONDS);
    return reporter;
  }

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


}
