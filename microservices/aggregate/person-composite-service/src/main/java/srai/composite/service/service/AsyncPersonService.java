package srai.composite.service.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import srai.common.micro.service.model.Person;
import srai.common.micro.service.model.Recommendation;

import java.util.concurrent.Future;

/**
 * Async Service class responsible for aggregating information from the 3 micro services
 *   - person service
 *   - person recommendation service
 *   - person recommendation service
 */
@Component
public class AsyncPersonService extends PersonService {

  private static final Logger LOG = LoggerFactory.getLogger(AsyncPersonService.class);

  @HystrixCommand(fallbackMethod = "defaultPerson"
      , groupKey="personRateLimiter", threadPoolProperties = { @HystrixProperty(name = "coreSize", value = "5") }
      )
  public Future<ResponseEntity<Person>> getPersonAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Person>>() {
      @Override
      public ResponseEntity<Person> invoke() {
        return getPerson(personId);
      }
    };
  }

  public ResponseEntity<Person> defaultPerson(int persontId, Throwable e) {
    LOG.warn("Using fallback method for person-service. {}", e.getMessage());
    return super.defaultPerson(persontId);
  }

  // ---------------------- //
  // PERSON RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendationsAsync"
      , commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000") }
      )
  public Future<ResponseEntity<Recommendation[]>> getPersonRecommendationsAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Recommendation[]>>() {
      @Override
      public ResponseEntity<Recommendation[]> invoke() {
        return getPersonRecommendations(personId);
      }
    };
  }

  public ResponseEntity<Recommendation[]> defaultPersonRecommendationsAsync(int persontId, Throwable e) {
    LOG.warn("Using fallback method for person-recommendation-service. {}", e.getMessage());
    return defaultPersonRecommendations(persontId);
  }

  // ---------------------- //
  // PRODUCT RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultProductRecommendationsAsync")
  public Future<ResponseEntity<Recommendation[]>> getProductRecommendationsAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Recommendation[]>>() {
      @Override
      public ResponseEntity<Recommendation[]> invoke() {
        return getProductRecommendations(personId);
      }
    };
  }

  public ResponseEntity<Recommendation[]> defaultProductRecommendationsAsync(int persontId, Throwable e) {
    LOG.warn("Using fallback method for product-recommendation-service. {}", e.getMessage());
    return defaultProductRecommendations(persontId);
  }

}