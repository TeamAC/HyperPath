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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "services", catalog = "hyperPath", schema = "")
@XmlRootElement
public class Services implements Serializable {
  private static final long serialVersionUID = 6799678850975228797L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "label", nullable = false, length = 45)
    private String label;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description", nullable = false, length = 45)
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating", nullable = false)
    private int rating;
    @JoinTable(name = "clients_bookmarked_services", joinColumns = {
        @JoinColumn(name = "services_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "clients_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany
    private List<Clients> clientsList;
    @JoinColumn(name = "ads_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Ads adsId;
    @JoinColumn(name = "gpslocation_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Gpslocation gpslocationId;
    @JoinColumn(name = "openingHours_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private OpeningHours openingHoursid;
    @JoinColumn(name = "entities_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Entities entitiesId;
    @JoinColumn(name = "categories_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Categories categoriesId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicesId")
    private List<Reviews> reviewsList;

    public Services() {
    }

    public Services(Integer id) {
        this.id = id;
    }

    public Services(Integer id, String label, String description, int rating) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @XmlTransient
    public List<Clients> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    public Ads getAdsId() {
        return adsId;
    }

    public void setAdsId(Ads adsId) {
        this.adsId = adsId;
    }

    public Gpslocation getGpslocationId() {
        return gpslocationId;
    }

    public void setGpslocationId(Gpslocation gpslocationId) {
        this.gpslocationId = gpslocationId;
    }

    public OpeningHours getOpeningHoursid() {
        return openingHoursid;
    }

    public void setOpeningHoursid(OpeningHours openingHoursid) {
        this.openingHoursid = openingHoursid;
    }

    public Entities getEntitiesId() {
        return entitiesId;
    }

    public void setEntitiesId(Entities entitiesId) {
        this.entitiesId = entitiesId;
    }

    public Categories getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(Categories categoriesId) {
        this.categoriesId = categoriesId;
    }

    @XmlTransient
    public List<Reviews> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Services other = (Services) obj;
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      return true;
    }

    @Override
    public String toString() {
        return "Services[ id=" + id + " ]";
    }

}
