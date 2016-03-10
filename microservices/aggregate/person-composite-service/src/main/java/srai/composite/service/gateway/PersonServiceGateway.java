package srai.composite.service.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import srai.common.micro.service.model.Person;
import srai.composite.service.gateway.service.PersonService;

import java.util.concurrent.Future;

@Service
public class PersonServiceGateway {

  private static final Logger LOG = LoggerFactory.getLogger(PersonServiceGateway.class);

  @Autowired
  private PersonService personService;

  @HystrixCommand(fallbackMethod = "defaultPerson")
  public ResponseEntity<Person> getPerson(int personId) {
    return personService.getPerson(personId);
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
