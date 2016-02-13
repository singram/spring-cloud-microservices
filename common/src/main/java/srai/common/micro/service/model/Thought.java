package srai.common.micro.service.model;

/** Thought data model. */
public class Thought extends CommonBaseModel {

  /** Person thought belongs to. */
  private Person person;

  /** Thought data. */
  private String data = "";

  public Thought(String data) {
    super();
    this.data = data;
  }

  /** Data getter. */
  public String getData() {
    return data;
  }

  /** Data setter. */
  public void setData(final String data) {
    this.data = data;
  }

  /** Person getter. */
  public Person getPerson() {
    return person;
  }

  /** Person setter. */
  public void setPerson(final Person person) {
    this.person = person;
  }

}
