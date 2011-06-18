package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "advertisers", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Advertisers implements Serializable {
  private static final long serialVersionUID = -759891713591446299L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id", nullable = false)
  protected Integer         Id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "name", nullable = false, length = 45)
  private String            name;
  @Size(max = 45)
  @Column(name = "description", length = 45)
  private String            description;
  @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Entities          entities;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisers")
  private List<Ads>         adsList;

  public Advertisers() {
  }

  public Advertisers(Integer Id) {
    this.Id = Id;
  }

  public Advertisers(Integer Id, String name) {
    this.Id = Id;
    this.name = name;
  }

  public Integer getAdvertisersPK() {
    return Id;
  }

  public void setAdvertisersPK(Integer Id) {
    this.Id = Id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities(Entities entities) {
    this.entities = entities;
  }

  @XmlTransient
  public List<Ads> getAdsList() {
    return adsList;
  }

  public void setAdsList(List<Ads> adsList) {
    this.adsList = adsList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (Id != null ? Id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Advertisers)) {
      return false;
    }
    Advertisers other = (Advertisers) object;
    if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Advertisers[ Id=" + Id + " ]";
  }

}
