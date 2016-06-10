package srai.micro.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.controller.ManagedResponseControllerBase;
import srai.micro.service.model.CachablePerson;
import srai.micro.service.model.repository.PersonRepository;

@RestController
public class PersonController extends ManagedResponseControllerBase {

  @Autowired
  PersonRepository personRepository;
  /**
   * Sample usage: curl $HOST:$PORT/person/1
   *
   * @param personId
   * @return
   */
  @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getPerson(@PathVariable long personId, @RequestParam(value="skipProblems", defaultValue="no") String skipProblems) {
    logger.info("/person/{personId} called", personId);
    int pt = 0;
    if (skipProblems != null && skipProblems.equals("no")) {
      pt = controlResponseTimeAndError();
    }
    logger.debug("/person/{personId} return the found person, processing time: {}", personId, pt);
    CachablePerson person = new CachablePerson(personId);
    person = personRepository.get(person);
    if (person == null)
      return new ResponseEntity<CachablePerson>(person, HttpStatus.NOT_FOUND);
    else
      return new ResponseEntity<CachablePerson>(person, HttpStatus.OK);
  }

  @RequestMapping(value = "/person/", method = RequestMethod.POST)
  @ResponseBody public void savePerson(@RequestBody final CachablePerson person) {
    logger.info("/person/ called");
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/ return the created person, processing time: {}", pt);
    personRepository.put(person);
  }

}
