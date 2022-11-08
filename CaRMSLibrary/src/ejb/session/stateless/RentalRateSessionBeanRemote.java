/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author Jester
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    public Long createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CarCategoryNotFoundException;
    
    public List<RentalRate> retrieveAllRentalRates() throws RentalRateNotFoundException;
    
    public RentalRate retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException;
    
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;
    
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException;
    
}
