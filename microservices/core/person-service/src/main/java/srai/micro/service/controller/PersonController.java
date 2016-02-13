package srai.micro.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.controller.ManagedResponseControllerBase;
import srai.common.micro.service.model.Person;

@RestController
public class PersonController extends ManagedResponseControllerBase {

  /**
   * Sample usage: curl $HOST:$PORT/person/1
   *
   * @param personId
   * @return
   */
  @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
  @ResponseBody public Person getPerson(@PathVariable int personId) {
    logger.info("/person called");
    final int pt = controlResponseTime();
    logger.debug("/person return the found person, processing time: {}", pt);
    return new Person(personId, "Frodo", "Bagins");
  }

}
