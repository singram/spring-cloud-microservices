package srai.composite.service.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

  @HystrixCommand(fallbackMethod = "defaultAsyncPerson"
      //      , groupKey="personRateLimiter", threadPoolProperties = { @HystrixProperty(name = "coreSize", value = "5") }
      )
  public Future<ResponseEntity<Person>> getPersonAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Person>>() {
      @Override
      public ResponseEntity<Person> invoke() {
        return getPerson(personId);
      }
    };
  }

  public ResponseEntity<Person> defaultAsyncPerson(int persontId, Throwable t) {
    LOG.warn("Using fallback method for async-person-service. {}", t.getMessage());
    return defaultPerson(persontId, t);
  }

  // ---------------------- //
  // PERSON RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendationsAsync"
      //      , commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500") }
      )
  public Future<ResponseEntity<Recommendation[]>> getPersonRecommendationsAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Recommendation[]>>() {
      @Override
      public ResponseEntity<Recommendation[]> invoke() {
        return getPersonRecommendations(personId);
      }
    };
  }

  public ResponseEntity<Recommendation[]> defaultPersonRecommendationsAsync(int persontId, Throwable t) {
    LOG.warn("Using fallback method for person-recommendation-service. {}", t.getMessage());
    return defaultPersonRecommendations(persontId, t);
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

  public ResponseEntity<Recommendation[]> defaultProductRecommendationsAsync(int persontId, Throwable t) {
    LOG.warn("Using fallback method for product-recommendation-service. {}", t.getMessage());
    return defaultProductRecommendations(persontId, t);
  }

}