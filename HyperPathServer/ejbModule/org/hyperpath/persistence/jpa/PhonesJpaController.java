package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.Entities;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class PhonesJpaController implements Serializable {
  private static final long serialVersionUID = 8649955848124565699L;
  private EntityManager em = null;

	public PhonesJpaController(EntityManagerFactory emf) {
      this.em = emf.createEntityManager();
  }

	public PhonesJpaController(EntityManager mockedEM) {
		this.em = mockedEM;
	}

    public void create(Phones phones) throws RollbackFailureException, Exception {
        if (phones.getEntitiesList() == null) {
            phones.setEntitiesList(new ArrayList<Entities>());
        }
        try {
            List<Entities> attachedEntitiesList = new ArrayList<Entities>();
            for (Entities entitiesListEntitiesToAttach : phones.getEntitiesList()) {
                entitiesListEntitiesToAttach = em.getReference(entitiesListEntitiesToAttach.getClass(), entitiesListEntitiesToAttach.getId());
                attachedEntitiesList.add(entitiesListEntitiesToAttach);
            }
            phones.setEntitiesList(attachedEntitiesList);
            em.persist(phones);
            for (Entities entitiesListEntities : phones.getEntitiesList()) {
                entitiesListEntities.getPhonesList().add(phones);
                entitiesListEntities = em.merge(entitiesListEntities);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Phones phones) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            Phones persistentPhones = em.find(Phones.class, phones.getId());
            List<Entities> entitiesListOld = persistentPhones.getEntitiesList();
            List<Entities> entitiesListNew = phones.getEntitiesList();
            List<Entities> attachedEntitiesListNew = new ArrayList<Entities>();
            for (Entities entitiesListNewEntitiesToAttach : entitiesListNew) {
                entitiesListNewEntitiesToAttach = em.getReference(entitiesListNewEntitiesToAttach.getClass(), entitiesListNewEntitiesToAttach.getId());
                attachedEntitiesListNew.add(entitiesListNewEntitiesToAttach);
            }
            entitiesListNew = attachedEntitiesListNew;
            phones.setEntitiesList(entitiesListNew);
            phones = em.merge(phones);
            for (Entities entitiesListOldEntities : entitiesListOld) {
                if (!entitiesListNew.contains(entitiesListOldEntities)) {
                    entitiesListOldEntities.getPhonesList().remove(phones);
                    entitiesListOldEntities = em.merge(entitiesListOldEntities);
                }
            }
            for (Entities entitiesListNewEntities : entitiesListNew) {
                if (!entitiesListOld.contains(entitiesListNewEntities)) {
                    entitiesListNewEntities.getPhonesList().add(phones);
                    entitiesListNewEntities = em.merge(entitiesListNewEntities);
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            Phones phones;
            try {
                phones = em.getReference(Phones.class, id);
                phones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phones with id " + id + " no longer exists.", enfe);
            }
            List<Entities> entitiesList = phones.getEntitiesList();
            for (Entities entitiesListEntities : entitiesList) {
                entitiesListEntities.getPhonesList().remove(phones);
                entitiesListEntities = em.merge(entitiesListEntities);
            }
            em.remove(phones);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Phones> findPhonesEntities() {
        return findPhonesEntities(true, -1, -1);
    }

    public List<Phones> findPhonesEntities(int maxResults, int firstResult) {
        return findPhonesEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Phones> findPhonesEntities(boolean all, int maxResults, int firstResult) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Phones> criteriaQuery = criteriaBuilder.createQuery(Phones.class);
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

    public Phones findPhones(Integer id) {
      try {
        return em.find(Phones.class, id);
      } finally {
        em.close();
      }
    }

    @SuppressWarnings("unchecked")
    public List<Phones> findExactPhone(String phoneNumber) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Phones> criteriaQuery = criteriaBuilder
            .createQuery(Phones.class);
        Root<Phones> phoneRoot = criteriaQuery.from(Phones.class);
        criteriaQuery.select(phoneRoot).where(
            criteriaBuilder.equal(phoneRoot.get("number"),
                phoneNumber));
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
      } finally {
        em.close();
      }
    }

    @SuppressWarnings("unchecked")
    public List<Phones> findApproximatePhones(String phoneNumber) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Phones> criteriaQuery = criteriaBuilder.createQuery(Phones.class);
        Root<Phones> phoneRoot = criteriaQuery.from(Phones.class);
        criteriaQuery.select(phoneRoot).where(
            criteriaBuilder.like(phoneRoot.<String> get("number"),
                '%' + phoneNumber + '%'));
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
      } finally {
        em.close();
      }
    }

    public int getPhonesCount() throws Exception {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder
            .createQuery(Long.class);
        Root<Phones> phoneRoot = criteriaQuery.from(Phones.class);
        criteriaQuery.select(criteriaBuilder.count(phoneRoot));
        Query q = em.createQuery(criteriaQuery);
        return ((Long) q.getSingleResult()).intValue();
      } finally {
          em.close();
      }
    }

}
