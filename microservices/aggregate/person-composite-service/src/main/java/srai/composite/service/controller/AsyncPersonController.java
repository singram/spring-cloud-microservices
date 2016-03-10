package srai.composite.service.controller;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.model.Person;
import srai.common.micro.service.model.Recommendation;
import srai.composite.service.gateway.PersonRecommendationServiceGateway;
import srai.composite.service.gateway.PersonServiceGateway;
import srai.composite.service.gateway.ProductRecommendationServiceGateway;
import srai.composite.service.model.PersonComposite;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/async")
public class AsyncPersonController {

  private static final Logger LOG = LoggerFactory.getLogger(AsyncPersonController.class);

  @Autowired
  PersonServiceGateway personServiceGateway;

  @Autowired
  PersonRecommendationServiceGateway personRecommendationsServiceGateway;

  @Autowired
  ProductRecommendationServiceGateway productRecommendationsServiceGateway;

  @RequestMapping("/{personId}")
  public ResponseEntity<PersonComposite> getProduct(@PathVariable int personId) throws InterruptedException, ExecutionException {

    Future<ResponseEntity<Person>> personResult = null;
    try {
      personResult = personServiceGateway.getPersonAsync(personId);
    } catch (HystrixRuntimeException e) {
      if (e.getCause().getClass().equals(RejectedExecutionException.class) ) {
        LOG.warn("********************* Person-Service concurrency limit reached *************************)");
        return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
      }
    }
    final Future<ResponseEntity<Recommendation[]>> recommendationPersonResult = personRecommendationsServiceGateway.getPersonRecommendationsAsync(personId);
    final Future<ResponseEntity<Recommendation[]>> recommendationProductResult = productRecommendationsServiceGateway.getProductRecommendationsAsync(personId);

    while(!(personResult.isDone() && recommendationPersonResult.isDone() && recommendationProductResult.isDone() ) ) {
      Thread.sleep(1);
    }

    if (personResult.get().getBody()== null) {
      return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
    }

    final Person person = personResult.get().getBody();
    final Recommendation[] personRecommendations = recommendationPersonResult.get().getBody();
    final Recommendation[] productRecommendations = recommendationProductResult.get().getBody();

    PersonComposite result = new PersonComposite();
    result.setPerson(person);
    result.setPersonRecommendations(Arrays.asList(personRecommendations));
    result.setProductRecommendations(Arrays.asList(productRecommendations));
    return new ResponseEntity<PersonComposite>(result, personResult.get().getStatusCode());
  }

}
