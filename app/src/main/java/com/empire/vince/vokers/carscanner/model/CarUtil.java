package com.empire.vince.vokers.carscanner.model;

/**
 * Created by maditsha on 9/7/2016.
 */
public class CarUtil {
    private String id;
    private String carMake;
    private String carModel;
    private String carNoPlate;
    private String carHex;
    private String carColor;
    private String carYear;
    private String carTrackNo;
    private String cellNum;
    private String simNum;
    private String route;

    public CarUtil() {
    }

    public CarUtil(String carMake, String carModel, String carNoPlate, String carHex, String carColor, String carYear, String carTrackNo) {
       // this.id = id;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carNoPlate = carNoPlate;
        this.carHex = carHex;
        this.carColor = carColor;
        this.carYear = carYear;
        this.carTrackNo = carTrackNo;
    }

    public CarUtil(String carMake, String carModel, String carNoPlate, String carHex, String carColor, String carYear, String carTrackNo, String cellNum, String simNum, String route) {
        this.carMake = carMake;
        this.carModel = carModel;
        this.carNoPlate = carNoPlate;
        this.carHex = carHex;
        this.carColor = carColor;
        this.carYear = carYear;
        this.carTrackNo = carTrackNo;
        this.cellNum = cellNum;
        this.simNum = simNum;
        this.route = route;
    }

    public CarUtil(String carMake, String carNoPlate, String carHex, String route) {
        this.carMake = carMake;
        this.carNoPlate = carNoPlate;
        this.carHex = carHex;
        this.route = route;
    }

    public String getId() {
        return id;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarNoPlate() {
        return carNoPlate;
    }

    public String getCarHex() {
        return carHex;
    }

    public String getCarColor() {
        return carColor;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getCarTrackNo() {
        return carTrackNo;
    }

    public String getCellNum() {
        return cellNum;
    }

    public String getSimNum() {
        return simNum;
    }

    public String getRoute() {
        return route;
    }
}
