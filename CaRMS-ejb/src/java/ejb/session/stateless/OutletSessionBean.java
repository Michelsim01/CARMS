/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;


    public OutletSessionBean() {
        
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Long createOutlet(Outlet newOutlet) throws InputDataValidationException, UnknownPersistenceException, OutletNameExistException {

        Set<ConstraintViolation<Outlet>> constraintViolations = validator.validate(newOutlet);

        if (constraintViolations.isEmpty()) {
            
            try {
                
                em.persist(newOutlet);
                em.flush();
                return newOutlet.getOutletId();
                
            } 
            catch (PersistenceException ex) {
                
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        
                        throw new OutletNameExistException("This outlet name already exists!");
                    } 
                    else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } 
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException {
        
        Outlet outlet = em.find(Outlet.class, outletId);
        
        if (outlet!= null) {
            return outlet;
        } 
        else {
            throw new OutletNotFoundException("Outlet with outlet id: " + outletId + " not found!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Outlet>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }  
}
