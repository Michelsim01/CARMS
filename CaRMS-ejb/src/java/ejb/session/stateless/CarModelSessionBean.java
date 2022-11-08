/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Jester
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarModelSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public CarModel createCarModel(Long carCategoryId, CarModel newModel) throws CarCategoryNotFoundException {
        try {
            CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
            newModel.setCarCategory(carCategory);
            carCategory.getModels().add(newModel);

            em.persist(newModel);
            em.flush();

            return newModel;
        } catch (CarCategoryNotFoundException ex) {
            throw new CarCategoryNotFoundException("Car Category does not exist! Car Model not created!");
        }
    }

    @Override
    public List<CarModel> retrieveAllModels() {
        Query query = em.createQuery("SELECT cm FROM CarModel m ORDER BY cm.carCategory.carCategoryName, cm.makeName, cm.modelName ASC");
        return query.getResultList();
    }

    @Override
    public CarModel retrieveModelById(Long modelId) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, modelId);
        if (carModel != null) {
            return carModel;
        } else {
            throw new CarModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }

    @Override
    public void deleteModel(Long modelId) throws CarModelNotFoundException {
        try {
            CarModel modelToDelete = retrieveModelById(modelId);

            if (modelToDelete.getCars().isEmpty()) {
                em.remove(modelToDelete);
            } else {
                modelToDelete.isDisabled(true);
            }
        } catch (CarModelNotFoundException ex) {
            throw new CarModelNotFoundException("Model ID " + modelId + " does not exist.");
        }
    }

    @Override
    public void updateModel(CarModel model) throws CarModelNotFoundException, InputDataValidationException {
        if (model != null && model.getCarModelId() != null) {
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                try {
                    CarModel modelToUpdate = retrieveModelById(model.getCarModelId());
                    modelToUpdate.setMake(model.getMake());
                    modelToUpdate.setModel(model.getModel());
                    modelToUpdate.isDisabled(model.isDisabled());
                } catch (CarModelNotFoundException ex) {
                    throw new CarModelNotFoundException("Model Id for model to be updated is not found!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarModel>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }
}
