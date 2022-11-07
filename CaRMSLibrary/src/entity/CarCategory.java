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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jester
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carCategoryId;
    @Column(nullable = false, length = 32, unique = true)
    private String carCategoryName;

    @OneToMany(mappedBy = "carCategory")
    private List<CarModel> carModels;
    @OneToMany(mappedBy = "carCategory")
    private List<RentalRate> rentalRates;
    
    public CarCategory() {
        this.carModels = new ArrayList<>();
        this.rentalRates = new ArrayList<>();
    }

    public CarCategory(String carCategoryName, List<CarModel> carModels, List<RentalRate> rentalRates) {
        this.carCategoryName = carCategoryName;
        this.carModels = carModels;
        this.rentalRates = rentalRates;
    }

    public CarCategory(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }
    
    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    public String getCarCategoryName() {
        return carCategoryName;
    }

    public void setCarCategoryName(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }
    
    @XmlTransient
    public List<CarModel> getModels() {
        return carModels;
    }
    
    public void setModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }
    
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCateogryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + carCategoryId + " ]";
    }
    
}
