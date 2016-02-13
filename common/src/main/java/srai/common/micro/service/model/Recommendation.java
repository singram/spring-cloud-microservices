package srai.common.micro.service.model;

/** Recommendation data model. */
public class Recommendation extends CommonBaseModel {

  /** Recommendation data. */
  private String recommendation = "";

  private long weight = 0;

  public Recommendation() {
  }

  public Recommendation(String recommendation) {
    super();
    this.recommendation = recommendation;
    this.weight = ((long) (Math.random() * (100)));
  }

  /** Recommendation getter. */
  public String getRecommendation() {
    return recommendation;
  }

  /** Recommendation setter. */
  public void setRecommendation(final String recommendation) {
    this.recommendation = recommendation;
  }

  /**
   * @return the weight
   */
  public long getWeight() {
    return weight;
  }

  /**
   * @param weight the weight to set
   */
  public void setWeight(long weight) {
    this.weight = weight;
  }

}
