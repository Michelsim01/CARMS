/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Jester
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarCategorySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createCarCategory(CarCategory newCarCategory) {
        em.persist(newCarCategory);
        em.flush();
        return newCarCategory.getCarCategoryId();
    }
    
    @Override
    public List<CarCategory> retrieveAllCarCategories() {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();
    }
    
    @Override
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);
        return carCategory;
    }
}
