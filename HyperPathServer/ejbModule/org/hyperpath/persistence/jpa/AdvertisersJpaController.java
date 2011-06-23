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
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Ads;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author chedi
 */
public class AdvertisersJpaController implements Serializable {

    public AdvertisersJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Advertisers advertisers) throws RollbackFailureException, Exception {
        if (advertisers.getAdsList() == null) {
            advertisers.setAdsList(new ArrayList<Ads>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Entities entitiesId = advertisers.getEntitiesId();
            if (entitiesId != null) {
                entitiesId = em.getReference(entitiesId.getClass(), entitiesId.getId());
                advertisers.setEntitiesId(entitiesId);
            }
            List<Ads> attachedAdsList = new ArrayList<Ads>();
            for (Ads adsListAdsToAttach : advertisers.getAdsList()) {
                adsListAdsToAttach = em.getReference(adsListAdsToAttach.getClass(), adsListAdsToAttach.getId());
                attachedAdsList.add(adsListAdsToAttach);
            }
            advertisers.setAdsList(attachedAdsList);
            em.persist(advertisers);
            if (entitiesId != null) {
                entitiesId.getAdvertisersList().add(advertisers);
                entitiesId = em.merge(entitiesId);
            }
            for (Ads adsListAds : advertisers.getAdsList()) {
                Advertisers oldAdvertisersIdOfAdsListAds = adsListAds.getAdvertisersId();
                adsListAds.setAdvertisersId(advertisers);
                adsListAds = em.merge(adsListAds);
                if (oldAdvertisersIdOfAdsListAds != null) {
                    oldAdvertisersIdOfAdsListAds.getAdsList().remove(adsListAds);
                    oldAdvertisersIdOfAdsListAds = em.merge(oldAdvertisersIdOfAdsListAds);
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

    public void edit(Advertisers advertisers) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Advertisers persistentAdvertisers = em.find(Advertisers.class, advertisers.getId());
            Entities entitiesIdOld = persistentAdvertisers.getEntitiesId();
            Entities entitiesIdNew = advertisers.getEntitiesId();
            List<Ads> adsListOld = persistentAdvertisers.getAdsList();
            List<Ads> adsListNew = advertisers.getAdsList();
            List<String> illegalOrphanMessages = null;
            for (Ads adsListOldAds : adsListOld) {
                if (!adsListNew.contains(adsListOldAds)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ads " + adsListOldAds + " since its advertisersId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entitiesIdNew != null) {
                entitiesIdNew = em.getReference(entitiesIdNew.getClass(), entitiesIdNew.getId());
                advertisers.setEntitiesId(entitiesIdNew);
            }
            List<Ads> attachedAdsListNew = new ArrayList<Ads>();
            for (Ads adsListNewAdsToAttach : adsListNew) {
                adsListNewAdsToAttach = em.getReference(adsListNewAdsToAttach.getClass(), adsListNewAdsToAttach.getId());
                attachedAdsListNew.add(adsListNewAdsToAttach);
            }
            adsListNew = attachedAdsListNew;
            advertisers.setAdsList(adsListNew);
            advertisers = em.merge(advertisers);
            if (entitiesIdOld != null && !entitiesIdOld.equals(entitiesIdNew)) {
                entitiesIdOld.getAdvertisersList().remove(advertisers);
                entitiesIdOld = em.merge(entitiesIdOld);
            }
            if (entitiesIdNew != null && !entitiesIdNew.equals(entitiesIdOld)) {
                entitiesIdNew.getAdvertisersList().add(advertisers);
                entitiesIdNew = em.merge(entitiesIdNew);
            }
            for (Ads adsListNewAds : adsListNew) {
                if (!adsListOld.contains(adsListNewAds)) {
                    Advertisers oldAdvertisersIdOfAdsListNewAds = adsListNewAds.getAdvertisersId();
                    adsListNewAds.setAdvertisersId(advertisers);
                    adsListNewAds = em.merge(adsListNewAds);
                    if (oldAdvertisersIdOfAdsListNewAds != null && !oldAdvertisersIdOfAdsListNewAds.equals(advertisers)) {
                        oldAdvertisersIdOfAdsListNewAds.getAdsList().remove(adsListNewAds);
                        oldAdvertisersIdOfAdsListNewAds = em.merge(oldAdvertisersIdOfAdsListNewAds);
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
                Integer id = advertisers.getId();
                if (findAdvertisers(id) == null) {
                    throw new NonexistentEntityException("The advertisers with id " + id + " no longer exists.");
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
            Advertisers advertisers;
            try {
                advertisers = em.getReference(Advertisers.class, id);
                advertisers.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The advertisers with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ads> adsListOrphanCheck = advertisers.getAdsList();
            for (Ads adsListOrphanCheckAds : adsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Advertisers (" + advertisers + ") cannot be destroyed since the Ads " + adsListOrphanCheckAds + " in its adsList field has a non-nullable advertisersId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entities entitiesId = advertisers.getEntitiesId();
            if (entitiesId != null) {
                entitiesId.getAdvertisersList().remove(advertisers);
                entitiesId = em.merge(entitiesId);
            }
            em.remove(advertisers);
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

    public List<Advertisers> findAdvertisersEntities() {
        return findAdvertisersEntities(true, -1, -1);
    }

    public List<Advertisers> findAdvertisersEntities(int maxResults, int firstResult) {
        return findAdvertisersEntities(false, maxResults, firstResult);
    }

    private List<Advertisers> findAdvertisersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Advertisers.class));
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

    public Advertisers findAdvertisers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Advertisers.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdvertisersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Advertisers> rt = cq.from(Advertisers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
