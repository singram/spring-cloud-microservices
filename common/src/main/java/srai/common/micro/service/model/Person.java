package srai.common.micro.service.model;

/** Person data model. */
public class Person extends CommonBaseModel {

  /** First name. */
  private String firstName;

  /** Last name. */
  private String lastName;

  public Person() {
  }

  public Person(final String firstName, final String lastName) {
    this(0, firstName, lastName);
  }

  public Person(final long id, final String firstName, final String lastName) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /** First name getter. */
  public String getFirstName() {
    return this.firstName;
  }

  /** First name setter. */
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /** Last name getter. */
  public String getLastName() {
    return this.lastName;
  }

  /** Last name setter. */
  public void setLastName(final String lastname) {
    this.lastName = lastname;
  }

  /** String representation. */
  @Override
  public String toString() {
    return "Person [id=" + this.id + ", firstName=" + this.firstName + ", lastName=" + this.lastName
        + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() +"]";
  }

}
