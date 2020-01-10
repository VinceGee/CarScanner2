package com.empire.vince.vokers.carscanner.search;

import java.math.BigDecimal;

/**
 * Created by VinceGee on 08/19/2016.
 */
public class Car {
    private String id, car_name, car_driver, numberplate, hexval,status;

    public boolean selected;

    public Car() {

    }

    public Car(String id, String car_name, String car_driver, String numberplate, String hexval, String status) {
        this.id = id;
        this.car_name = car_name;
        this.car_driver = car_driver;
        this.numberplate = numberplate;
        this.hexval  = hexval;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHexval() {
        return hexval;
    }

    public void setHexval(String hexval) {
        this.hexval = hexval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_driver() {
        return car_driver;
    }

    public void setCar_driver(String car_driver) {
        this.car_driver = car_driver;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
