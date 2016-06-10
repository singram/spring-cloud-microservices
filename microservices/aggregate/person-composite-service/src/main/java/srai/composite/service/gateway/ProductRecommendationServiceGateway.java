package srai.composite.service.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import srai.common.micro.service.model.Recommendation;
import srai.composite.service.gateway.service.ProductRecommendationService;

import java.util.concurrent.Future;

@Service
public class ProductRecommendationServiceGateway {

  private static final Logger LOG = LoggerFactory.getLogger(ProductRecommendationServiceGateway.class);

  @Autowired
  private ProductRecommendationService productRecommendationService;

  @HystrixCommand(fallbackMethod = "defaultProductRecommendations")
  public ResponseEntity<Recommendation[]> getProductRecommendationsSP(int personId) {
    return productRecommendationService.getRecommendationsSP(personId);
  }

  @HystrixCommand(fallbackMethod = "defaultProductRecommendations")
  public ResponseEntity<Recommendation[]> getProductRecommendations(int personId) {
    return productRecommendationService.getRecommendations(personId);
  }

  @HystrixCommand(fallbackMethod = "defaultProductRecommendations")
  public Future<ResponseEntity<Recommendation[]>> getProductRecommendationsAsync(final int personId) {
    return new AsyncResult<ResponseEntity<Recommendation[]>>() {
      @Override
      public ResponseEntity<Recommendation[]> invoke() {
        return getProductRecommendations(personId);
      }
    };
  }

  public ResponseEntity<Recommendation[]> defaultProductRecommendations(int persontId) {
    LOG.warn("Using fallback method for product-recommendations-service");
    Recommendation[] emptyArray = {};
    return new ResponseEntity<Recommendation[]>(emptyArray, HttpStatus.BAD_GATEWAY);
  }

}
