package srai.composite.service.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  /*
  @HystrixCommand(fallbackMethod = "defaultList")
  public Future<ArrayList<String>> getStringList(final int someId) {
    return new AsyncResult<ArrayList<String>>() {
      @Override
      public ArrayList<String> invoke() {
        return new ArrayList<String>();
      }
    };
  }

  public ArrayList<String> defaultList(final int someId) {
    return null;
  }
   */

  @HystrixCommand(fallbackMethod = "defaultPerson")
  public Future<Person> getPersonAsync(final int personId) {
    return new AsyncResult<Person>() {
      @Override
      public Person invoke() {
        return getPerson(personId).getBody();
      }
    };
  }

  public Person defaultPerson(int persontId, Throwable e) {
    LOG.warn("Using fallback method for person-service. {}", e.getMessage());
    return null;
  }

  // ---------------------- //
  // PERSON RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendationsAsync")
  public Future<Recommendation[]> getPersonRecommendationsAsync(final int personId) {
    return new AsyncResult<Recommendation[]>() {
      @Override
      public Recommendation[] invoke() {
        return getPersonRecommendations(personId).getBody();
      }
    };
  }

  public Recommendation[] defaultPersonRecommendationsAsync(int persontId) {
    return defaultPersonRecommendations(persontId).getBody();
  }

  // ---------------------- //
  // PRODUCT RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultProductRecommendationsAsync")
  public Future<Recommendation[]> getProductRecommendationsAsync(final int personId) {
    return new AsyncResult<Recommendation[]>() {
      @Override
      public Recommendation[] invoke() {
        return getProductRecommendations(personId).getBody();
      }
    };
  }

  public Recommendation[] defaultProductRecommendationsAsync(int persontId) {
    return defaultProductRecommendations(persontId).getBody();
  }

}