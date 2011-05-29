package org.hyperpath.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "openingHours", catalog = "hyperPath", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpeningHours.findAll"        , query = "SELECT o FROM OpeningHours o"),
    @NamedQuery(name = "OpeningHours.findById"       , query = "SELECT o FROM OpeningHours o WHERE o.id = :id"),
    @NamedQuery(name = "OpeningHours.findByOpenTime" , query = "SELECT o FROM OpeningHours o WHERE o.openTime = :openTime"),
    @NamedQuery(name = "OpeningHours.findByCloseTime", query = "SELECT o FROM OpeningHours o WHERE o.closeTime = :closeTime"),
    @NamedQuery(name = "OpeningHours.findByDays"     , query = "SELECT o FROM OpeningHours o WHERE o.days = :days")})
public class OpeningHours implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "openTime", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date openTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "closeTime", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date closeTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "days", nullable = false)
    private int days;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "openingHours")
    private List<Services> servicesList;

    public OpeningHours() {
    }

    public OpeningHours(Integer id) {
        this.id = id;
    }

    public OpeningHours(Integer id, Date openTime, Date closeTime, int days) {
        this.id = id;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.days = days;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpeningHours)) {
            return false;
        }
        OpeningHours other = (OpeningHours) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.hyperpath.persistence.entities.OpeningHours[ id=" + id + " ]";
    }
    
}
