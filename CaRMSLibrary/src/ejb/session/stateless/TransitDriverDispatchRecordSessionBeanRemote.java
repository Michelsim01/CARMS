/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import javax.ejb.Remote;
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
@Remote
public interface TransitDriverDispatchRecordSessionBeanRemote {
    public Long createTransitDriverDispatchRecord(TransitDriverDispatchRecord newTransitDriverDispatchRecord, Long employeeId, Long dropOffOutletId, Long reservationId) throws EmployeeNotFoundException, OutletNotFoundException, ReservationNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long id) throws TransitDriverDispatchRecordNotFoundException;
    
}
