package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "gpslocation", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Gpslocation implements Serializable {
  @Basic(optional = false)
  @NotNull
  @Column(name = "time", nullable = false)
  @Temporal(TemporalType.TIME)
  private Date              time;
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id", nullable = false)
  private Integer           id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "latitude", nullable = false, length = 45)
  private String            latitude;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "longitude", nullable = false, length = 45)
  private String            longitude;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "altitude", nullable = false, length = 45)
  private String            altitude;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "gpslocation")
  private List<Services>    servicesList;

  public Gpslocation() {
  }

  public Gpslocation(Integer id) {
    this.id = id;
  }

  public Gpslocation(Integer id, Date time, String latitude,
      String longitude, String altitude) {
    this.id = id;
    this.time = time;
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getAltitude() {
    return altitude;
  }

  public void setAltitude(String altitude) {
    this.altitude = altitude;
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
    if (!(object instanceof Gpslocation)) {
      return false;
    }
    Gpslocation other = (Gpslocation) object;
    if ((this.id == null && other.id != null)
        || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.hyperpath.persistence.entities.Gpslocation[ id=" + id
        + " ]";
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

}
