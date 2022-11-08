/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;

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
    public CarModel retrieveModelByModelId(Long modelId) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, modelId);

        if (carModel != null) {
            return carModel;
        } else {
            throw new CarModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }
}
