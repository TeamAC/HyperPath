package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Services;

import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

public class AdvertisersJpaController implements Serializable {
  private static final long serialVersionUID = 5591474412841830570L;

  public AdvertisersJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Advertisers advertisers) throws Exception {
        if (advertisers.getAdsList() == null) {
            advertisers.setAdsList(new ArrayList<Ads>());
        }
        EntityManager em = null;
        try {
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
        } catch (Exception ex) {
          throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
  }

  public void edit(Advertisers advertisers) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
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
        } catch (Exception ex) {
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

  public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, Exception {
      EntityManager em = null;
      try {
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
      } catch (Exception ex) {
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

  @SuppressWarnings("unchecked")
  private List<Advertisers> findAdvertisersEntities(boolean all,
                                                    int maxResults,
                                                    int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Advertisers> criteriaQuery = criteriaBuilder.createQuery(Advertisers.class);
      Query query = em.createQuery(criteriaQuery);
      if (!all) {
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);
      }
      return query.getResultList();
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
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Advertisers> advrisersRoot = criteriaQuery.from(Advertisers.class);
      criteriaQuery.select(criteriaBuilder.count(advrisersRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public List<Services> findAdvertisersByPhone(String phone) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findAdvertisersByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findAdvertisersByFax(String fax) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findAdvertisersByMail(String mail) {
    // TODO Auto-generated method stub
    return null;
  }

  public Advertisers findAdvertiserByAd(Advertisers ad) {
    // TODO Auto-generated method stub
    return null;
  }

}
