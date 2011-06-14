package org.hyperpath.services;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.jpa.CategoriesJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "CategoriesServices")
@Stateless()
public class CategoriesServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  CategoriesJpaController controller;

  /**
   * Add new category
   */
  @WebMethod(operationName = "addCategory")
  public void addService(@WebParam(name = "category") Categories category)
      throws Exception, PreexistingEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    controller.create(category);
  }

  /**
   * Update category
   */
  @WebMethod(operationName = "updateCategory")
  public void updateCategory(@WebParam(name = "category") Categories category)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    controller.edit(category);
  }

  /**
   * Delete category
   */
  @WebMethod(operationName = "deleteCategory")
  public void deleteCategory(@WebParam(name = "categoryId") Integer categoryId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    controller.destroy(categoryId);
  }

  /**
   * Find category by exact label
   */
  @WebMethod(operationName = "findCategoryByExactLabel")
  public List<Categories> findCategoryByExactLabel(@WebParam(name = "categoryLabel") String categoryLabel)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.findCategoriesByExacLabel(categoryLabel);
  }

  /**
   * Find category by approximate label
   */
  @WebMethod(operationName = "findCategoriesByApproximateLabel")
  public List<Categories> findCategoriesByApproximateLabel(@WebParam(name = "categoryLabel") String categoryLabel)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.findCategoriesByApproximateLabel(categoryLabel);
  }

  /**
   * Find category by exact description
   */
  @WebMethod(operationName = "findCategoriesByExactDescription")
  public List<Categories> findCategoriesByExactDescription(@WebParam(name = "categoryLabel") String categoryDescription)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.findCategoriesByExactDescription(categoryDescription);
  }

  /**
   * Find category by approximate description
   */
  @WebMethod(operationName = "findCategoriesByApproximateDescription")
  public List<Categories> findCategoriesByApproximateDescription(@WebParam(name = "categoryLabel") String categoryDescription)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.findCategoriesByApproximateDescription(categoryDescription);
  }

  /**
   * Find category by approximate description
   */
  @WebMethod(operationName = "listAllCategories")
  public List<Categories> listAllCategories()
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.findCategoriesEntities();
  }

  /**
   * Find total categories number
   */
  @WebMethod(operationName = "countCategories")
  public int countCategories()
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new CategoriesJpaController(utx, emf);
    return controller.getCategoriesCount();
  }
}
