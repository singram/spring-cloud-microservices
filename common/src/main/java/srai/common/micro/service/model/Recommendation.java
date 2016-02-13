package srai.common.micro.service.model;

import java.util.Calendar;

/** Recommendation data model. */
public class Recommendation extends CommonBaseModel {

  /** Recommendation data. */
  private String recommendation = "";

  private long weight = 0;

  public Recommendation(String recommendation) {
    this.id = (long) (Math.random() * (10000));
    this.recommendation = recommendation;
    this.weight = ((long) (Math.random() * (100)));
    this.setCreatedAt(Calendar.getInstance().getTime());
    this.setUpdatedAt(Calendar.getInstance().getTime());
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
