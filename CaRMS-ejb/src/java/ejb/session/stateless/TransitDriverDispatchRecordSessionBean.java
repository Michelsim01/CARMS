/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Stateless
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransitDriverDispatchRecordSessionBean() {

        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public Long createTransitDriverDispatchRecord(TransitDriverDispatchRecord newTransitDriverDispatchRecord, Long employeeId, Long dropOffOutletId, Long reservationId) throws EmployeeNotFoundException, OutletNotFoundException, ReservationNotFoundException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<TransitDriverDispatchRecord>> constraintViolations = validator.validate(newTransitDriverDispatchRecord);

        if (constraintViolations.isEmpty()) {

            try {

                em.persist(newTransitDriverDispatchRecord);
                Employee transitDriver = employeeSessionBean.retrieveEmployeeById(employeeId);

                // associate transit driver and transit driver dispatch record
                newTransitDriverDispatchRecord.setTransitDriver(transitDriver);
                transitDriver.getTransitDriverDispatchRecords().add(newTransitDriverDispatchRecord);

                Outlet dropOffOutlet = outletSessionBean.retrieveOutletById(dropOffOutletId);

                // associate transit driver dispatch record and drop off outlet
                newTransitDriverDispatchRecord.setDropOffOutlet(dropOffOutlet);
                dropOffOutlet.getTransitDriverDispatchRecords().add(newTransitDriverDispatchRecord);

                Reservation reservation = reservationSessionBean.retrieveReservationById(reservationId);

                // associate transit driver dispatch record and reservation
                newTransitDriverDispatchRecord.setReservation(reservation);
                reservation.setTransitDriverDispatchRecord(newTransitDriverDispatchRecord);

                em.flush();
                return newTransitDriverDispatchRecord.getTransitDriverDispatchRecordId();

            } catch (PersistenceException ex) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (OutletNotFoundException ex) {
                throw new OutletNotFoundException("Outlet with pickup outlet ID: " + dropOffOutletId + " not found!");
            } catch (EmployeeNotFoundException ex) {
                throw new EmployeeNotFoundException("Employee with employee ID: " + employeeId + " does not exist!");
            } catch (ReservationNotFoundException ex) {
                throw new ReservationNotFoundException("Reservation with reservation ID: " + reservationId + " does not exist!");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long id) throws TransitDriverDispatchRecordNotFoundException {
         TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, id);
        
        if (transitDriverDispatchRecord!= null) {
            return transitDriverDispatchRecord;
        } 
        else {
            throw new TransitDriverDispatchRecordNotFoundException("Transit driver dispatch with transit driver dispatch id: " + id + " not found!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransitDriverDispatchRecord>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

}
