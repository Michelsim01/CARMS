/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import entity.Car;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jester
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;


    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;



    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct()
    {
        if(em.find(Car.class, 1L) == null)
        {
            carSessionBeanLocal.createNewCar(new Car("S123456A"));
            carSessionBeanLocal.createNewCar(new Car("S234567A"));
            carSessionBeanLocal.createNewCar(new Car("S345678A"));
            carSessionBeanLocal.createNewCar(new Car("S456789A"));
            carSessionBeanLocal.createNewCar(new Car("S123341V"));
        }
    }
}
