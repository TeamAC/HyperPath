package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ads", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Ads implements Serializable {
  private static final long serialVersionUID = 1821980702014258109L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id", nullable = false)
  private Integer           Id;
  @Basic(optional = false)
  @NotNull
  @Lob
  @Size(min = 1, max = 65535)
  @Column(name = "description", nullable = false, length = 65535)
  private String            description;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "shortDescription", nullable = false, length = 45)
  private String            shortDescription;
  @Basic(optional = false)
  @NotNull
  @Column(name = "startDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              startDate;
  @Basic(optional = false)
  @NotNull
  @Column(name = "endDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              endDate;
  @JoinColumn(name = "advertisers_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Advertisers       advertisers;
  @JoinColumn(name = "services_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Services          services;

  public Ads() {
  }

  public Ads(Integer id) {
    this.Id = id;
  }

  public Ads(Integer id, String description, String shortDescription,
      Date startDate, Date endDate) {
    this.Id = id;
    this.description = description;
    this.shortDescription = shortDescription;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Integer getAdsPK() {
    return Id;
  }

  public void setAdsPK(Integer id) {
    this.Id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Advertisers getAdvertisers() {
    return advertisers;
  }

  public void setAdvertisers(Advertisers advertisers) {
    this.advertisers = advertisers;
  }

  public Services getServices() {
    return services;
  }

  public void setServices(Services services) {
    this.services = services;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (Id != null ? Id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Ads)) {
      return false;
    }
    Ads other = (Ads) object;
    if ((this.Id == null && other.Id != null)
        || (this.Id != null && !this.Id.equals(other.Id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.hyperpath.persistence.entities.Ads[ Id=" + Id + " ]";
  }

}
