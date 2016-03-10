package srai.composite.service.gateway.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import srai.common.micro.service.model.Person;

@FeignClient("person-service")
public interface PersonService {
  @RequestMapping(method = RequestMethod.GET, value = "/person/{personId}")
  ResponseEntity<Person> getPerson(@PathVariable("personId") int personId);
}

