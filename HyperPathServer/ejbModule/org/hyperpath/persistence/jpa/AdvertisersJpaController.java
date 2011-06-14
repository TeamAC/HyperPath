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
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class AdvertisersJpaController implements Serializable {

  public AdvertisersJpaController(UserTransaction utx,
      EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Advertisers advertisers)
      throws PreexistingEntityException, RollbackFailureException,
      Exception {
    if (advertisers.getAdsList() == null) {
      advertisers.setAdsList(new ArrayList<Ads>());
    }

    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Entities entities = advertisers.getEntities();
      if (entities != null) {
        entities = em.getReference(entities.getClass(),
            entities.getId());
        advertisers.setEntities(entities);
      }
      List<Ads> attachedAdsList = new ArrayList<Ads>();
      for (Ads adsListAdsToAttach : advertisers.getAdsList()) {
        adsListAdsToAttach = em.getReference(
            adsListAdsToAttach.getClass(),
            adsListAdsToAttach.getAdsPK());
        attachedAdsList.add(adsListAdsToAttach);
      }
      advertisers.setAdsList(attachedAdsList);
      em.persist(advertisers);
      if (entities != null) {
        entities.getAdvertisersList().add(advertisers);
        entities = em.merge(entities);
      }
      for (Ads adsListAds : advertisers.getAdsList()) {
        Advertisers oldAdvertisersOfAdsListAds = adsListAds
            .getAdvertisers();
        adsListAds.setAdvertisers(advertisers);
        adsListAds = em.merge(adsListAds);
        if (oldAdvertisersOfAdsListAds != null) {
          oldAdvertisersOfAdsListAds.getAdsList().remove(adsListAds);
          oldAdvertisersOfAdsListAds = em
              .merge(oldAdvertisersOfAdsListAds);
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      if (findAdvertisers(advertisers.getAdvertisersPK()) != null) {
        throw new PreexistingEntityException("Advertisers "
            + advertisers + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Advertisers advertisers) throws IllegalOrphanException,
      NonexistentEntityException, RollbackFailureException, Exception {

    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Advertisers persistentAdvertisers = em.find(Advertisers.class,
          advertisers.getAdvertisersPK());
      Entities entitiesOld = persistentAdvertisers.getEntities();
      Entities entitiesNew = advertisers.getEntities();
      List<Ads> adsListOld = persistentAdvertisers.getAdsList();
      List<Ads> adsListNew = advertisers.getAdsList();
      List<String> illegalOrphanMessages = null;
      for (Ads adsListOldAds : adsListOld) {
        if (!adsListNew.contains(adsListOldAds)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<String>();
          }
          illegalOrphanMessages.add("You must retain Ads "
              + adsListOldAds
              + " since its advertisers field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      if (entitiesNew != null) {
        entitiesNew = em.getReference(entitiesNew.getClass(),
            entitiesNew.getId());
        advertisers.setEntities(entitiesNew);
      }
      List<Ads> attachedAdsListNew = new ArrayList<Ads>();
      for (Ads adsListNewAdsToAttach : adsListNew) {
        adsListNewAdsToAttach = em.getReference(
            adsListNewAdsToAttach.getClass(),
            adsListNewAdsToAttach.getAdsPK());
        attachedAdsListNew.add(adsListNewAdsToAttach);
      }
      adsListNew = attachedAdsListNew;
      advertisers.setAdsList(adsListNew);
      advertisers = em.merge(advertisers);
      if (entitiesOld != null && !entitiesOld.equals(entitiesNew)) {
        entitiesOld.getAdvertisersList().remove(advertisers);
        entitiesOld = em.merge(entitiesOld);
      }
      if (entitiesNew != null && !entitiesNew.equals(entitiesOld)) {
        entitiesNew.getAdvertisersList().add(advertisers);
        entitiesNew = em.merge(entitiesNew);
      }
      for (Ads adsListNewAds : adsListNew) {
        if (!adsListOld.contains(adsListNewAds)) {
          Advertisers oldAdvertisersOfAdsListNewAds = adsListNewAds
              .getAdvertisers();
          adsListNewAds.setAdvertisers(advertisers);
          adsListNewAds = em.merge(adsListNewAds);
          if (oldAdvertisersOfAdsListNewAds != null
              && !oldAdvertisersOfAdsListNewAds
                  .equals(advertisers)) {
            oldAdvertisersOfAdsListNewAds.getAdsList().remove(
                adsListNewAds);
            oldAdvertisersOfAdsListNewAds = em
                .merge(oldAdvertisersOfAdsListNewAds);
          }
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = advertisers.getAdvertisersPK();
        if (findAdvertisers(id) == null) {
          throw new NonexistentEntityException(
              "The advertisers with id " + id
                  + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws IllegalOrphanException,
      NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Advertisers advertisers;
      try {
        advertisers = em.getReference(Advertisers.class, id);
        advertisers.getAdvertisersPK();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The advertisers with id "
            + id + " no longer exists.", enfe);
      }
      List<String> illegalOrphanMessages = null;
      List<Ads> adsListOrphanCheck = advertisers.getAdsList();
      for (Ads adsListOrphanCheckAds : adsListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages
            .add("This Advertisers ("
                + advertisers
                + ") cannot be destroyed since the Ads "
                + adsListOrphanCheckAds
                + " in its adsList field has a non-nullable advertisers field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      Entities entities = advertisers.getEntities();
      if (entities != null) {
        entities.getAdvertisersList().remove(advertisers);
        entities = em.merge(entities);
      }
      em.remove(advertisers);
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
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

  public List<Advertisers> findAdvertisersEntities(int maxResults,
                                                   int firstResult) {
    return findAdvertisersEntities(false, maxResults, firstResult);
  }

  private List<Advertisers> findAdvertisersEntities(boolean all,
                                                    int maxResults,
                                                    int firstResult) {
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
