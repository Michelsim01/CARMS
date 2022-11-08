/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Jester
 */
@Remote
public interface CarModelSessionBeanRemote {

    public List<CarModel> retrieveAllModels();

    public CarModel retrieveModelById(Long modelId) throws CarModelNotFoundException;

    public CarModel createCarModel(Long carCategoryId, CarModel newModel) throws CarCategoryNotFoundException;
    
    public void deleteModel(Long modelId) throws CarModelNotFoundException;
    
    public void updateModel(CarModel model) throws CarModelNotFoundException, InputDataValidationException;

}
