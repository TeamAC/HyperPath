package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "clients", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Clients implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  private Integer           Id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "name", nullable = false, length = 45)
  private String            name;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "lastName", nullable = false, length = 45)
  private String            lastName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 45)
  @Column(name = "login", nullable = false, length = 45)
  private String            login;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 64)
  @Column(name = "password", nullable = false, length = 64)
  private String            password;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 6)
  @Column(name = "gender", nullable = false, length = 6)
  private String            gender;
  @Basic(optional = false)
  @NotNull
  @Column(name = "birthdate", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date              birthdate;
  @JoinTable(name = "clients_reviews", joinColumns = { @JoinColumn(name = "clients_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "reviews_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Reviews>     reviewsList;
  @JoinTable(name = "clients_bookmarked_services", joinColumns = { @JoinColumn(name = "clients_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "services_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Services>    servicesList;
  @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Entities          entities;

  public Clients() {
  }

  public Clients(Integer id) {
    this.Id = id;
  }

  public Clients(Integer id, String name, String lastName, String login,
      String password, String gender, Date birthdate) {
    this.Id = id;
    this.name = name;
    this.lastName = lastName;
    this.login = login;
    this.password = password;
    this.gender = gender;
    this.birthdate = birthdate;
  }

  public Integer getClientsPK() {
    return Id;
  }

  public void setClientsPK(Integer id) {
    this.Id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }

  @XmlTransient
  public List<Reviews> getReviewsList() {
    return reviewsList;
  }

  public void setReviewsList(List<Reviews> reviewsList) {
    this.reviewsList = reviewsList;
  }

  @XmlTransient
  public List<Services> getServicesList() {
    return servicesList;
  }

  public void setServicesList(List<Services> servicesList) {
    this.servicesList = servicesList;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities(Entities entities) {
    this.entities = entities;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (Id != null ? Id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are
    // not set
    if (!(object instanceof Clients)) {
      return false;
    }
    Clients other = (Clients) object;
    if ((this.Id == null && other.Id != null)
        || (this.Id != null && !this.Id.equals(other.Id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.hyperpath.persistence.entities.Clients[ Id=" + Id + " ]";
  }

}
