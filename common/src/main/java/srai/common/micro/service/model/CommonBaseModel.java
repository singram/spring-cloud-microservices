package srai.common.micro.service.model;

import java.util.Date;

/** Abstract base model including primary id and
 * created_at and updated_at time fields.
 */
public class CommonBaseModel {
  /** Primary id. */
  protected Long id;

  /** Model created at timestamp. */
  private Date createdAt;

  /** Model updated at timestamp. */
  private Date updatedAt;

  /** Constructor. */
  protected CommonBaseModel() {
    // Private constructor to prevent direct instantiation.
  }

  /** Primary id getter. */
  public Long getId() {
    return id;
  }

  /** Primary id setter. */
  public void setId(final Long id) {
    this.id = id;
  }

  /** Created at getter. */
  public Date getCreatedAt() {
    return (Date) createdAt.clone();
  }

  /** Created at setter. */
  public void setCreatedAt(final Date createdAt) {
    this.createdAt = (Date) createdAt.clone();
  }

  /** Updated at getter. */
  public Date getUpdatedAt() {
    return (Date) updatedAt.clone();
  }

  /** Updated at setter. */
  public void setUpdatedAt(final Date updatedAt) {
    this.updatedAt = (Date) updatedAt.clone();
  }

}
