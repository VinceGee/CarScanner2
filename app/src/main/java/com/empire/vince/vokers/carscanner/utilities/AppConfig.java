package com.empire.vince.vokers.carscanner.utilities;

import android.content.Context;

import com.empire.vince.vokers.carscanner.model.Driver;

import java.util.ArrayList;

/**
 * Created by VinceGee on 05/30/2016.
 */
public class AppConfig {

    // variable to hold context
    private static Context context;

//save the context recievied via constructor in a local variable

    public AppConfig(Context context){
        this.context=context;
    }

    public static ArrayList<Driver> CAR_HISTORY;

    public static String LOCATION ="HARARE";

    public static String IP_ADDRESS = "192.168.10.105";
    //public static String IP_ADDRESS = "192.168.1.46";
    //public static String IP_ADDRESS = "192.168.43.34";


    public static  String BASE_URL = "http://"+IP_ADDRESS+"/vanchscanner";
    // Server user login url
    public static String URL_LOGIN = BASE_URL + "/login.php";

    // Server user register url
    public static String URL_REGISTER = BASE_URL + "/register.php";

    public static  String URL_SEARCH = BASE_URL + "/search.php";

    public static  String URL_DETAILS = BASE_URL + "/mobile/getmotadzese.php"; //http://localhost/vanch/mobile/getmotadzese.php
    public static  String URL_OFFENSES = BASE_URL + "/mobile/getmaoffenses.php"; //http://localhost/vanch/mobile/getmaoffenses.php
    public static  String URL_DRIVERS = BASE_URL + "/mobile/getalldrivers.php"; //http://localhost/vanch/mobile/getalldrivers.php
    public static  String URL_USERS = BASE_URL + "/mobile/getallusers.php";//http://localhost/vanch/mobile/getallusers.php

    //public static SharedPreferences preferences ;

    // Server user register url
    public static  String URL_WIN = BASE_URL + "/checkingin.php";





}
