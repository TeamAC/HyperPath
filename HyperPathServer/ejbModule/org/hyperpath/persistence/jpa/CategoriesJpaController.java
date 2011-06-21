package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

public class CategoriesJpaController implements Serializable {
  private static final long serialVersionUID = 8448103872273399270L;

  public CategoriesJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Categories categories) throws Exception {
      if (categories.getServicesList() == null) {
          categories.setServicesList(new ArrayList<Services>());
      }
      EntityManager em = null;
      try {
          em = getEntityManager();
          List<Services> attachedServicesList = new ArrayList<Services>();
          for (Services servicesListServicesToAttach : categories.getServicesList()) {
              servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
              attachedServicesList.add(servicesListServicesToAttach);
          }
          categories.setServicesList(attachedServicesList);
          em.persist(categories);
          for (Services servicesListServices : categories.getServicesList()) {
              Categories oldCategoriesIdOfServicesListServices = servicesListServices.getCategoriesId();
              servicesListServices.setCategoriesId(categories);
              servicesListServices = em.merge(servicesListServices);
              if (oldCategoriesIdOfServicesListServices != null) {
                  oldCategoriesIdOfServicesListServices.getServicesList().remove(servicesListServices);
                  oldCategoriesIdOfServicesListServices = em.merge(oldCategoriesIdOfServicesListServices);
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

  public void edit(Categories categories) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Categories persistentCategories = em.find(Categories.class, categories.getId());
            List<Services> servicesListOld = persistentCategories.getServicesList();
            List<Services> servicesListNew = categories.getServicesList();
            List<String> illegalOrphanMessages = null;
            for (Services servicesListOldServices : servicesListOld) {
                if (!servicesListNew.contains(servicesListOldServices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Services " + servicesListOldServices + " since its categoriesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Services> attachedServicesListNew = new ArrayList<Services>();
            for (Services servicesListNewServicesToAttach : servicesListNew) {
                servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
                attachedServicesListNew.add(servicesListNewServicesToAttach);
            }
            servicesListNew = attachedServicesListNew;
            categories.setServicesList(servicesListNew);
            categories = em.merge(categories);
            for (Services servicesListNewServices : servicesListNew) {
                if (!servicesListOld.contains(servicesListNewServices)) {
                    Categories oldCategoriesIdOfServicesListNewServices = servicesListNewServices.getCategoriesId();
                    servicesListNewServices.setCategoriesId(categories);
                    servicesListNewServices = em.merge(servicesListNewServices);
                    if (oldCategoriesIdOfServicesListNewServices != null && !oldCategoriesIdOfServicesListNewServices.equals(categories)) {
                        oldCategoriesIdOfServicesListNewServices.getServicesList().remove(servicesListNewServices);
                        oldCategoriesIdOfServicesListNewServices = em.merge(oldCategoriesIdOfServicesListNewServices);
                    }
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categories.getId();
                if (findCategories(id) == null) {
                    throw new NonexistentEntityException("The categories with id " + id + " no longer exists.");
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
          Categories categories;
          try {
              categories = em.getReference(Categories.class, id);
              categories.getId();
          } catch (EntityNotFoundException enfe) {
              throw new NonexistentEntityException("The categories with id " + id + " no longer exists.", enfe);
          }
          List<String> illegalOrphanMessages = null;
          List<Services> servicesListOrphanCheck = categories.getServicesList();
          for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
              if (illegalOrphanMessages == null) {
                  illegalOrphanMessages = new ArrayList<String>();
              }
              illegalOrphanMessages.add("This Categories (" + categories + ") cannot be destroyed since the Services " + servicesListOrphanCheckServices + " in its servicesList field has a non-nullable categoriesId field.");
          }
          if (illegalOrphanMessages != null) {
              throw new IllegalOrphanException(illegalOrphanMessages);
          }
          em.remove(categories);
      } catch (Exception ex) {
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

  public List<Categories> findCategoriesEntities(int maxResults, int firstResult) {
    return findCategoriesEntities(false, maxResults, firstResult);
  }

  public Categories findCategories(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categories.class, id);
        } finally {
            em.close();
        }
    }

  @SuppressWarnings("unchecked")
  private List<Categories> findCategoriesEntities(boolean all, int maxResults, int firstResult) {
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
