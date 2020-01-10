package com.empire.vince.vokers.carscanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;
import com.empire.vince.vokers.carscanner.utilities.VokersEditText;

import java.text.DateFormat;
import java.util.Date;

public class FailedCarDetailsActivity extends AppCompatActivity {
private VokersEditText mNoPlate;
private VokersEditText mStatus;
private VokersEditText mGuardName;
    private LinearLayout mLinearLayout;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_car_details);

        mNoPlate = (VokersEditText)findViewById(R.id.setting_no_plate);
        mStatus = (VokersEditText)findViewById(R.id.status_text);
        mGuardName = (VokersEditText)findViewById(R.id.setting_guard_name);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String fullname = preferences.getString("name", "");

        mGuardName.setText(fullname);

        //Getting today's date
        //Getting today's date
        DateFormat format = DateFormat.getDateTimeInstance();
        // System.out.println(format.format(date));
        final String today = format.format(new Date());

mLinearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String number_plate = mNoPlate.getText().toString();
        String status = mStatus.getText().toString();

       // db.addDetails(fullname, number_plate, today, status, getLocation().toUpperCase());
        loadOutClass();
    }
});


    }
    public String getLocation(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loc = preferences.getString("LOCATION", "");
        return loc==null? AppConfig.LOCATION:loc;
    }

    private void loadOutClass(){
        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        //finish();
        finish();
    }
}
