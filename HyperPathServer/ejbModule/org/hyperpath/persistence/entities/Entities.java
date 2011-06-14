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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "entities", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Entities implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id", nullable = false)
  private Integer           id;
  @JoinTable(name = "entities_has_emails", joinColumns = { @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "emails_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Emails>      emailsList;
  @JoinTable(name = "entities_has_faxes", joinColumns = { @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "faxes_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Faxes>       faxesList;
  @JoinTable(name = "entities_has_phones", joinColumns = { @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "phones_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Phones>      phonesList;
  @JoinTable(name = "entities_has_address", joinColumns = { @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false) })
  @ManyToMany
  private List<Address>     addressList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "entities")
  private List<Advertisers> advertisersList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "entities")
  private List<Services>    servicesList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "entities")
  private List<Clients>     clientsList;

  public Entities() {
  }

  public Entities(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @XmlTransient
  public List<Emails> getEmailsList() {
    return emailsList;
  }

  public void setEmailsList(List<Emails> emailsList) {
    this.emailsList = emailsList;
  }

  @XmlTransient
  public List<Faxes> getFaxesList() {
    return faxesList;
  }

  public void setFaxesList(List<Faxes> faxesList) {
    this.faxesList = faxesList;
  }

  @XmlTransient
  public List<Phones> getPhonesList() {
    return phonesList;
  }

  public void setPhonesList(List<Phones> phonesList) {
    this.phonesList = phonesList;
  }

  @XmlTransient
  public List<Address> getAddressList() {
    return addressList;
  }

  public void setAddressList(List<Address> addressList) {
    this.addressList = addressList;
  }

  @XmlTransient
  public List<Advertisers> getAdvertisersList() {
    return advertisersList;
  }

  public void setAdvertisersList(List<Advertisers> advertisersList) {
    this.advertisersList = advertisersList;
  }

  @XmlTransient
  public List<Services> getServicesList() {
    return servicesList;
  }

  public void setServicesList(List<Services> servicesList) {
    this.servicesList = servicesList;
  }

  @XmlTransient
  public List<Clients> getClientsList() {
    return clientsList;
  }

  public void setClientsList(List<Clients> clientsList) {
    this.clientsList = clientsList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Entities)) {
      return false;
    }
    Entities other = (Entities) object;
    if ((this.id == null && other.id != null)
        || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.hyperpath.persistence.entities.Entities[ id=" + id + " ]";
  }

}
