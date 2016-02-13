package srai.composite.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private RestOperations restTemplate;


  // -------- //
  // personS //
  // -------- //

  public ResponseEntity<Person> getPerson(int personId) {
    String url = "http://person-service:8080/person/" + personId;
    LOG.info("Getperson from URL: {}", url);

    ResponseEntity<Person> svcResult = restTemplate.getForEntity(url, Person.class);
    LOG.debug("Getperson http-status: {}", svcResult.getStatusCode());
    LOG.debug("Getperson.id: {}", svcResult.getBody().getId());

    return svcResult;
  }

  // ---------------------- //
  // PERSON RECOMMENDATIONS //
  // ---------------------- //

  public ResponseEntity<Recommendation[]> getPersonRecommendations(int personId) {
    String url = "http://person-recommendation-service:8080/recommendations/" + personId;
    LOG.debug("GetPersonRecommendations from URL: {}", url);

    ResponseEntity<Recommendation[]> svcResult = restTemplate.getForEntity(url, Recommendation[].class);
    LOG.debug("GetPersonRecommentation http-status: {}", svcResult.getStatusCode());
    LOG.debug("GetPersonRecommentation.recommentdation count: {}", svcResult.getBody().length);

    return svcResult;
  }

  // ---------------------- //
  // PRODUCT RECOMMENDATIONS //
  // ---------------------- //

  public ResponseEntity<Recommendation[]> getProductRecommendations(int personId) {
    String url = "http://product-recommendation-service:8080/recommendations/" + personId;
    LOG.debug("GetProductRecommendations from URL: {}", url);

    ResponseEntity<Recommendation[]> svcResult = restTemplate.getForEntity(url, Recommendation[].class);
    LOG.debug("GetProductRecommentation http-status: {}", svcResult.getStatusCode());
    LOG.debug("GetProductRecommentation.recommentdation count: {}", svcResult.getBody().length);

    return svcResult;
  }

}