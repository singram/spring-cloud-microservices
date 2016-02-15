package srai.micro.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Cachable {

  @JsonIgnore
  public String getKey();

  @JsonIgnore
  public String getObjectKey();

}
