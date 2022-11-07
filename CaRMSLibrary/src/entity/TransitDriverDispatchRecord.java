/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author michelsim
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchRecordId;
    @Column(nullable = false)
    private Boolean isComplete;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transitDate;
    
    @ManyToOne(optional = false, cascade = {})
    @JoinColumn(nullable = false)
    private Employee transitDriver;
    
    @ManyToOne(optional = false, cascade = {})
    @JoinColumn(nullable = false)
    private Outlet dropOffOutlet;
    
    @OneToOne(optional = false, cascade = {})
    @JoinColumn(nullable = false)
    private Reservation reservation;

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(Boolean isComplete, Date transitDate) {
        this.isComplete = isComplete;
        this.transitDate = transitDate;
    }

    public TransitDriverDispatchRecord(Boolean isComplete, Date transitDate, Employee transitDriver, Outlet dropOffOutlet, Reservation reservation) {
        this.isComplete = isComplete;
        this.transitDate = transitDate;
        this.transitDriver = transitDriver;
        this.dropOffOutlet = dropOffOutlet;
        this.reservation = reservation;
    }
    
    public Long getTransitDriverDispatchRecordId() {
        return transitDriverDispatchRecordId;
    }

    public void setTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) {
        this.transitDriverDispatchRecordId = transitDriverDispatchRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchRecordId != null ? transitDriverDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchRecordId == null && other.transitDriverDispatchRecordId != null) || (this.transitDriverDispatchRecordId != null && !this.transitDriverDispatchRecordId.equals(other.transitDriverDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecord[ id=" + transitDriverDispatchRecordId + " ]";
    }

    /**
     * @return the isComplete
     */
    public Boolean getIsComplete() {
        return isComplete;
    }

    /**
     * @param isComplete the isComplete to set
     */
    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    /**
     * @return the transitDate
     */
    public Date getTransitDate() {
        return transitDate;
    }

    /**
     * @param transitDate the transitDate to set
     */
    public void setTransitDate(Date transitDate) {
        this.transitDate = transitDate;
    }

    /**
     * @return the transitDriver
     */
    public Employee getTransitDriver() {
        return transitDriver;
    }

    /**
     * @param transitDriver the transitDriver to set
     */
    public void setTransitDriver(Employee transitDriver) {
        this.transitDriver = transitDriver;
    }

    /**
     * @return the dropOffOutlet
     */
    public Outlet getDropOffOutlet() {
        return dropOffOutlet;
    }

    /**
     * @param dropOffOutlet the dropOffOutlet to set
     */
    public void setDropOffOutlet(Outlet dropOffOutlet) {
        this.dropOffOutlet = dropOffOutlet;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}
