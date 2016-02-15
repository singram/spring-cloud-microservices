package srai.composite.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.model.Person;
import srai.common.micro.service.model.Recommendation;
import srai.composite.service.model.PersonComposite;
import srai.composite.service.service.PersonService;

import java.util.Arrays;
import java.util.Calendar;

@RestController
public class PersonController {

  private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

  @Autowired
  PersonService integration;

  @RequestMapping("/")
  public String getProduct() {
    return "{\"timestamp\":\"" + Calendar.getInstance().getTime() + "\",\"content\":\"Hello from PersonAPi\"}";
  }

  @RequestMapping("/{personId}")
  public ResponseEntity<PersonComposite> getProduct(@PathVariable int personId) {

    // 1. First get mandatory product information
    ResponseEntity<Person> personResult = integration.getPerson(personId);

    if (!personResult.getStatusCode().is2xxSuccessful()) {
      LOG.error("Could not retrieve person {} from person-service.  Http code - ", personId, personResult.getStatusCode());
      return new ResponseEntity<>(null, personResult.getStatusCode());
    }

    // 2. Get optional person recommendations
    Recommendation[] personRecommendations = null;
    try {
      ResponseEntity<Recommendation[]> recommendationResult = integration.getPersonRecommendations(personId);
      personRecommendations = recommendationResult.getBody();
    } catch (Throwable t) {
      LOG.error("getPersonRecommendations error", t);
      throw t;
    }

    // 3. Get optional product recommendations
    Recommendation[] productRecommendations = null;
    try {
      ResponseEntity<Recommendation[]> recommendationResult = integration.getProductRecommendations(personId);
      productRecommendations = recommendationResult.getBody();
    } catch (Throwable t) {
      LOG.error("getProductRecommendations error", t);
      throw t;
    }

    PersonComposite result = new PersonComposite();
    result.setPerson(personResult.getBody());
    result.setPersonRecommendations(Arrays.asList(personRecommendations));
    result.setProductRecommendations(Arrays.asList(productRecommendations));
    return new ResponseEntity<PersonComposite>(result, personResult.getStatusCode());
  }

}
