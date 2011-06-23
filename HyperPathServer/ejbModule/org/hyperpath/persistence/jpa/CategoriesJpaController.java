package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class CategoriesJpaController implements Serializable {
	private static final long serialVersionUID = 8689698790842031781L;
    private EntityManager em         = null;

	public CategoriesJpaController(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
    }

	public CategoriesJpaController(EntityManager mockedEm){
		this.em = mockedEm;
	}

    public void create(Categories categories) throws RollbackFailureException, Exception {
        if (categories.getServicesList() == null) {
            categories.setServicesList(new ArrayList<Services>());
        }
	    try {
	        List<Services> attachedServicesList = new ArrayList<Services>();
	        for (Services servicesListServicesToAttach : categories.getServicesList()) {
	            servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
	            attachedServicesList.add(servicesListServicesToAttach);
	        }
	        categories.setServicesList(attachedServicesList);
	        em.persist(categories);
	        for (Services servicesListServices : categories.getServicesList()) {
	            Categories oldCategoriesIdOfServicesListServices = servicesListServices.getCategory();
	            servicesListServices.setCategory(categories);
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

    public void edit(Categories categories) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
        	Categories old_categories = em.find(Categories.class, categories.getId());
        	if(categories != null){
        		old_categories.setLabel(categories.getLabel());
        		old_categories.setDescription(categories.getDescription());
        		categories = em.merge(old_categories);
        	}

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
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

    @SuppressWarnings("unchecked")
    private List<Categories> findCategoriesEntities(boolean all, int maxResults, int firstResult) {
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

    public Categories findCategories(Integer id) {
        try {
            return em.find(Categories.class, id);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
	public List<Services> findServicesByCategories(Integer categoryId){
        try {
              CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
              CriteriaQuery<Services> criteriaQuery = criteriaBuilder.createQuery(Services.class);
              Root<Services> serviceRoot = criteriaQuery.from(Services.class);
              criteriaQuery.select(serviceRoot).where( criteriaBuilder.equal(serviceRoot.get("category").get("id"),categoryId));
              Query query = em.createQuery(criteriaQuery);
              return query.getResultList();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Categories> findCategoriesByExactLabel(String categoryLabel) {
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
