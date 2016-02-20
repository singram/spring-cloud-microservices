package srai.micro.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.controller.ManagedResponseControllerBase;
import srai.common.micro.service.model.Recommendation;

import java.util.HashSet;
import java.util.Set;

@RestController
public class ProductRecommendationController extends ManagedResponseControllerBase {

  /**
   * Sample usage: curl $HOST:$PORT/recommendations/1
   *
   * @param personId
   * @return
   */
  @RequestMapping(value = "/recommendations/{personId}", method = RequestMethod.GET)
  @ResponseBody public Set<Recommendation> getPerson(@PathVariable int personId) {
    logger.info("/recommendations called");
    final int pt = controlResponseTimeAndError();
    Set<Recommendation> recommendations = new HashSet<Recommendation>();
    recommendations.add(new Recommendation("product recommendation 1"));
    recommendations.add(new Recommendation("product recommendation 2"));
    recommendations.add(new Recommendation("product recommendation 3"));
    logger.debug("/recommendations return the found person, processing time: {}", pt);
    return recommendations;
  }

}
