/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.PartnerNameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Long createPartner(Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException;

    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;
}
