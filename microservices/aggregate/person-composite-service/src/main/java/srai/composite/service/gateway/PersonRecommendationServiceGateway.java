package srai.composite.service.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import srai.common.micro.service.model.Recommendation;
import srai.composite.service.gateway.service.PersonRecommendationService;

import java.util.concurrent.Future;

@Service
public class PersonRecommendationServiceGateway {

  private static final Logger LOG = LoggerFactory.getLogger(PersonRecommendationServiceGateway.class);

  @Autowired
  private PersonRecommendationService personRecommendationService;

  //@HystrixCommand(threadPoolKey="SpecialPool",
  //    fallbackMethod = "defaultPersonRecommendations",
  //        commandProperties = { @HystrixProperty( name="execution.isolation.strategy", value="SEMAPHORE"),
  //          @HystrixProperty( name="execution.isolation.semaphore.maxConcurrentRequests", value="20" } ,
  //    commandProperties = { @HystrixProperty( name="execution.isolation.strategy", value="THREAD") } ,
  //    threadPoolProperties = { @HystrixProperty(name = "coreSize", value = "30") })
  @HystrixCommand(fallbackMethod = "defaultPersonRecommendations")
  public ResponseEntity<Recommendation[]> getPersonRecommendationsSP(int personId) {
    return personRecommendationService.getRecommendationsSP(personId);
  }

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendations")
  public ResponseEntity<Recommendation[]> getPersonRecommendations(int personId) {
    return personRecommendationService.getRecommendations(personId);
  }

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendations"
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

  public ResponseEntity<Recommendation[]> defaultPersonRecommendations(int persontId) {
    LOG.warn("Using fallback method for person-recommendations-service");
    Recommendation[] emptyArray = {};
    return new ResponseEntity<Recommendation[]>(emptyArray, HttpStatus.BAD_GATEWAY);
  }

}
