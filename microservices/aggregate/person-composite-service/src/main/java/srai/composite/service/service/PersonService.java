package srai.composite.service.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import srai.common.micro.service.model.Person;
import srai.common.micro.service.model.Recommendation;

/**
 * Service class responsible for aggregating information from the 3 micro services
 *   - person service
 *   - person recommendation service
 *   - person recommendation service
 */
@Component
public class PersonService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

  @Autowired
  protected RestOperations restTemplate;


  // -------- //
  // personS //
  // -------- //

  @HystrixCommand(fallbackMethod = "defaultPerson")
  public ResponseEntity<Person> getPerson(int personId) {
    String url = "http://person-service:8080/person/" + personId;
    LOG.info("Getperson from URL: {}", url);

    ResponseEntity<Person> svcResult = restTemplate.getForEntity(url, Person.class);
    LOG.debug("Getperson http-status: {}", svcResult.getStatusCode());
    LOG.debug("Getperson.id: {}", svcResult.getBody().getId());

    return svcResult;
  }

  public ResponseEntity<Person> defaultPerson(int persontId) {
    LOG.warn("Using fallback method for person-service");
    return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
  }

  // ---------------------- //
  // PERSON RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultPersonRecommendations")
  public ResponseEntity<Recommendation[]> getPersonRecommendations(int personId) {
    String url = "http://person-recommendation-service:8080/recommendations/" + personId;
    LOG.info("GetPersonRecommendations from URL: {}", url);

    ResponseEntity<Recommendation[]> svcResult = restTemplate.getForEntity(url, Recommendation[].class);
    LOG.debug("GetPersonRecommentation http-status: {}", svcResult.getStatusCode());
    LOG.debug("GetPersonRecommentation.recommentdation count: {}", svcResult.getBody().length);

    return svcResult;
  }

  public ResponseEntity<Recommendation[]> defaultPersonRecommendations(int persontId) {
    LOG.warn("Using fallback method for person-recommendations-service");
    Recommendation[] emptyArray = {};
    return new ResponseEntity<Recommendation[]>(emptyArray, HttpStatus.BAD_GATEWAY);
  }

  // ---------------------- //
  // PRODUCT RECOMMENDATIONS //
  // ---------------------- //

  @HystrixCommand(fallbackMethod = "defaultProductRecommendations")
  public ResponseEntity<Recommendation[]> getProductRecommendations(int personId) {
    String url = "http://product-recommendation-service:8080/recommendations/" + personId;
    LOG.info("GetProductRecommendations from URL: {}", url);

    ResponseEntity<Recommendation[]> svcResult = restTemplate.getForEntity(url, Recommendation[].class);
    LOG.debug("GetProductRecommentation http-status: {}", svcResult.getStatusCode());
    LOG.debug("GetProductRecommentation.recommentdation count: {}", svcResult.getBody().length);

    return svcResult;
  }

  public ResponseEntity<Recommendation[]> defaultProductRecommendations(int persontId) {
    LOG.warn("Using fallback method for product-recommendations-service");
    Recommendation[] emptyArray = {};
    return new ResponseEntity<Recommendation[]>(emptyArray, HttpStatus.BAD_GATEWAY);
  }


}