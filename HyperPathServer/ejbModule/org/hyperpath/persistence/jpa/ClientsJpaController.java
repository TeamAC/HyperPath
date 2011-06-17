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
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Reviews;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class ClientsJpaController implements Serializable {
  private static final long serialVersionUID = 7580378731132730416L;

  public ClientsJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Clients clients) throws PreexistingEntityException,
      RollbackFailureException, Exception {
    if (clients.getReviewsList() == null) {
      clients.setReviewsList(new ArrayList<Reviews>());
    }
    if (clients.getServicesList() == null) {
      clients.setServicesList(new ArrayList<Services>());
    }

    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Entities entities = clients.getEntities();
      if (entities != null) {
        entities = em.getReference(entities.getClass(),
            entities.getId());
        clients.setEntities(entities);
      }
      List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
      for (Reviews reviewsListReviewsToAttach : clients.getReviewsList()) {
        reviewsListReviewsToAttach = em.getReference(
            reviewsListReviewsToAttach.getClass(),
            reviewsListReviewsToAttach.getId());
        attachedReviewsList.add(reviewsListReviewsToAttach);
      }
      clients.setReviewsList(attachedReviewsList);
      List<Services> attachedServicesList = new ArrayList<Services>();
      for (Services servicesListServicesToAttach : clients
          .getServicesList()) {
        servicesListServicesToAttach = em.getReference(
            servicesListServicesToAttach.getClass(),
            servicesListServicesToAttach.getServicesPK());
        attachedServicesList.add(servicesListServicesToAttach);
      }
      clients.setServicesList(attachedServicesList);
      em.persist(clients);
      if (entities != null) {
        entities.getClientsList().add(clients);
        entities = em.merge(entities);
      }
      for (Reviews reviewsListReviews : clients.getReviewsList()) {
        reviewsListReviews.getClientsList().add(clients);
        reviewsListReviews = em.merge(reviewsListReviews);
      }
      for (Services servicesListServices : clients.getServicesList()) {
        servicesListServices.getClientsList().add(clients);
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
      if (findClients(clients.getClientsPK()) != null) {
        throw new PreexistingEntityException("Clients " + clients
            + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Clients clients) throws NonexistentEntityException,
      RollbackFailureException, Exception {

    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Clients persistentClients = em.find(Clients.class,
          clients.getClientsPK());
      Entities entitiesOld = persistentClients.getEntities();
      Entities entitiesNew = clients.getEntities();
      List<Reviews> reviewsListOld = persistentClients.getReviewsList();
      List<Reviews> reviewsListNew = clients.getReviewsList();
      List<Services> servicesListOld = persistentClients
          .getServicesList();
      List<Services> servicesListNew = clients.getServicesList();
      if (entitiesNew != null) {
        entitiesNew = em.getReference(entitiesNew.getClass(),
            entitiesNew.getId());
        clients.setEntities(entitiesNew);
      }
      List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
      for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
        reviewsListNewReviewsToAttach = em.getReference(
            reviewsListNewReviewsToAttach.getClass(),
            reviewsListNewReviewsToAttach.getId());
        attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
      }
      reviewsListNew = attachedReviewsListNew;
      clients.setReviewsList(reviewsListNew);
      List<Services> attachedServicesListNew = new ArrayList<Services>();
      for (Services servicesListNewServicesToAttach : servicesListNew) {
        servicesListNewServicesToAttach = em.getReference(
            servicesListNewServicesToAttach.getClass(),
            servicesListNewServicesToAttach.getServicesPK());
        attachedServicesListNew.add(servicesListNewServicesToAttach);
      }
      servicesListNew = attachedServicesListNew;
      clients.setServicesList(servicesListNew);
      clients = em.merge(clients);
      if (entitiesOld != null && !entitiesOld.equals(entitiesNew)) {
        entitiesOld.getClientsList().remove(clients);
        entitiesOld = em.merge(entitiesOld);
      }
      if (entitiesNew != null && !entitiesNew.equals(entitiesOld)) {
        entitiesNew.getClientsList().add(clients);
        entitiesNew = em.merge(entitiesNew);
      }
      for (Reviews reviewsListOldReviews : reviewsListOld) {
        if (!reviewsListNew.contains(reviewsListOldReviews)) {
          reviewsListOldReviews.getClientsList().remove(clients);
          reviewsListOldReviews = em.merge(reviewsListOldReviews);
        }
      }
      for (Reviews reviewsListNewReviews : reviewsListNew) {
        if (!reviewsListOld.contains(reviewsListNewReviews)) {
          reviewsListNewReviews.getClientsList().add(clients);
          reviewsListNewReviews = em.merge(reviewsListNewReviews);
        }
      }
      for (Services servicesListOldServices : servicesListOld) {
        if (!servicesListNew.contains(servicesListOldServices)) {
          servicesListOldServices.getClientsList().remove(clients);
          servicesListOldServices = em.merge(servicesListOldServices);
        }
      }
      for (Services servicesListNewServices : servicesListNew) {
        if (!servicesListOld.contains(servicesListNewServices)) {
          servicesListNewServices.getClientsList().add(clients);
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
        Integer id = clients.getClientsPK();
        if (findClients(id) == null) {
          throw new NonexistentEntityException("The clients with id "
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
      Clients clients;
      try {
        clients = em.getReference(Clients.class, id);
        clients.getClientsPK();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The clients with id "
            + id + " no longer exists.", enfe);
      }
      Entities entities = clients.getEntities();
      if (entities != null) {
        entities.getClientsList().remove(clients);
        entities = em.merge(entities);
      }
      List<Reviews> reviewsList = clients.getReviewsList();
      for (Reviews reviewsListReviews : reviewsList) {
        reviewsListReviews.getClientsList().remove(clients);
        reviewsListReviews = em.merge(reviewsListReviews);
      }
      List<Services> servicesList = clients.getServicesList();
      for (Services servicesListServices : servicesList) {
        servicesListServices.getClientsList().remove(clients);
        servicesListServices = em.merge(servicesListServices);
      }
      em.remove(clients);
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

  public List<Clients> findClientsEntities() {
    return findClientsEntities(true, -1, -1);
  }

  public List<Clients> findClientsEntities(int maxResults, int firstResult) {
    return findClientsEntities(false, maxResults, firstResult);
  }

  private List<Clients> findClientsEntities(boolean all, int maxResults,
                                            int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Clients.class));
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

  public Clients findClients(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Clients.class, id);
    } finally {
      em.close();
    }
  }

  public int getClientsCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Clients> rt = cq.from(Clients.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public List<Clients> findClientsByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByPhone(String phone) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByFax(String fax) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByMail(String mail) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public List<Clients> findClientsByName(String clientName) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Clients> criteriaQuery = criteriaBuilder.createQuery(Clients.class);
      Root<Clients> clientRoot = criteriaQuery.from(Clients.class);
      criteriaQuery.select(clientRoot).where( criteriaBuilder.equal(clientRoot.<String>get("name"),clientName));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Clients> findClientsByLastName(String clientLastName) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Clients> criteriaQuery = criteriaBuilder.createQuery(Clients.class);
      Root<Clients> clientRoot = criteriaQuery.from(Clients.class);
      criteriaQuery.select(clientRoot).where( criteriaBuilder.equal(clientRoot.get("lastName"),clientLastName));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Clients> findClientsByLogin(String clientLogin) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Clients> criteriaQuery = criteriaBuilder.createQuery(Clients.class);
      Root<Clients> clientRoot = criteriaQuery.from(Clients.class);
      criteriaQuery.select(clientRoot).where( criteriaBuilder.equal(clientRoot.get("login"),clientLogin));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Clients> findClientsByBirthDate(String clientBirthDate) {
    EntityManager em = getEntityManager();
    try {
	    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	    CriteriaQuery<Clients> criteriaQuery = criteriaBuilder.createQuery(Clients.class);
	    Root<Clients> clientRoot = criteriaQuery.from(Clients.class);
	    criteriaQuery.select(clientRoot).where( criteriaBuilder.equal(clientRoot.get("birthdate"), clientBirthDate));
	    Query query = em.createQuery(criteriaQuery);
	    return query.getResultList();
    } finally {
      em.close();
    }
  }

  public List<Clients> findClientsByAge(int clientAge) {
    // TODO Auto-generated method stub
    return null;
  }

}
