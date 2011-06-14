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
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class CategoriesJpaController implements Serializable {
  private static final long serialVersionUID = 8448103872273399270L;

  public CategoriesJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Categories categories) throws RollbackFailureException,
      Exception {
    if (categories.getServicesList() == null) {
      categories.setServicesList(new ArrayList<Services>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      List<Services> attachedServicesList = new ArrayList<Services>();
      for (Services servicesListServicesToAttach : categories
          .getServicesList()) {
        servicesListServicesToAttach = em.getReference(
            servicesListServicesToAttach.getClass(),
            servicesListServicesToAttach.getServicesPK());
        attachedServicesList.add(servicesListServicesToAttach);
      }
      categories.setServicesList(attachedServicesList);
      em.persist(categories);
      for (Services servicesListServices : categories.getServicesList()) {
        Categories oldCategoriesOfServicesListServices = servicesListServices
            .getCategories();
        servicesListServices.setCategories(categories);
        servicesListServices = em.merge(servicesListServices);
        if (oldCategoriesOfServicesListServices != null) {
          oldCategoriesOfServicesListServices.getServicesList()
              .remove(servicesListServices);
          oldCategoriesOfServicesListServices = em
              .merge(oldCategoriesOfServicesListServices);
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
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Categories categories) throws IllegalOrphanException,
      NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Categories persistentCategories = em.find(Categories.class,
          categories.getId());
      List<Services> servicesListOld = persistentCategories
          .getServicesList();
      List<Services> servicesListNew = categories.getServicesList();
      List<String> illegalOrphanMessages = null;
      for (Services servicesListOldServices : servicesListOld) {
        if (!servicesListNew.contains(servicesListOldServices)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<String>();
          }
          illegalOrphanMessages.add("You must retain Services "
              + servicesListOldServices
              + " since its categories field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<Services> attachedServicesListNew = new ArrayList<Services>();
      for (Services servicesListNewServicesToAttach : servicesListNew) {
        servicesListNewServicesToAttach = em.getReference(
            servicesListNewServicesToAttach.getClass(),
            servicesListNewServicesToAttach.getServicesPK());
        attachedServicesListNew.add(servicesListNewServicesToAttach);
      }
      servicesListNew = attachedServicesListNew;
      categories.setServicesList(servicesListNew);
      categories = em.merge(categories);
      for (Services servicesListNewServices : servicesListNew) {
        if (!servicesListOld.contains(servicesListNewServices)) {
          Categories oldCategoriesOfServicesListNewServices = servicesListNewServices
              .getCategories();
          servicesListNewServices.setCategories(categories);
          servicesListNewServices = em.merge(servicesListNewServices);
          if (oldCategoriesOfServicesListNewServices != null
              && !oldCategoriesOfServicesListNewServices
                  .equals(categories)) {
            oldCategoriesOfServicesListNewServices
                .getServicesList().remove(
                    servicesListNewServices);
            oldCategoriesOfServicesListNewServices = em
                .merge(oldCategoriesOfServicesListNewServices);
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
        Integer id = categories.getId();
        if (findCategories(id) == null) {
          throw new NonexistentEntityException(
              "The categories with id " + id
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
      Categories categories;
      try {
        categories = em.getReference(Categories.class, id);
        categories.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The categories with id "
            + id + " no longer exists.", enfe);
      }
      List<String> illegalOrphanMessages = null;
      List<Services> servicesListOrphanCheck = categories
          .getServicesList();
      for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages
            .add("This Categories ("
                + categories
                + ") cannot be destroyed since the Services "
                + servicesListOrphanCheckServices
                + " in its servicesList field has a non-nullable categories field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(categories);
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

  public List<Categories> findCategoriesEntities() {
    return findCategoriesEntities(true, -1, -1);
  }

  public List<Categories> findCategoriesEntities(int maxResults,
                                                 int firstResult) {
    return findCategoriesEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Categories> findCategoriesEntities(boolean all,
                                                  int maxResults,
                                                  int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
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

  @SuppressWarnings("unchecked")
  public List<Categories> findCategoriesByExactLabel(String categoryLabel) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
      Root<Categories> categoryRoot = criteriaQuery.from(Categories.class);
      criteriaQuery.select(categoryRoot).where( criteriaBuilder.equal(categoryRoot.get("label"),categoryLabel));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Categories> findCategoriesByApproximateLabel(String categoryLabel) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
      Root<Categories> categoryRoot = criteriaQuery.from(Categories.class);
      criteriaQuery.select(categoryRoot).where(criteriaBuilder.like(categoryRoot.<String> get("label"),'%' + categoryLabel + '%'));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Categories> findCategoriesByExactDescription(String categoryDescription) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
      Root<Categories> categoryRoot = criteriaQuery.from(Categories.class);
      criteriaQuery.select(categoryRoot).where( criteriaBuilder.equal(categoryRoot.get("description"),categoryDescription));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Categories> findCategoriesByApproximateDescription(String categoryDescription) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
      Root<Categories> categoryRoot = criteriaQuery.from(Categories.class);
      criteriaQuery.select(categoryRoot).where(criteriaBuilder.like(categoryRoot.<String> get("description"),'%' + categoryDescription + '%'));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  public Categories findCategories(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Categories.class, id);
    } finally {
      em.close();
    }
  }

  public int getCategoriesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Categories> categoryRoot = criteriaQuery.from(Categories.class);
      criteriaQuery.select(criteriaBuilder.count(categoryRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
