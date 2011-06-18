package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

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

 public void create(Clients clients) {
        if (clients.getServicesList() == null) {
            clients.setServicesList(new ArrayList<Services>());
        }
        if (clients.getReviewsList() == null) {
            clients.setReviewsList(new ArrayList<Reviews>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
            Entities entitiesId = clients.getEntitiesId();
            if (entitiesId != null) {
                entitiesId = em.getReference(entitiesId.getClass(), entitiesId.getId());
                clients.setEntitiesId(entitiesId);
            }
            List<Services> attachedServicesList = new ArrayList<Services>();
            for (Services servicesListServicesToAttach : clients.getServicesList()) {
                servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
                attachedServicesList.add(servicesListServicesToAttach);
            }
            clients.setServicesList(attachedServicesList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : clients.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getId());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            clients.setReviewsList(attachedReviewsList);
            em.persist(clients);
            if (entitiesId != null) {
                entitiesId.getClientsList().add(clients);
                entitiesId = em.merge(entitiesId);
            }
            for (Services servicesListServices : clients.getServicesList()) {
                servicesListServices.getClientsList().add(clients);
                servicesListServices = em.merge(servicesListServices);
            }
            for (Reviews reviewsListReviews : clients.getReviewsList()) {
                Clients oldClientsIdOfReviewsListReviews = reviewsListReviews.getClientsId();
                reviewsListReviews.setClientsId(clients);
                reviewsListReviews = em.merge(reviewsListReviews);
                if (oldClientsIdOfReviewsListReviews != null) {
                    oldClientsIdOfReviewsListReviews.getReviewsList().remove(reviewsListReviews);
                    oldClientsIdOfReviewsListReviews = em.merge(oldClientsIdOfReviewsListReviews);
                }
            }
            utx.commit();
        } catch (NotSupportedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SystemException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalStateException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (RollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicMixedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicRollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clients clients) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
            Clients persistentClients = em.find(Clients.class, clients.getId());
            Entities entitiesIdOld = persistentClients.getEntitiesId();
            Entities entitiesIdNew = clients.getEntitiesId();
            List<Services> servicesListOld = persistentClients.getServicesList();
            List<Services> servicesListNew = clients.getServicesList();
            List<Reviews> reviewsListOld = persistentClients.getReviewsList();
            List<Reviews> reviewsListNew = clients.getReviewsList();
            List<String> illegalOrphanMessages = null;
            for (Reviews reviewsListOldReviews : reviewsListOld) {
                if (!reviewsListNew.contains(reviewsListOldReviews)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its clientsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entitiesIdNew != null) {
                entitiesIdNew = em.getReference(entitiesIdNew.getClass(), entitiesIdNew.getId());
                clients.setEntitiesId(entitiesIdNew);
            }
            List<Services> attachedServicesListNew = new ArrayList<Services>();
            for (Services servicesListNewServicesToAttach : servicesListNew) {
                servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
                attachedServicesListNew.add(servicesListNewServicesToAttach);
            }
            servicesListNew = attachedServicesListNew;
            clients.setServicesList(servicesListNew);
            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getId());
                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
            }
            reviewsListNew = attachedReviewsListNew;
            clients.setReviewsList(reviewsListNew);
            clients = em.merge(clients);
            if (entitiesIdOld != null && !entitiesIdOld.equals(entitiesIdNew)) {
                entitiesIdOld.getClientsList().remove(clients);
                entitiesIdOld = em.merge(entitiesIdOld);
            }
            if (entitiesIdNew != null && !entitiesIdNew.equals(entitiesIdOld)) {
                entitiesIdNew.getClientsList().add(clients);
                entitiesIdNew = em.merge(entitiesIdNew);
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
            for (Reviews reviewsListNewReviews : reviewsListNew) {
                if (!reviewsListOld.contains(reviewsListNewReviews)) {
                    Clients oldClientsIdOfReviewsListNewReviews = reviewsListNewReviews.getClientsId();
                    reviewsListNewReviews.setClientsId(clients);
                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
                    if (oldClientsIdOfReviewsListNewReviews != null && !oldClientsIdOfReviewsListNewReviews.equals(clients)) {
                        oldClientsIdOfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
                        oldClientsIdOfReviewsListNewReviews = em.merge(oldClientsIdOfReviewsListNewReviews);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clients.getId();
                if (findClients(id) == null) {
                    throw new NonexistentEntityException("The clients with id " + id + " no longer exists.");
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
            utx.begin();
            Clients clients;
            try {
                clients = em.getReference(Clients.class, id);
                clients.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clients with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reviews> reviewsListOrphanCheck = clients.getReviewsList();
            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clients (" + clients + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable clientsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entities entitiesId = clients.getEntitiesId();
            if (entitiesId != null) {
                entitiesId.getClientsList().remove(clients);
                entitiesId = em.merge(entitiesId);
            }
            List<Services> servicesList = clients.getServicesList();
            for (Services servicesListServices : servicesList) {
                servicesListServices.getClientsList().remove(clients);
                servicesListServices = em.merge(servicesListServices);
            }
            em.remove(clients);
            utx.commit();
        } catch (NotSupportedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SystemException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalStateException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (RollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicMixedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicRollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
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

  @SuppressWarnings("unchecked")
  private List<Clients> findClientsEntities(boolean all, int maxResults,
                                            int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Clients> criteriaQuery = criteriaBuilder.createQuery(Clients.class);
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
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Clients> clientsRoot = criteriaQuery.from(Clients.class);
      criteriaQuery.select(criteriaBuilder.count(clientsRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
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

  public List<Clients> findClientsByName(String clientName) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByLastName(String clientLastName) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByLogin(String clientLogin) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByBirthDate(String clientBirthDate) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByAge(int clientAge) {
    // TODO Auto-generated method stub
    return null;
  }

}
