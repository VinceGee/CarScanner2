/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.empire.vince.vokers.carscanner.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.model.Driver;
import com.empire.vince.vokers.carscanner.model.Offense;
import com.empire.vince.vokers.carscanner.model.User;

import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "vanch";

    // Login table name
    private static final String TABLE_USER = "user";


    // DETAILS table name
    private static final String TABLE_DRIVERS_DETAILS = "driver_table";

    // DETAILS table name
    private static final String TABLE_CAR_STATIC_INFO = "car_table";
    private static final String TABLE_CAR_UPDATE_INFO = "car_table_info";

    private static final String TABLE_OFFENSE_STATIC_INFO = "offense_table";
    private static final String TABLE_OFFENSE_UPDATE_INFO = "offense_table_info";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    //private static final String KEY_CREATED_AT = "created_at";

    // Login Table Columns names
    private static final String D_ID = "col_id";
    private static final String D_NAME = "name";
    private static final String D_NO_PLATE = "numberplate";
    private static final String D_LICENCE = "licence";
    private static final String D_ADDRESS = "address";
    private static final String D_NAT_ID = "nat_id";
    private static final String D_DRIVER_CELL = "phone";

    //Storing static car information
    private static final String CAR_ID = "id";
    private static final String CAR_MAKE = "make";
    private static final String CAR_MODEL = "model";
    private static final String CAR_NO_PLATE = "no_plate";
    private static final String CAR_HEX = "hex";
    private static final String CAR_COLOR = "color";
    private static final String CAR_YEAR = "year";
    private static final String CAR_TRACK_NO = "track";
    private static final String CAR_CELL_NO = "cellno";
    private static final String CAR_SIM_NO = "simno";
    private static final String CAR_ROUTE = "route";

    //Storing static offense information

    private static final String O_ID = "id";
    private static final String O_NO_PLATE = "no_plate";
    private static final String O_CLASS = "class";
    private static final String O_OFFENSE = "offense";
    private static final String O_DETAILS = "details";
    private static final String O_DATE_TIME = "date";
    private static final String O_LOCATION = "loc";
    private static final String O_FINE = "fine";
    private static final String O_FINE_DUE_DATE = "fine_due_date";
    private static final String O_ADDED_BY = "added_by";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT" + ")";

        String CREATE_DRIVER_TABLE = "CREATE TABLE " + TABLE_DRIVERS_DETAILS + "("
                + D_ID + " INTEGER PRIMARY KEY,"
                + D_NAME + " TEXT,"
                + D_LICENCE + " TEXT,"
                + D_NO_PLATE + " TEXT,"
                + D_ADDRESS + " TEXT,"
                + D_NAT_ID + " TEXT,"
                + D_DRIVER_CELL + " TEXT" + ")";

        String CREATE_CAR_TABLE = "CREATE TABLE " + TABLE_CAR_STATIC_INFO + "("
                + CAR_ID + " INTEGER PRIMARY KEY,"
                + CAR_MAKE + " TEXT,"
                + CAR_MODEL + " TEXT,"
                + CAR_NO_PLATE + " TEXT,"
                + CAR_HEX + " TEXT,"
                + CAR_COLOR + " TEXT,"
                + CAR_YEAR + " TEXT,"
                + CAR_TRACK_NO + " TEXT,"
                + CAR_CELL_NO + " TEXT,"
                + CAR_SIM_NO + " TEXT,"
                + CAR_ROUTE + " TEXT" + ")";

        String CREATE_UPDATE_CAR_TABLE = "CREATE TABLE " + TABLE_CAR_UPDATE_INFO + "("
                + CAR_ID + " INTEGER PRIMARY KEY,"
                + CAR_MAKE + " TEXT,"
                + CAR_MODEL + " TEXT,"
                + CAR_NO_PLATE + " TEXT,"
                + CAR_HEX + " TEXT,"
                + CAR_COLOR + " TEXT,"
                + CAR_YEAR + " TEXT,"
                + CAR_TRACK_NO + " TEXT,"
                + CAR_CELL_NO + " TEXT,"
                + CAR_SIM_NO + " TEXT,"
                + CAR_ROUTE + " TEXT" + ")";

        String CREATE_OFFENSE_TABLE = "CREATE TABLE " + TABLE_OFFENSE_STATIC_INFO + "("
                + O_ID + " INTEGER PRIMARY KEY,"
                + O_NO_PLATE + " TEXT,"
                + O_CLASS + " TEXT,"
                + O_OFFENSE + " TEXT,"
                + O_DETAILS + " TEXT,"
                + O_DATE_TIME + " TEXT,"
                + O_LOCATION + " TEXT,"
                + O_FINE + " TEXT,"
                + O_FINE_DUE_DATE + " TEXT,"
                + O_ADDED_BY + " TEXT" + ")";

        String CREATE_OFFENSE_UPDATE_TABLE = "CREATE TABLE " + TABLE_OFFENSE_UPDATE_INFO + "("
                + O_ID + " INTEGER PRIMARY KEY,"
                + O_NO_PLATE + " TEXT,"
                + O_CLASS + " TEXT,"
                + O_OFFENSE + " TEXT,"
                + O_DETAILS + " TEXT,"
                + O_DATE_TIME + " TEXT,"
                + O_LOCATION + " TEXT,"
                + O_FINE + " TEXT,"
                + O_FINE_DUE_DATE + " TEXT,"
                + O_ADDED_BY + " TEXT" + ")";


        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_CAR_TABLE);
        db.execSQL(CREATE_OFFENSE_TABLE);
        db.execSQL(CREATE_UPDATE_CAR_TABLE);
        db.execSQL(CREATE_OFFENSE_UPDATE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE_USER IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE_DRIVERS_DETAILS IF EXISTS " + TABLE_DRIVERS_DETAILS);
        db.execSQL("DROP TABLE_CARS_DETAILS IF EXISTS " + TABLE_CAR_STATIC_INFO);
        db.execSQL("DROP TABLE_OFFENSES_DETAILS IF EXISTS " + TABLE_OFFENSE_STATIC_INFO);
        db.execSQL("DROP TABLE_OFFENSES_INFO_DETAILS IF EXISTS " + TABLE_OFFENSE_UPDATE_INFO);
        db.execSQL("DROP TABLE_CAR_INFO_DETAILS IF EXISTS " + TABLE_CAR_UPDATE_INFO);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PASSWORD, password); // Email
        //values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing driver details in database
     * */
    public void addDriversStaticDetails(String no_plate, String name, String licence, String drivers_add, String nat_id, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(D_NO_PLATE, no_plate);
        values.put(D_NAME, name);
        values.put(D_LICENCE, licence); //
        values.put(D_ADDRESS, drivers_add);
        values.put(D_NAT_ID, nat_id);
        values.put(D_DRIVER_CELL, phone);

        // Inserting Row
        long idy = db.insert(TABLE_DRIVERS_DETAILS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    /**
     * Storing car static details in database
     * */
    //carMake,carModel,numberPleti,hexValue,carColor,carYear,trackNo,cellNum,simNum,route
    public void addCarStaticDetails(String make, String model, String no_plate, String hex, String color, String year, String trackNo, String cellNum, String simNum, String route) {
        SQLiteDatabase db = this.getWritableDatabase();
/*car_name,car_driver,number_plate,hexval,status,guard,location*/
        ContentValues values = new ContentValues();
        //values.put(CAR_ID, id);
        values.put(CAR_MAKE, make);
        values.put(CAR_MODEL, model); //
        values.put(CAR_NO_PLATE, no_plate);
        values.put(CAR_HEX, hex);
        values.put(CAR_COLOR, color);
        values.put(CAR_SIM_NO, year);
        values.put(CAR_ROUTE, trackNo);
        values.put(CAR_CELL_NO, cellNum);
        values.put(CAR_SIM_NO, simNum);
        values.put(CAR_ROUTE, route);

        // Inserting Row
        long idy = db.insert(TABLE_CAR_STATIC_INFO, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    //carMake,carModel,numberPleti,hexValue,carColor,carYear,trackNo,cellNum,simNum,route
    public void addCarUpdatesInfoDetails(String make, String model, String no_plate, String hex, String color, String year, String trackNo, String cellNum, String simNum, String route) {
        SQLiteDatabase db = this.getWritableDatabase();
/*car_name,car_driver,number_plate,hexval,status,guard,location*/
        ContentValues values = new ContentValues();
        //values.put(CAR_ID, id);
        values.put(CAR_MAKE, make);
        values.put(CAR_MODEL, model); //
        values.put(CAR_NO_PLATE, no_plate);
        values.put(CAR_HEX, hex);
        values.put(CAR_COLOR, color);
        values.put(CAR_SIM_NO, year);
        values.put(CAR_ROUTE, trackNo);
        values.put(CAR_CELL_NO, cellNum);
        values.put(CAR_SIM_NO, simNum);
        values.put(CAR_ROUTE, route);

        // Inserting Row
        long idy = db.insert(TABLE_CAR_UPDATE_INFO, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    /**
     * Storing offense static details in database
     * */
//	numberPleti, oClass, offense, offenseDetails, offenseDateTime, offenseLocation, offenseFine, fineDueDate, addedBy
    public void addOffensesStaticDetails(String no_plate, String oclass, String offense, String details, String time, String loc, String fine, String due, String by) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(O_NO_PLATE, no_plate);
        values.put(O_CLASS, oclass); //
        values.put(O_OFFENSE, offense);
        values.put(O_DETAILS, details);
        values.put(O_DATE_TIME, time);
        values.put(O_LOCATION, loc);
        values.put(O_FINE, fine);
        values.put(O_FINE_DUE_DATE, due);
        values.put(O_ADDED_BY, by);

        // Inserting Row
        long idy = db.insert(TABLE_OFFENSE_STATIC_INFO, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    public void addOffensesUpdatesInfoDetails(String no_plate, String oclass, String offense, String details, String time, String loc, String fine, String due, String by) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(O_NO_PLATE, no_plate);
        values.put(O_CLASS, oclass); //
        values.put(O_OFFENSE, offense);
        values.put(O_DETAILS, details);
        values.put(O_DATE_TIME, time);
        values.put(O_LOCATION, loc);
        values.put(O_FINE, fine);
        values.put(O_FINE_DUE_DATE, due);
        values.put(O_ADDED_BY, by);

        // Inserting Row
        long idy = db.insert(TABLE_OFFENSE_UPDATE_INFO, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    /**
     * Getting user data from database
     * */
    public ArrayList<User> getUserDetails() {
        ArrayList<User> user = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                user.add(new User(cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user data by email and password from database
     * */
    public User getUserDetailsByEmailAndPassword(String email, String password) {
        User user = null;
        String selectQuery = "SELECT  * FROM "+ TABLE_USER +" WHERE "+ KEY_EMAIL +"='"+ email +"' AND "+ KEY_PASSWORD +"='"+ password +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user

        return user;
    }

    /**
     * Getting details data from database
     * */
    //(numberPleti, name, licence, drivers_add, nat_id, phone
    public ArrayList<Driver> getDriversInfo() {
        String selectQuery = "SELECT  * FROM " + TABLE_DRIVERS_DETAILS;
        ArrayList<Driver> drivers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                drivers.add(new Driver(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(5)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return drivers;
    }

    /**
     * Getting driver data by no plate from database
     * */
    public Driver getDriverInfo(String noPlate) {
        Driver driver = null;
        String selectQuery = "SELECT  * FROM "+ TABLE_DRIVERS_DETAILS +" WHERE "+ D_NO_PLATE +"='"+ noPlate +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                driver = new Driver(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getString(5), cursor.getString(6));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user

        return driver;
    }

    /**
     * Getting cars static details data from database
     * */
    public ArrayList<CarUtil> getCarStaticDetails() {
        ArrayList<CarUtil> carUtil = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CAR_STATIC_INFO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                carUtil.add(new CarUtil(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carUtil;
    }

    public ArrayList<CarUtil> getCarUpdatesInfoDetails() {
        ArrayList<CarUtil> carUtil = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CAR_UPDATE_INFO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                carUtil.add(new CarUtil(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carUtil;
    }

    /**
     * Getting car details by hex data from database
     * */
    public CarUtil getCarDetailsByHexVal(String hex) {
        CarUtil carUtil = null;
        String selectQuery = "SELECT  * FROM " + TABLE_CAR_STATIC_INFO +" WHERE "+ CAR_HEX +" = '"+ hex +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


               carUtil = new CarUtil(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carUtil;
    }

    /**
     * Getting car details by hex data from database
     * */
    public CarUtil getCarDetailsByNoPlate(String no_plate) {
        CarUtil carUtil = null;
        String selectQuery = "SELECT  * FROM " + TABLE_CAR_STATIC_INFO +" WHERE "+ CAR_NO_PLATE +" = '"+ no_plate +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                carUtil = new CarUtil(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carUtil;
    }

    /**
     * Getting offenses static details data from database
     * */
    public ArrayList<Offense> getOffensesStaticDetails() {
        ArrayList<Offense> offenses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_OFFENSE_STATIC_INFO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                offenses.add(new Offense(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offenses;
    }

    public ArrayList<Offense> getOffensesUpdateInfoDetails() {
        ArrayList<Offense> offenses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_OFFENSE_UPDATE_INFO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                offenses.add(new Offense(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9)));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offenses;
    }

    /**
     * Getting driver data by no plate from database
     * */
    public Offense getOffenseInfo(String noPlate) {
        Offense offense = null;
        String selectQuery = "SELECT  * FROM "+ TABLE_OFFENSE_STATIC_INFO +" WHERE "+ O_NO_PLATE +"='"+ noPlate +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {


                offense = new Offense(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7), cursor.getString(8), cursor.getString(9));

                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user

        return offense;
    }

    /**
     * Edit the car details with the correct status
     * */

    public void updateOffenseDetails(String amount, String no_plate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + TABLE_OFFENSE_STATIC_INFO + " SET " + O_FINE + " = '" + amount + "' WHERE " + O_NO_PLATE + " = '" + no_plate + "'";

        db.execSQL(strSQL);
    }

    /**
     * Edit the car details with the correct status
     * */

    public void updateCarDetails(String status, String no_plate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + TABLE_CAR_STATIC_INFO + " SET " + CAR_COLOR + " = '" + status + "' WHERE " + CAR_NO_PLATE + " = '" + no_plate + "'";

        db.execSQL(strSQL);
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteDBInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_DRIVERS_DETAILS, null, null);
        db.delete(TABLE_CAR_STATIC_INFO, null, null);
        db.delete(TABLE_OFFENSE_STATIC_INFO, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public boolean isDriverContentEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean empty = true;
        Cursor cur;
        try{
            cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DRIVERS_DETAILS, null);
        }catch(Exception e){
            e.printStackTrace();
            return empty;
        }
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

    public boolean isOffenseContentEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean empty = true;
        Cursor cur;
        try{
            cur  = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_OFFENSE_STATIC_INFO, null);
        }catch(Exception e){
            e.printStackTrace();
            return empty;
        }
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

    public boolean isUserContentEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean empty = true;
        Cursor cur;
        try{
            cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER, null);
        }catch(Exception e){
            e.printStackTrace();
            return empty;
        }
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

    public boolean isCarContentEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean empty = true;
        Cursor cur;
        try{
            cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CAR_STATIC_INFO, null);
        }catch(Exception e){
            e.printStackTrace();
            return empty;
        }
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

}
