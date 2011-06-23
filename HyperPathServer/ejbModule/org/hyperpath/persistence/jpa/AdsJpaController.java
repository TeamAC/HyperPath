/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author chedi
 */
public class AdsJpaController implements Serializable {

    public AdsJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
  }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
        return emf.createEntityManager();
  }

  public void create(Ads ads) throws RollbackFailureException, Exception {
        if (ads.getServicesList() == null) {
            ads.setServicesList(new ArrayList<Services>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Advertisers advertisersId = ads.getAdvertisersId();
            if (advertisersId != null) {
                advertisersId = em.getReference(advertisersId.getClass(), advertisersId.getId());
                ads.setAdvertisersId(advertisersId);
            }
            List<Services> attachedServicesList = new ArrayList<Services>();
            for (Services servicesListServicesToAttach : ads.getServicesList()) {
                servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
                attachedServicesList.add(servicesListServicesToAttach);
            }
            ads.setServicesList(attachedServicesList);
            em.persist(ads);
            if (advertisersId != null) {
                advertisersId.getAdsList().add(ads);
                advertisersId = em.merge(advertisersId);
            }
            for (Services servicesListServices : ads.getServicesList()) {
                Ads oldAdsIdOfServicesListServices = servicesListServices.getAdsId();
                servicesListServices.setAdsId(ads);
                servicesListServices = em.merge(servicesListServices);
                if (oldAdsIdOfServicesListServices != null) {
                    oldAdsIdOfServicesListServices.getServicesList().remove(servicesListServices);
                    oldAdsIdOfServicesListServices = em.merge(oldAdsIdOfServicesListServices);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

  public void edit(Ads ads) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ads persistentAds = em.find(Ads.class, ads.getId());
            Advertisers advertisersIdOld = persistentAds.getAdvertisersId();
            Advertisers advertisersIdNew = ads.getAdvertisersId();
            List<Services> servicesListOld = persistentAds.getServicesList();
            List<Services> servicesListNew = ads.getServicesList();
            List<String> illegalOrphanMessages = null;
            for (Services servicesListOldServices : servicesListOld) {
                if (!servicesListNew.contains(servicesListOldServices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Services " + servicesListOldServices + " since its adsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (advertisersIdNew != null) {
                advertisersIdNew = em.getReference(advertisersIdNew.getClass(), advertisersIdNew.getId());
                ads.setAdvertisersId(advertisersIdNew);
            }
            List<Services> attachedServicesListNew = new ArrayList<Services>();
            for (Services servicesListNewServicesToAttach : servicesListNew) {
                servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
                attachedServicesListNew.add(servicesListNewServicesToAttach);
            }
            servicesListNew = attachedServicesListNew;
            ads.setServicesList(servicesListNew);
            ads = em.merge(ads);
            if (advertisersIdOld != null && !advertisersIdOld.equals(advertisersIdNew)) {
                advertisersIdOld.getAdsList().remove(ads);
                advertisersIdOld = em.merge(advertisersIdOld);
            }
            if (advertisersIdNew != null && !advertisersIdNew.equals(advertisersIdOld)) {
                advertisersIdNew.getAdsList().add(ads);
                advertisersIdNew = em.merge(advertisersIdNew);
            }
            for (Services servicesListNewServices : servicesListNew) {
                if (!servicesListOld.contains(servicesListNewServices)) {
                    Ads oldAdsIdOfServicesListNewServices = servicesListNewServices.getAdsId();
                    servicesListNewServices.setAdsId(ads);
                    servicesListNewServices = em.merge(servicesListNewServices);
                    if (oldAdsIdOfServicesListNewServices != null && !oldAdsIdOfServicesListNewServices.equals(ads)) {
                        oldAdsIdOfServicesListNewServices.getServicesList().remove(servicesListNewServices);
                        oldAdsIdOfServicesListNewServices = em.merge(oldAdsIdOfServicesListNewServices);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ads.getId();
                if (findAds(id) == null) {
                    throw new NonexistentEntityException("The ads with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ads ads;
            try {
                ads = em.getReference(Ads.class, id);
                ads.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ads with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Services> servicesListOrphanCheck = ads.getServicesList();
            for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ads (" + ads + ") cannot be destroyed since the Services " + servicesListOrphanCheckServices + " in its servicesList field has a non-nullable adsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Advertisers advertisersId = ads.getAdvertisersId();
            if (advertisersId != null) {
                advertisersId.getAdsList().remove(ads);
                advertisersId = em.merge(advertisersId);
            }
            em.remove(ads);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ads> findAdsEntities() {
        return findAdsEntities(true, -1, -1);
    }

    public List<Ads> findAdsEntities(int maxResults, int firstResult) {
        return findAdsEntities(false, maxResults, firstResult);
    }

    private List<Ads> findAdsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ads.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ads findAds(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ads.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ads> rt = cq.from(Ads.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
