/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
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
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author Jester
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    public RentalRateSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CarCategoryNotFoundException {
        CarCategory category = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);

        em.persist(newRentalRate);
        newRentalRate.setCarCategory(category);
        category.getRentalRates().add(newRentalRate);
        em.flush();
        return newRentalRate.getRentalRateId();
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() throws RentalRateNotFoundException {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr");

        return query.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException {

        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRateToRemove = retrieveRentalRateByRentalId(rentalRateId);
        rentalRateToRemove.getCarCategory().getRentalRates().remove(rentalRateToRemove);
        em.remove(rentalRateToRemove);
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                RentalRate rentalRateToUpdate = retrieveRentalRateByRentalId(rentalRate.getRentalRateId());

                if (rentalRateToUpdate.getRentalRateId().equals(rentalRate.getRentalRateId())) {
                    rentalRateToUpdate.setRentalRateName(rentalRate.getRentalRateName());
                    rentalRateToUpdate.setDailyRate(rentalRate.getDailyRate());
                    rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
                    rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
                    rentalRateToUpdate.setCarCategory(rentalRate.getCarCategory());
                } else {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            } else {
                throw new RentalRateNotFoundException("Rental rate ID not provided for product to be updated");
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }
}
