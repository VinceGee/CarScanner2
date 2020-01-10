package com.empire.vince.vokers.carscanner.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.activities.MainMenuActivity;
import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.login.SessionManager;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;
import com.empire.vince.vokers.carscanner.utilities.VokersEditText;

public class AdminProperties extends AppCompatActivity {

    private static VokersEditText mSetIp;
    private static VokersEditText mSetLoc;
    private Button mResetButton;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSetIp = (VokersEditText) findViewById(R.id.setting_ip);
        mSetLoc = (VokersEditText) findViewById(R.id.setting_location);
        mResetButton = (Button) findViewById(R.id.resetDB);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        mSetIp.setText(getIpAddr());


        mSetLoc.setText(getLocation());


        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteDBInfo();

                setIp_addr(mSetIp.getText().toString());
                //AppConfig.setIp_addr();
                // AppConfig.IP_ADDRESS_GET = mSetIp.getText().toString();

                setILocation(mSetLoc.getText().toString());
               // AppConfig.setILocation();

                getConfig();

                Toast.makeText(getApplicationContext(), "Reset Successful", Toast.LENGTH_LONG).show();


            }

            private void getConfig() {
                AppConfig.BASE_URL = "http://" + getIpAddr() + "/vanch";
                // Server user login url
                AppConfig.URL_LOGIN = AppConfig.BASE_URL + "/login.php";

                // Server user register url
                AppConfig.URL_REGISTER = AppConfig.BASE_URL + "/register.php";

                AppConfig.URL_SEARCH = AppConfig.BASE_URL + "/search.php";

                AppConfig.URL_DETAILS = AppConfig.BASE_URL + "/getmotadzese.php"; //http://localhost/vanch/getmotadzese.php

                AppConfig.URL_USERS = AppConfig.BASE_URL + "/getallusers.php";//http://localhost/vanch/getallusers.php

                //public static SharedPreferences preferences ;

                // Server user register url
                AppConfig.URL_WIN = AppConfig.BASE_URL + "/checkingin.php";
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }





    public void setIp_addr(String ip){
        AppConfig.IP_ADDRESS = ip;
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());  //storing details in the shared preferences
        SharedPreferences.Editor editor= pref.edit();

        editor.putString("IP_ADDRESS", ip);
        editor.apply();


    }

    public void setILocation(String loc){
        AppConfig.LOCATION = loc;
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());  //storing details in the shared preferences
        SharedPreferences.Editor editor= pref.edit();

        editor.putString("LOCATION", loc);
        editor.apply();


    }


    public  String getIpAddr(){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip_addr = pref.getString("IP_ADDRESS", "");

        return ip_addr==null?AppConfig.IP_ADDRESS:ip_addr;
    }

    public String getLocation(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loc = preferences.getString("LOCATION", "");
        return loc==null?AppConfig.LOCATION:loc;
    }

}
