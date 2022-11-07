/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
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
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    

    public EmployeeSessionBean() {
        
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Long createEmployee(Employee newEmployee, Long outletId) throws UnknownPersistenceException, EmployeeUsernameExistException, OutletNotFoundException, InputDataValidationException {

        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);

        if (constraintViolations.isEmpty()) {
            
            try {
                
                Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
                em.persist(newEmployee);
                newEmployee.setOutlet(outlet);
                em.flush();
                return newEmployee.getEmployeeId();
            } 
            catch (PersistenceException ex) {
                
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new EmployeeUsernameExistException("Employee username already exists!");
                    } 
                    else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } 
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } 
            catch (OutletNotFoundException ex) {
                throw new OutletNotFoundException("Outlet with outlet id: " + outletId + " not found!");
            }
        }
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
         Employee employee = em.find(Employee.class, employeeId);
        
        if (employee!= null) {
            return employee;
        } 
        else {
            throw new EmployeeNotFoundException("Employee with employee id: " + employeeId + " not found!");
        }
    }
    
     private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }  
}
    
