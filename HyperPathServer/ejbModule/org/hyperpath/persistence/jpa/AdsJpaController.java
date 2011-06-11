package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import java.util.List;
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
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class AdsJpaController implements Serializable {

    public AdsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ads ads) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Advertisers advertisers = ads.getAdvertisers();
            if (advertisers != null) {
                advertisers = em.getReference(advertisers.getClass(), advertisers.getAdvertisersPK());
                ads.setAdvertisers(advertisers);
            }
            Services services = ads.getServices();
            if (services != null) {
                services = em.getReference(services.getClass(), services.getServicesPK());
                ads.setServices(services);
            }
            em.persist(ads);
            if (advertisers != null) {
                advertisers.getAdsList().add(ads);
                advertisers = em.merge(advertisers);
            }
            if (services != null) {
                services.getAdsList().add(ads);
                services = em.merge(services);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAds(ads.getAdsPK()) != null) {
                throw new PreexistingEntityException("Ads " + ads + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ads ads) throws NonexistentEntityException, RollbackFailureException, Exception {

        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ads persistentAds = em.find(Ads.class, ads.getAdsPK());
            Advertisers advertisersOld = persistentAds.getAdvertisers();
            Advertisers advertisersNew = ads.getAdvertisers();
            Services servicesOld = persistentAds.getServices();
            Services servicesNew = ads.getServices();
            if (advertisersNew != null) {
                advertisersNew = em.getReference(advertisersNew.getClass(), advertisersNew.getAdvertisersPK());
                ads.setAdvertisers(advertisersNew);
            }
            if (servicesNew != null) {
                servicesNew = em.getReference(servicesNew.getClass(), servicesNew.getServicesPK());
                ads.setServices(servicesNew);
            }
            ads = em.merge(ads);
            if (advertisersOld != null && !advertisersOld.equals(advertisersNew)) {
                advertisersOld.getAdsList().remove(ads);
                advertisersOld = em.merge(advertisersOld);
            }
            if (advertisersNew != null && !advertisersNew.equals(advertisersOld)) {
                advertisersNew.getAdsList().add(ads);
                advertisersNew = em.merge(advertisersNew);
            }
            if (servicesOld != null && !servicesOld.equals(servicesNew)) {
                servicesOld.getAdsList().remove(ads);
                servicesOld = em.merge(servicesOld);
            }
            if (servicesNew != null && !servicesNew.equals(servicesOld)) {
                servicesNew.getAdsList().add(ads);
                servicesNew = em.merge(servicesNew);
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
                Integer id = ads.getAdsPK();
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ads ads;
            try {
                ads = em.getReference(Ads.class, id);
                ads.getAdsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ads with id " + id + " no longer exists.", enfe);
            }
            Advertisers advertisers = ads.getAdvertisers();
            if (advertisers != null) {
                advertisers.getAdsList().remove(ads);
                advertisers = em.merge(advertisers);
            }
            Services services = ads.getServices();
            if (services != null) {
                services.getAdsList().remove(ads);
                services = em.merge(services);
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
