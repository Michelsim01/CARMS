/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Local
public interface OutletSessionBeanLocal {

    public Long createOutlet(Outlet newOutlet) throws InputDataValidationException, UnknownPersistenceException, OutletNameExistException;

    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
}
