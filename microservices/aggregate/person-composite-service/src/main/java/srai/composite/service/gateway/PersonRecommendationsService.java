package srai.composite.service.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import srai.common.micro.service.model.Recommendation;

import java.util.concurrent.Future;

@Service
public class PersonRecommendationsService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonRecommendationsService.class);

  @Autowired
  protected RestOperations restTemplate;

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendations")
  public ResponseEntity<Recommendation[]> getPersonRecommendations(int personId) {
    String url = "http://person-recommendation-service:8080/recommendations/" + personId;
    LOG.info("GetPersonRecommendations from URL: {}", url);

    ResponseEntity<Recommendation[]> svcResult = restTemplate.getForEntity(url, Recommendation[].class);
    LOG.debug("GetPersonRecommentation http-status: {}", svcResult.getStatusCode());
    LOG.debug("GetPersonRecommentation.recommentdation count: {}", svcResult.getBody().length);

    return svcResult;
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
