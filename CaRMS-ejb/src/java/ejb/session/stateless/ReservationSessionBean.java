/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
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
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {

        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public Long createReservation(Reservation newReservation, Long customerId, Long carId, Long pickUpOutletId, Long returnOutletId) throws CustomerNotFoundException, OutletNotFoundException, CarNotFoundException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);

        if (constraintViolations.isEmpty()) {

            try {
                em.persist(newReservation);
                Customer customer = customerSessionBean.retrieveCustomerById(customerId);

                // asscoiate customer and reservation - one to many
                customer.getReservations().add(newReservation);
                newReservation.setCustomer(customer);

                Car car = carSessionBean.retrieveCarById(carId);

                // associate car and reservation 
                car.getReservations().add(newReservation);
                newReservation.setCar(car);

                // associate pickup and return outlet and car
                Outlet pickUpOutlet = outletSessionBean.retrieveOutletById(pickUpOutletId);
                newReservation.setPickUpOutlet(pickUpOutlet);

                Outlet returnOutlet = outletSessionBean.retrieveOutletById(returnOutletId);
                newReservation.setReturnOutlet(returnOutlet);
                
                em.flush();
                return newReservation.getReservationId();
                
            } 
            catch (PersistenceException ex) {

                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } 
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } 
            catch (OutletNotFoundException ex) {
                throw new OutletNotFoundException("Outlet with pickup outlet ID: " + pickUpOutletId + " or return outlet ID: " + returnOutletId + " not found!");
            } 
            catch (CustomerNotFoundException ex) {
                throw new CustomerNotFoundException("Customer with customer ID: " + customerId + " does not exist!");
            }
            catch (CarNotFoundException ex) {
                throw new CarNotFoundException("Car with car ID: " + carId + " does not exist!");
            }
        } 
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        if (reservation!= null) {
            return reservation;
        } 
        else {
            throw new ReservationNotFoundException("Reservation with reservation id: " + reservationId + " not found!");
        }
    }
    
    public void cancelReservation(Long reservationId) throws ReservationNotFoundException {
        try {
            Reservation reservationToCancel = retrieveReservationById(reservationId);
        
            reservationToCancel.setIsCancelled(Boolean.TRUE);
            
        } 
        catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Reservation with reservation id: " + reservationId + " not found!");
        }
    }
    
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        return query.getResultList();
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }  
}
