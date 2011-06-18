package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

public class ServicesJpaController implements Serializable {
  private static final long serialVersionUID = 5950588872562050584L;

  public ServicesJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  public ServicesJpaController(EntityManager mockedEM){
    em = mockedEM;
  }

  private EntityManager        em  = null;
  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    if(em != null)
      return em;
    return emf.createEntityManager();
  }

    public void create(Services services) {
        if (services.getClientsList() == null) {
            services.setClientsList(new ArrayList<Clients>());
        }
        if (services.getReviewsList() == null) {
            services.setReviewsList(new ArrayList<Reviews>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ads adsId = services.getAdsId();
            if (adsId != null) {
                adsId = em.getReference(adsId.getClass(), adsId.getId());
                services.setAdsId(adsId);
            }
            Gpslocation gpslocationId = services.getGpslocationId();
            if (gpslocationId != null) {
                gpslocationId = em.getReference(gpslocationId.getClass(), gpslocationId.getId());
                services.setGpslocationId(gpslocationId);
            }
            OpeningHours openingHoursid = services.getOpeningHoursid();
            if (openingHoursid != null) {
                openingHoursid = em.getReference(openingHoursid.getClass(), openingHoursid.getId());
                services.setOpeningHoursid(openingHoursid);
            }
            Entities entitiesId = services.getEntitiesId();
            if (entitiesId != null) {
                entitiesId = em.getReference(entitiesId.getClass(), entitiesId.getId());
                services.setEntitiesId(entitiesId);
            }
            Categories categoriesId = services.getCategoriesId();
            if (categoriesId != null) {
                categoriesId = em.getReference(categoriesId.getClass(), categoriesId.getId());
                services.setCategoriesId(categoriesId);
            }
            List<Clients> attachedClientsList = new ArrayList<Clients>();
            for (Clients clientsListClientsToAttach : services.getClientsList()) {
                clientsListClientsToAttach = em.getReference(clientsListClientsToAttach.getClass(), clientsListClientsToAttach.getId());
                attachedClientsList.add(clientsListClientsToAttach);
            }
            services.setClientsList(attachedClientsList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : services.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getId());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            services.setReviewsList(attachedReviewsList);
            em.persist(services);
            if (adsId != null) {
                adsId.getServicesList().add(services);
                adsId = em.merge(adsId);
            }
            if (gpslocationId != null) {
                gpslocationId.getServicesList().add(services);
                gpslocationId = em.merge(gpslocationId);
            }
            if (openingHoursid != null) {
                openingHoursid.getServicesList().add(services);
                openingHoursid = em.merge(openingHoursid);
            }
            if (entitiesId != null) {
                entitiesId.getServicesList().add(services);
                entitiesId = em.merge(entitiesId);
            }
            if (categoriesId != null) {
                categoriesId.getServicesList().add(services);
                categoriesId = em.merge(categoriesId);
            }
            for (Clients clientsListClients : services.getClientsList()) {
                clientsListClients.getServicesList().add(services);
                clientsListClients = em.merge(clientsListClients);
            }
            for (Reviews reviewsListReviews : services.getReviewsList()) {
                Services oldServicesIdOfReviewsListReviews = reviewsListReviews.getServicesId();
                reviewsListReviews.setServicesId(services);
                reviewsListReviews = em.merge(reviewsListReviews);
                if (oldServicesIdOfReviewsListReviews != null) {
                    oldServicesIdOfReviewsListReviews.getReviewsList().remove(reviewsListReviews);
                    oldServicesIdOfReviewsListReviews = em.merge(oldServicesIdOfReviewsListReviews);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Services services) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Services persistentServices = em.find(Services.class, services.getId());
            Ads adsIdOld = persistentServices.getAdsId();
            Ads adsIdNew = services.getAdsId();
            Gpslocation gpslocationIdOld = persistentServices.getGpslocationId();
            Gpslocation gpslocationIdNew = services.getGpslocationId();
            OpeningHours openingHoursidOld = persistentServices.getOpeningHoursid();
            OpeningHours openingHoursidNew = services.getOpeningHoursid();
            Entities entitiesIdOld = persistentServices.getEntitiesId();
            Entities entitiesIdNew = services.getEntitiesId();
            Categories categoriesIdOld = persistentServices.getCategoriesId();
            Categories categoriesIdNew = services.getCategoriesId();
            List<Clients> clientsListOld = persistentServices.getClientsList();
            List<Clients> clientsListNew = services.getClientsList();
            List<Reviews> reviewsListOld = persistentServices.getReviewsList();
            List<Reviews> reviewsListNew = services.getReviewsList();
            List<String> illegalOrphanMessages = null;
            for (Reviews reviewsListOldReviews : reviewsListOld) {
                if (!reviewsListNew.contains(reviewsListOldReviews)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its servicesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (adsIdNew != null) {
                adsIdNew = em.getReference(adsIdNew.getClass(), adsIdNew.getId());
                services.setAdsId(adsIdNew);
            }
            if (gpslocationIdNew != null) {
                gpslocationIdNew = em.getReference(gpslocationIdNew.getClass(), gpslocationIdNew.getId());
                services.setGpslocationId(gpslocationIdNew);
            }
            if (openingHoursidNew != null) {
                openingHoursidNew = em.getReference(openingHoursidNew.getClass(), openingHoursidNew.getId());
                services.setOpeningHoursid(openingHoursidNew);
            }
            if (entitiesIdNew != null) {
                entitiesIdNew = em.getReference(entitiesIdNew.getClass(), entitiesIdNew.getId());
                services.setEntitiesId(entitiesIdNew);
            }
            if (categoriesIdNew != null) {
                categoriesIdNew = em.getReference(categoriesIdNew.getClass(), categoriesIdNew.getId());
                services.setCategoriesId(categoriesIdNew);
            }
            List<Clients> attachedClientsListNew = new ArrayList<Clients>();
            for (Clients clientsListNewClientsToAttach : clientsListNew) {
                clientsListNewClientsToAttach = em.getReference(clientsListNewClientsToAttach.getClass(), clientsListNewClientsToAttach.getId());
                attachedClientsListNew.add(clientsListNewClientsToAttach);
            }
            clientsListNew = attachedClientsListNew;
            services.setClientsList(clientsListNew);
            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getId());
                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
            }
            reviewsListNew = attachedReviewsListNew;
            services.setReviewsList(reviewsListNew);
            services = em.merge(services);
            if (adsIdOld != null && !adsIdOld.equals(adsIdNew)) {
                adsIdOld.getServicesList().remove(services);
                adsIdOld = em.merge(adsIdOld);
            }
            if (adsIdNew != null && !adsIdNew.equals(adsIdOld)) {
                adsIdNew.getServicesList().add(services);
                adsIdNew = em.merge(adsIdNew);
            }
            if (gpslocationIdOld != null && !gpslocationIdOld.equals(gpslocationIdNew)) {
                gpslocationIdOld.getServicesList().remove(services);
                gpslocationIdOld = em.merge(gpslocationIdOld);
            }
            if (gpslocationIdNew != null && !gpslocationIdNew.equals(gpslocationIdOld)) {
                gpslocationIdNew.getServicesList().add(services);
                gpslocationIdNew = em.merge(gpslocationIdNew);
            }
            if (openingHoursidOld != null && !openingHoursidOld.equals(openingHoursidNew)) {
                openingHoursidOld.getServicesList().remove(services);
                openingHoursidOld = em.merge(openingHoursidOld);
            }
            if (openingHoursidNew != null && !openingHoursidNew.equals(openingHoursidOld)) {
                openingHoursidNew.getServicesList().add(services);
                openingHoursidNew = em.merge(openingHoursidNew);
            }
            if (entitiesIdOld != null && !entitiesIdOld.equals(entitiesIdNew)) {
                entitiesIdOld.getServicesList().remove(services);
                entitiesIdOld = em.merge(entitiesIdOld);
            }
            if (entitiesIdNew != null && !entitiesIdNew.equals(entitiesIdOld)) {
                entitiesIdNew.getServicesList().add(services);
                entitiesIdNew = em.merge(entitiesIdNew);
            }
            if (categoriesIdOld != null && !categoriesIdOld.equals(categoriesIdNew)) {
                categoriesIdOld.getServicesList().remove(services);
                categoriesIdOld = em.merge(categoriesIdOld);
            }
            if (categoriesIdNew != null && !categoriesIdNew.equals(categoriesIdOld)) {
                categoriesIdNew.getServicesList().add(services);
                categoriesIdNew = em.merge(categoriesIdNew);
            }
            for (Clients clientsListOldClients : clientsListOld) {
                if (!clientsListNew.contains(clientsListOldClients)) {
                    clientsListOldClients.getServicesList().remove(services);
                    clientsListOldClients = em.merge(clientsListOldClients);
                }
            }
            for (Clients clientsListNewClients : clientsListNew) {
                if (!clientsListOld.contains(clientsListNewClients)) {
                    clientsListNewClients.getServicesList().add(services);
                    clientsListNewClients = em.merge(clientsListNewClients);
                }
            }
            for (Reviews reviewsListNewReviews : reviewsListNew) {
                if (!reviewsListOld.contains(reviewsListNewReviews)) {
                    Services oldServicesIdOfReviewsListNewReviews = reviewsListNewReviews.getServicesId();
                    reviewsListNewReviews.setServicesId(services);
                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
                    if (oldServicesIdOfReviewsListNewReviews != null && !oldServicesIdOfReviewsListNewReviews.equals(services)) {
                        oldServicesIdOfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
                        oldServicesIdOfReviewsListNewReviews = em.merge(oldServicesIdOfReviewsListNewReviews);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = services.getId();
                if (findServices(id) == null) {
                    throw new NonexistentEntityException("The services with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Services services;
            try {
                services = em.getReference(Services.class, id);
                services.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The services with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reviews> reviewsListOrphanCheck = services.getReviewsList();
            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Services (" + services + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable servicesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ads adsId = services.getAdsId();
            if (adsId != null) {
                adsId.getServicesList().remove(services);
                adsId = em.merge(adsId);
            }
            Gpslocation gpslocationId = services.getGpslocationId();
            if (gpslocationId != null) {
                gpslocationId.getServicesList().remove(services);
                gpslocationId = em.merge(gpslocationId);
            }
            OpeningHours openingHoursid = services.getOpeningHoursid();
            if (openingHoursid != null) {
                openingHoursid.getServicesList().remove(services);
                openingHoursid = em.merge(openingHoursid);
            }
            Entities entitiesId = services.getEntitiesId();
            if (entitiesId != null) {
                entitiesId.getServicesList().remove(services);
                entitiesId = em.merge(entitiesId);
            }
            Categories categoriesId = services.getCategoriesId();
            if (categoriesId != null) {
                categoriesId.getServicesList().remove(services);
                categoriesId = em.merge(categoriesId);
            }
            List<Clients> clientsList = services.getClientsList();
            for (Clients clientsListClients : clientsList) {
                clientsListClients.getServicesList().remove(services);
                clientsListClients = em.merge(clientsListClients);
            }
            em.remove(services);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


  public List<Services> findServicesEntities() {
    return findServicesEntities(true, -1, -1);
  }

  public List<Services> findServicesEntities(int maxResults, int firstResult) {
    return findServicesEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Services> findServicesEntities(boolean all, int maxResults,
                                              int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Services> criteriaQuery = criteriaBuilder.createQuery(Services.class);
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

  public Services findServices(Integer id) {
    EntityManager em = getEntityManager();
    try {
        return em.find(Services.class, id);
    } finally {
        em.close();
    }
  }

  public int getServicesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Services> servicesRoot = criteriaQuery.from(Services.class);
      criteriaQuery.select(criteriaBuilder.count(servicesRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public List<Services> findServicesByLabel(String serviceLabel) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByCategory(Categories category) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByGpsLocation(Gpslocation gpsLocation,
                                                  int range) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServiceByRating(int rating, Categories category) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByUser(Clients client) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesPhone(String phone) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByFax(String fax) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByOpeningTime(Categories category,
                                                  Date startTime) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByClosingTime(Categories category,
                                                  Date endTime) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByTimeRange(Categories category,
                                                  Date startTime,
                                                  Date endTime) {
    // TODO Auto-generated method stub
    return null;
  }

}
