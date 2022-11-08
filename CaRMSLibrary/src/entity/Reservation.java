/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author michelsim
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Column(nullable = false)
    private Boolean paid;
    @Column(nullable = false)
    private Boolean isCancelled;
    @Column(nullable = false)
    private Boolean isCompleted;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Column(nullable = false)
    private BigDecimal totalCost;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car car;
    
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Outlet pickUpOutlet;
    
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Outlet returnOutlet;
    
    @OneToMany(mappedBy = "reservation")
    private List<RentalRate> rentalRates;
    
    @ManyToOne
    private Partner partner;
    
    @OneToOne(fetch = FetchType.EAGER)
    private TransitDriverDispatchRecord transitDriverDispatchRecord;
    
            
    public Reservation() {
        this.rentalRates = new ArrayList<>();
    }

    public Reservation(Boolean paid, Boolean isCancelled, Boolean isCompleted, Date startDate, Date endDate, BigDecimal totalCost, Customer customer, Car car, Outlet pickUpOutlet, Outlet returnOutlet, List<RentalRate> rentalRates, Partner partner, TransitDriverDispatchRecord transitDriverDispatchRecord) {
        this.paid = paid;
        this.isCancelled = isCancelled;
        this.isCompleted = isCompleted;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.customer = customer;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.returnOutlet = returnOutlet;
        this.rentalRates = rentalRates;
        this.partner = partner;
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the isCancelled
     */
    public Boolean getIsCancelled() {
        return isCancelled;
    }

    /**
     * @param isCancelled the isCancelled to set
     */
    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * @return the isCompleted
     */
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the pickUpOutlet
     */
    public Outlet getPickUpOutlet() {
        return pickUpOutlet;
    }

    /**
     * @param pickUpOutlet the pickUpOutlet to set
     */
    public void setPickUpOutlet(Outlet pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    /**
     * @return the returnOutlet
     */
    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the transitDriverDispatchRecord
     */
    public TransitDriverDispatchRecord getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    /**
     * @param transitDriverDispatchRecord the transitDriverDispatchRecord to set
     */
    public void setTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
}
