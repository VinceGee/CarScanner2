package com.empire.vince.vokers.carscanner.model;

/**
 * Created by Anotida on 1/28/2017.
 */

public class Offense {
    private String numberPlate;
    private String driverClass;
    private String offense;
    private String offenseDetails;
    private String offenseDateTime;
    private String offenseLocation;
    private String offenseFine;
    private String fineDueDate;
    private String addedBy;

    public Offense(String numberPlate, String driverClass, String offense, String offenseDetails, String offenseDateTime, String offenseLocation, String offenseFine, String fineDueDate, String addedBy) {
        this.numberPlate = numberPlate;
        this.driverClass = driverClass;
        this.offense = offense;
        this.offenseDetails = offenseDetails;
        this.offenseDateTime = offenseDateTime;
        this.offenseLocation = offenseLocation;
        this.offenseFine = offenseFine;
        this.fineDueDate = fineDueDate;
        this.addedBy = addedBy;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getOffense() {
        return offense;
    }

    public String getOffenseDetails() {
        return offenseDetails;
    }

    public String getOffenseDateTime() {
        return offenseDateTime;
    }

    public String getOffenseLocation() {
        return offenseLocation;
    }

    public String getOffenseFine() {
        return offenseFine;
    }

    public String getFineDueDate() {
        return fineDueDate;
    }

    public String getAddedBy() {
        return addedBy;
    }
}
