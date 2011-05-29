package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "address", catalog = "hyperPath", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"street", "zip", "city", "department", "country", "ext"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll"            , query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findById"           , query = "SELECT a FROM Address a WHERE a.id = :id"),
    @NamedQuery(name = "Address.findByStreet"       , query = "SELECT a FROM Address a WHERE a.street = :street"),
    @NamedQuery(name = "Address.findByZip"          , query = "SELECT a FROM Address a WHERE a.zip = :zip"),
    @NamedQuery(name = "Address.findByCity"         , query = "SELECT a FROM Address a WHERE a.city = :city"),
    @NamedQuery(name = "Address.findByDepartment"   , query = "SELECT a FROM Address a WHERE a.department = :department"),
    @NamedQuery(name = "Address.findByCountry"      , query = "SELECT a FROM Address a WHERE a.country = :country"),
    @NamedQuery(name = "Address.findByExt"          , query = "SELECT a FROM Address a WHERE a.ext = :ext"),
    @NamedQuery(name = "Address.findByAll"          , query = "SELECT a FROM Address a WHERE a.street = :street and a.zip = :zip and a.city = :city and a.department = :department and a.coutry = :country and a.ext = :ext"),
    @NamedQuery(name = "Address.findAdvertiserByAddress" , query = "select c.* from hyperPath.advertisers c inner join hyperPath.entities_has_address e on c.entities_id = e.entities_id where e.address_id :addressId)"),
    @NamedQuery(name = "Address.findClientByAddress"     , query = "select c.* from hyperPath.clients c inner join hyperPath.entities_has_address e on c.entities_id = e.entities_id where e.address_id = :addressId)")
})
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "street", nullable = false, length = 45)
    private String street;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "zip", nullable = false, length = 45)
    private String zip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "city", nullable = false, length = 45)
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "department", nullable = false, length = 45)
    private String department;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "country", nullable = false, length = 45)
    private String country;
    @Size(max = 45)
    @Column(name = "ext", length = 45)
    private String ext;
    @ManyToMany(mappedBy = "addressList")
    private List<Entities> entitiesList;

    public Address() {
    }

    public Address(Integer id) {
        this.id = id;
    }

    public Address(Integer id, String street, String zip, String city, String department, String country) {
        this.id = id;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.department = department;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @XmlTransient
    public List<Entities> getEntitiesList() {
        return entitiesList;
    }

    public void setEntitiesList(List<Entities> entitiesList) {
        this.entitiesList = entitiesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.hyperpath.persistence.entities.Address[ id=" + id + " ]";
    }
    
}
