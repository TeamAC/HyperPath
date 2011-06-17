package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Clients;
import java.util.ArrayList;
import java.util.List;

import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class ReviewsJpaController implements Serializable {
  private static final long serialVersionUID = 4270693784161684160L;

  private EntityManager        em  = null;
  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public ReviewsJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  public ReviewsJpaController(EntityManager mockedEM) {
    em = mockedEM;
  }

  public EntityManager getEntityManager() {
    if (em != null)
      return em;
    return emf.createEntityManager();
  }

  public void create(Reviews reviews) throws RollbackFailureException,
      Exception {
    if (reviews.getClientsList() == null) {
      reviews.setClientsList(new ArrayList<Clients>());
    }
    if (reviews.getServicesList() == null) {
      reviews.setServicesList(new ArrayList<Services>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      List<Clients> attachedClientsList = new ArrayList<Clients>();
      for (Clients clientsListClientsToAttach : reviews.getClientsList()) {
        clientsListClientsToAttach = em.getReference(
            clientsListClientsToAttach.getClass(),
            clientsListClientsToAttach.getClientsPK());
        attachedClientsList.add(clientsListClientsToAttach);
      }
      reviews.setClientsList(attachedClientsList);
      List<Services> attachedServicesList = new ArrayList<Services>();
      for (Services servicesListServicesToAttach : reviews
          .getServicesList()) {
        servicesListServicesToAttach = em.getReference(
            servicesListServicesToAttach.getClass(),
            servicesListServicesToAttach.getServicesPK());
        attachedServicesList.add(servicesListServicesToAttach);
      }
      reviews.setServicesList(attachedServicesList);
      em.persist(reviews);
      for (Clients clientsListClients : reviews.getClientsList()) {
        clientsListClients.getReviewsList().add(reviews);
        clientsListClients = em.merge(clientsListClients);
      }
      for (Services servicesListServices : reviews.getServicesList()) {
        servicesListServices.getReviewsList().add(reviews);
        servicesListServices = em.merge(servicesListServices);
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
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Reviews reviews) throws NonexistentEntityException,
      RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Reviews persistentReviews = em.find(Reviews.class, reviews.getId());
      List<Clients> clientsListOld = persistentReviews.getClientsList();
      List<Clients> clientsListNew = reviews.getClientsList();
      List<Services> servicesListOld = persistentReviews
          .getServicesList();
      List<Services> servicesListNew = reviews.getServicesList();
      List<Clients> attachedClientsListNew = new ArrayList<Clients>();
      for (Clients clientsListNewClientsToAttach : clientsListNew) {
        clientsListNewClientsToAttach = em.getReference(
            clientsListNewClientsToAttach.getClass(),
            clientsListNewClientsToAttach.getClientsPK());
        attachedClientsListNew.add(clientsListNewClientsToAttach);
      }
      clientsListNew = attachedClientsListNew;
      reviews.setClientsList(clientsListNew);
      List<Services> attachedServicesListNew = new ArrayList<Services>();
      for (Services servicesListNewServicesToAttach : servicesListNew) {
        servicesListNewServicesToAttach = em.getReference(
            servicesListNewServicesToAttach.getClass(),
            servicesListNewServicesToAttach.getServicesPK());
        attachedServicesListNew.add(servicesListNewServicesToAttach);
      }
      servicesListNew = attachedServicesListNew;
      reviews.setServicesList(servicesListNew);
      reviews = em.merge(reviews);
      for (Clients clientsListOldClients : clientsListOld) {
        if (!clientsListNew.contains(clientsListOldClients)) {
          clientsListOldClients.getReviewsList().remove(reviews);
          clientsListOldClients = em.merge(clientsListOldClients);
        }
      }
      for (Clients clientsListNewClients : clientsListNew) {
        if (!clientsListOld.contains(clientsListNewClients)) {
          clientsListNewClients.getReviewsList().add(reviews);
          clientsListNewClients = em.merge(clientsListNewClients);
        }
      }
      for (Services servicesListOldServices : servicesListOld) {
        if (!servicesListNew.contains(servicesListOldServices)) {
          servicesListOldServices.getReviewsList().remove(reviews);
          servicesListOldServices = em.merge(servicesListOldServices);
        }
      }
      for (Services servicesListNewServices : servicesListNew) {
        if (!servicesListOld.contains(servicesListNewServices)) {
          servicesListNewServices.getReviewsList().add(reviews);
          servicesListNewServices = em.merge(servicesListNewServices);
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
        Integer id = reviews.getId();
        if (findReviews(id) == null) {
          throw new NonexistentEntityException("The reviews with id "
              + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws NonexistentEntityException,
      RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Reviews reviews;
      try {
        reviews = em.getReference(Reviews.class, id);
        reviews.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The reviews with id "
            + id + " no longer exists.", enfe);
      }
      List<Clients> clientsList = reviews.getClientsList();
      for (Clients clientsListClients : clientsList) {
        clientsListClients.getReviewsList().remove(reviews);
        clientsListClients = em.merge(clientsListClients);
      }
      List<Services> servicesList = reviews.getServicesList();
      for (Services servicesListServices : servicesList) {
        servicesListServices.getReviewsList().remove(reviews);
        servicesListServices = em.merge(servicesListServices);
      }
      em.remove(reviews);
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

  public List<Reviews> findReviewsEntities() {
    return findReviewsEntities(true, -1, -1);
  }

  public List<Reviews> findReviewsEntities(int maxResults, int firstResult) {
    return findReviewsEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
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

  public Reviews findReviews(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Reviews.class, id);
    } finally {
      em.close();
    }
  }

  public int getReviewsCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
      criteriaQuery.select(criteriaBuilder.count(reviewsRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Reviews> findReviewsByService(Services service) {
//    EntityManager em = getEntityManager();
//    try {
//      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
//      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
//      Join<Reviews, Services> shrJoin      = reviewsRoot.join("reviews_id", JoinType.INNER);
//      Join<Reviews, Services> servicesJoin = reviewsRoot.join("services_id", JoinType.INNER);
//      criteriaQuery.multiselect(reviewsRoot, servicesJoin.get("name"));
//      Query query = em.createQuery(criteriaQuery);
//      return query.getResultList();
//    } finally {
//      em.close();
//    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Reviews> findReviewsByClient(Clients client) {
//    EntityManager em = getEntityManager();
//    try {
//      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
//      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
//      criteriaQuery.select(reviewsRoot).where( criteriaBuilder.equal(Reviews.get("address"),faxNumber));
//      Query query = em.createQuery(criteriaQuery);
//      return query.getResultList();
//    } finally {
//      em.close();
//    }
    return null;
  }

}
