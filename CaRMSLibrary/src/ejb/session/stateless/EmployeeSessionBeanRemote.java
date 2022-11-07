/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author michelsim
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
    public Long createEmployee(Employee newEmployee, Long outletId) throws UnknownPersistenceException, EmployeeUsernameExistException, OutletNotFoundException, InputDataValidationException;
    
     public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
}
