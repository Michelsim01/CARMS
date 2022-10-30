/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsclient;

import ejb.session.stateless.CarSessionBeanRemote;
import entity.Car;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Jester
 */
public class Main {

    @EJB(name = "CarSessionBeanRemote")
    private static CarSessionBeanRemote carSessionBeanRemote;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        
        for(Car car:cars)
        {
            System.out.println("carId=" + car.getCarId() + ";, licensePlate=" + car.getLicensePlateNumber());
        }
    }
    
}
