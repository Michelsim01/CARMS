/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author michelsim
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long outletId;
    @Column(nullable = false, length = 64, unique = true)
    private String outletName;
    @Column(nullable = false, length = 128, unique = true)
    private String outletAddress;
    @Column(nullable = false)
    private String openingTime; 
    @Column(nullable = false)
    private String closingTime;
    
    @OneToMany(mappedBy="outlet", cascade = {}, fetch = FetchType.EAGER)
    private List<Employee> employees;
    
    @OneToMany(mappedBy="outlet", cascade = {}, fetch = FetchType.EAGER)
    private List<Car> cars;
    
    @OneToMany(mappedBy="dropOffOutlet", cascade = {}, fetch = FetchType.EAGER)
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;
    

    public Outlet() {
        this.employees = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.transitDriverDispatchRecords = new ArrayList<>();
        
    }

    public Outlet(String outletName, String outletAddress, String openingTime, String closingTime) {
        this();
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Outlet(String outletName, String outletAddress, String openingTime, String closingTime, List<Employee> employees, List<Car> cars, List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.employees = employees;
        this.cars = cars;
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    /**
     * @return the outletName
     */
    public String getOutletName() {
        return outletName;
    }

    /**
     * @param outletName the outletName to set
     */
    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    /**
     * @return the outletAddress
     */
    public String getOutletAddress() {
        return outletAddress;
    }

    /**
     * @param outletAddress the outletAddress to set
     */
    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    /**
     * @return the openingTime
     */
    public String getOpeningTime() {
        return openingTime;
    }

    /**
     * @param openingTime the openingTime to set
     */
    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    /**
     * @return the closingTime
     */
    public String getClosingTime() {
        return closingTime;
    }

    /**
     * @param closingTime the closingTime to set
     */
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return the cars
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the transitDriverDispatchRecords
     */
    public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    /**
     * @param transitDriverDispatchRecords the transitDriverDispatchRecords to set
     */
    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }
    
}
