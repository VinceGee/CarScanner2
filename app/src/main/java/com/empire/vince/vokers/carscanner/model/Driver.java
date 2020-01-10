package com.empire.vince.vokers.carscanner.model;

/**
 * Created by maditsha on 9/7/2016.
 */

public class Driver {
    private String name;
    private String no_plate;
    private String lic_no;
    private String address;
    private String nat_id;
    private String cell_no;

    public Driver(String guard, String no_plate, String date, String status, String location, String cell_no) {
        this.name = guard;
        this.no_plate = no_plate;
        this.lic_no = date;
        this.address = status;
        this.nat_id = location;
        this.cell_no = cell_no;
    }

    public String getCell_no() {
        return cell_no;
    }

    public String getName() {
        return name;
    }

    public String getNo_plate() {
        return no_plate;
    }

    public String getLic_no() {
        return lic_no;
    }

    public String getAddress() {
        return address;
    }

    public String getNat_id() {
        return nat_id;
    }
}
