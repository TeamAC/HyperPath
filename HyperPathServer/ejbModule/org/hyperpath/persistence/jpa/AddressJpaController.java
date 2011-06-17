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
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Services;

import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class AddressJpaController implements Serializable {
  private static final long serialVersionUID = -7707097477154823435L;

  public AddressJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Address address) throws RollbackFailureException,
      Exception {
    if (address.getEntitiesList() == null) {
      address.setEntitiesList(new ArrayList<Entities>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      List<Entities> attachedEntitiesList = new ArrayList<Entities>();
      for (Entities entitiesListEntitiesToAttach : address
          .getEntitiesList()) {
        entitiesListEntitiesToAttach = em.getReference(
            entitiesListEntitiesToAttach.getClass(),
            entitiesListEntitiesToAttach.getId());
        attachedEntitiesList.add(entitiesListEntitiesToAttach);
      }
      address.setEntitiesList(attachedEntitiesList);
      em.persist(address);
      for (Entities entitiesListEntities : address.getEntitiesList()) {
        entitiesListEntities.getAddressList().add(address);
        entitiesListEntities = em.merge(entitiesListEntities);
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

  public void edit(Address address) throws NonexistentEntityException,
      RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Address persistentAddress = em.find(Address.class, address.getId());
      List<Entities> entitiesListOld = persistentAddress
          .getEntitiesList();
      List<Entities> entitiesListNew = address.getEntitiesList();
      List<Entities> attachedEntitiesListNew = new ArrayList<Entities>();
      for (Entities entitiesListNewEntitiesToAttach : entitiesListNew) {
        entitiesListNewEntitiesToAttach = em.getReference(
            entitiesListNewEntitiesToAttach.getClass(),
            entitiesListNewEntitiesToAttach.getId());
        attachedEntitiesListNew.add(entitiesListNewEntitiesToAttach);
      }
      entitiesListNew = attachedEntitiesListNew;
      address.setEntitiesList(entitiesListNew);
      address = em.merge(address);
      for (Entities entitiesListOldEntities : entitiesListOld) {
        if (!entitiesListNew.contains(entitiesListOldEntities)) {
          entitiesListOldEntities.getAddressList().remove(address);
          entitiesListOldEntities = em.merge(entitiesListOldEntities);
        }
      }
      for (Entities entitiesListNewEntities : entitiesListNew) {
        if (!entitiesListOld.contains(entitiesListNewEntities)) {
          entitiesListNewEntities.getAddressList().add(address);
          entitiesListNewEntities = em.merge(entitiesListNewEntities);
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
        Integer id = address.getId();
        if (findAddress(id) == null) {
          throw new NonexistentEntityException("The address with id "
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
      Address address;
      try {
        address = em.getReference(Address.class, id);
        address.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The address with id "
            + id + " no longer exists.", enfe);
      }
      List<Entities> entitiesList = address.getEntitiesList();
      for (Entities entitiesListEntities : entitiesList) {
        entitiesListEntities.getAddressList().remove(address);
        entitiesListEntities = em.merge(entitiesListEntities);
      }
      em.remove(address);
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

  public List<Address> findAddressEntities() {
    return findAddressEntities(true, -1, -1);
  }

  public List<Address> findAddressEntities(int maxResults, int firstResult) {
    return findAddressEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Address> findAddressEntities(boolean all, int maxResults,
                                            int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Emails> criteriaQuery = criteriaBuilder.createQuery(Emails.class);
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

  public Address findAddress(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Address.class, id);
    } finally {
      em.close();
    }
  }

  public int getAddressCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Address> addressRoot = criteriaQuery.from(Address.class);
      criteriaQuery.select(criteriaBuilder.count(addressRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public List<Address> findApproximateAddressesByCategory(Categories category, String address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Address> findAddressByRange(Categories category, String location, int range) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Services> findServicesByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Advertisers> findAdvertizersByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Clients> findClientsByAddress(Address address) {
    // TODO Auto-generated method stub
    return null;
  }



}