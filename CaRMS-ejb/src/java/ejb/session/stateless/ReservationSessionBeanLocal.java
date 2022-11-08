/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface ReservationSessionBeanLocal {

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public Long createReservation(Reservation newReservation, Long customerId, Long carId, Long pickUpOutletId, Long returnOutletId) throws CustomerNotFoundException, OutletNotFoundException, CarNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public void cancelReservation(Long reservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReservations();
    
}
