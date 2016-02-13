package srai.common.micro.service.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Person data model. */
public class Person extends CommonBaseModel {

  /** First name. */
  private String firstName;

  /** Last name. */
  private String lastName;

  /** Thoughts a person has. */
  private List<Thought> thoughts = new ArrayList<Thought>();

  /** Children a person has. */
  private Set<Person> children = new HashSet<Person>();

  /** Parent of person. */
  private Person parent;

  public Person(final String firstName, final String lastName) {
    this(0, firstName, lastName);
  }

  public Person(final long id, final String firstName, final String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.setCreatedAt(Calendar.getInstance().getTime());
    this.setUpdatedAt(Calendar.getInstance().getTime());
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
    return "Person [firstName=" + this.firstName + ", lastName=" + this.lastName
        + "]";
  }

  /** Thoughts getter. */
  public List<Thought> getThoughts() {
    return thoughts;
  }

  /** Thoughts setter. */
  public void setThoughts(final List<Thought> thoughts) {
    this.thoughts = thoughts;
  }

  /**
   * Children getter.
   * @return the children.
   */
  public Set<Person> getChildren() {
    return children;
  }

  /**
   * Children setter.
   * @param children the children to set.
   */
  public void setChildren(final Set<Person> children) {
    this.children = children;
  }

  /**
   * Parent getter.
   * @return the parent.
   */
  public Person getParent() {
    return parent;
  }

  /**
   * Parent setter.
   * @param parent the parent to set.
   */
  public void setParent(final Person parent) {
    this.parent = parent;
  }

}
