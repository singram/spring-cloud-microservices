package srai.micro.service.model;

public class CachablePerson extends srai.common.micro.service.model.Person implements Cachable {

  private static final long serialVersionUID = 347915415730767126L;

  public static final String OBJECT_KEY = "PERSON";

  public CachablePerson() {
    super();
  }

  public CachablePerson(long id) {
    super();
    this.id = id;
  }

  @Override
  public String getKey() {
    return String.valueOf(getId());
  }

  @Override
  public String getObjectKey() {
    return OBJECT_KEY;
  }

}
