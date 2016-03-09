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

import srai.common.micro.service.model.Person;

import java.util.concurrent.Future;

@Service
public class PersonService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

  @Autowired
  protected RestOperations restTemplate;

  //  @Autowired
  //  private LoadBalancerClient loadBalancer;

  @HystrixCommand(fallbackMethod = "defaultPerson")
  public ResponseEntity<Person> getPerson(int personId) {
    //    ServiceInstance instance = loadBalancer.choose(serviceId);
    //    String url = "http://person-service:8080/person/" + personId;
    String url = "http://person-service/person/" + personId;
    LOG.info("Getperson from URL: {}", url);

    ResponseEntity<Person> svcResult = restTemplate.getForEntity(url, Person.class);
    LOG.debug("Getperson http-status: {}", svcResult.getStatusCode());
    LOG.debug("Getperson.id: {}", svcResult.getBody().getId());

    return svcResult;
  }

  @HystrixCommand(fallbackMethod = "defaultPerson"
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

  public ResponseEntity<Person> defaultPerson(int persontId) {
    LOG.warn("Using fallback method for person-service {} ");
    return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
  }

}
