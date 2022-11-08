/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarModel;
import entity.Outlet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.exception.CarExistException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author Jester
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB(name = "CarModelSessionBeanLocal")
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewCar(Long modelId, Long outletId, Car newCar) throws CarExistException, OutletNotFoundException, CarModelNotFoundException, InputDataValidationException {
        try {

            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {

                CarModel model = carModelSessionBeanLocal.retrieveModelById(modelId);
                Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
                em.persist(newCar);
                newCar.setCarModel(model);
                newCar.setOutlet(outlet);
                outlet.getCars().add(newCar);
                model.getCars().add(newCar);
                em.flush();
                return newCar.getCarId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (PersistenceException ex) {
            throw new CarExistException("License plate number " + newCar.getLicensePlateNumber() + " already exists!");
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet not found" + outletId);
        } catch (CarModelNotFoundException ex) {
            throw new CarModelNotFoundException("Model associated to the car does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.category.name, c.model.modelName,c.licensePlateNumber ASC");
        return query.getResultList();
    }

    public Car retrieveCarById(Long carId) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);
        if (car != null) {
            return car;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException {
        try {
            Car carToRemove = retrieveCarById(carId);
            if (carToRemove != null) {
                if (carToRemove.getStatus() == CarStatusEnum.ON_RENT) {
                    carToRemove.setOutlet(null);
                    carToRemove.setIsDisabled(true);
                } else {
                    em.remove(carToRemove);
                }
            }
        } catch (CarNotFoundException ex) {
            throw new CarNotFoundException("Car ID " + carId + "does not exist!");
        }
    }

    @Override
    public void updateCar(Car car) throws CarNotFoundException, InputDataValidationException {
        if (car.getCarId() != null) {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                Car carToUpdate = retrieveCarById(car.getCarId());
                carToUpdate.setLicensePlateNumber(car.getLicensePlateNumber());
                carToUpdate.setColour(car.getColour());
                carToUpdate.setIsDisabled(car.getIsDisabled());
                carToUpdate.setStatus(car.getStatus());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarNotFoundException("Car ID is null!");
        }
    }
}
