/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

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

    @Override
    public Long createCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<CarCategory>> constraintViolations = validator.validate(newCarCategory);

            if (constraintViolations.isEmpty()) {
                em.persist(newCarCategory);
                em.flush();
                return newCarCategory.getCarCategoryId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarCategoryExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarCategory>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    @Override
    public List<CarCategory> retrieveAllCarCategories() {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();
    }

    @Override
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);

        if (carCategory != null) {
            return carCategory;
        } else {
            throw new CarCategoryNotFoundException("Car Category ID " + carCategoryId + " does not exist!");
        }
    }
}
