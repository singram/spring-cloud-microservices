package srai.composite.service.model;

import srai.common.micro.service.model.Person;
import srai.common.micro.service.model.Recommendation;

import java.util.List;

public class PersonComposite {

  private Person person;

  private List<Recommendation> personRecommendations;

  private List<Recommendation> productRecommendations;

  /**
   * @return the person
   */
  public Person getPerson() {
    return person;
  }

  /**
   * @param person the person to set
   */
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * @return the personRecommendations
   */
  public List<Recommendation> getPersonRecommendations() {
    return personRecommendations;
  }

  /**
   * @param personRecommendations the personRecommendations to set
   */
  public void setPersonRecommendations(List<Recommendation> personRecommendations) {
    this.personRecommendations = personRecommendations;
  }

  /**
   * @return the productRecommendations
   */
  public List<Recommendation> getProductRecommendations() {
    return productRecommendations;
  }

  /**
   * @param productRecommendations the productRecommendations to set
   */
  public void setProductRecommendations(List<Recommendation> productRecommendations) {
    this.productRecommendations = productRecommendations;
  }

}
