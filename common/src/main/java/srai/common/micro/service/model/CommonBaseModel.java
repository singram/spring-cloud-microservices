package srai.common.micro.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Calendar;
import java.util.Date;

/** Abstract base model including primary id and
 * created_at and updated_at time fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
    this.id = (long) (Math.random() * (10000));
    this.setCreatedAt(Calendar.getInstance().getTime());
    this.setUpdatedAt(Calendar.getInstance().getTime());
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
