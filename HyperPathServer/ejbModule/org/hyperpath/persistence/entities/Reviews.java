package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "reviews", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Reviews implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id", nullable = false)
  private Integer           id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "rating", nullable = false)
  private int               rating;
  @Basic(optional = false)
  @NotNull
  @Lob
  @Size(min = 1, max = 65535)
  @Column(name = "description", nullable = false, length = 65535)
  private String            description;
  @ManyToMany(mappedBy = "reviewsList")
  private List<Clients>     clientsList;
  @ManyToMany(mappedBy = "reviewsList")
  private List<Services>    servicesList;

  public Reviews() {
  }

  public Reviews(Integer id) {
    this.id = id;
  }

  public Reviews(Integer id, int rating, String description) {
    this.id = id;
    this.rating = rating;
    this.description = description;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlTransient
  public List<Clients> getClientsList() {
    return clientsList;
  }

  public void setClientsList(List<Clients> clientsList) {
    this.clientsList = clientsList;
  }

  @XmlTransient
  public List<Services> getServicesList() {
    return servicesList;
  }

  public void setServicesList(List<Services> servicesList) {
    this.servicesList = servicesList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Reviews)) {
      return false;
    }
    Reviews other = (Reviews) object;
    if ((this.id == null && other.id != null)
        || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.hyperpath.persistence.entities.Reviews[ id=" + id + " ]";
  }

}
