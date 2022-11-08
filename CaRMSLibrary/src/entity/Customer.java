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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author michelsim
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 64, unique = true)
    private String customerName;
    @Column(nullable = false, length = 64)
    private String email;
    @Column(nullable = false, length = 64)
    private String creditCardNumber;
    @Column(nullable = false, length = 64)
    private String cvv;
    @Column(nullable = false, length = 64)
    private String cardExpirationDate;
    
    @OneToMany(mappedBy="customer", cascade = {}, fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    
    @ManyToOne
    private Partner partner;

    public Customer() {
        this.reservations = new ArrayList<>();
    }

    public Customer(String customerName, String email, String creditCardNumber, String cvv, String cardExpirationDate) {
        this();
        this.customerName = customerName;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.cardExpirationDate = cardExpirationDate;
    }

    public Customer(String customerName, String email, String creditCardNumber, String cvv, String cardExpirationDate, List<Reservation> reservations, Partner partner) {
        this.customerName = customerName;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.cardExpirationDate = cardExpirationDate;
        this.reservations = reservations;
        this.partner = partner;
    }

    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * @return the cardExpirationDate
     */
    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    /**
     * @param cardExpirationDate the cardExpirationDate to set
     */
    public void setCardExpirationDate(String cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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
    
}
