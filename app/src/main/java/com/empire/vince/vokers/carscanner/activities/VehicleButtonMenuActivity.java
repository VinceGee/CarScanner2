package com.empire.vince.vokers.carscanner.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.model.Driver;
import com.empire.vince.vokers.carscanner.model.Offense;

public class VehicleButtonMenuActivity extends Activity implements View.OnClickListener{
    private SQLiteHandler db;
    private Button mButtonNoPlate;
    private Button mButtonName;
    private Button mButtonRoute;
    private Button mButtonGps;
    private Button mButtonOffense;
    private Button mButtonNext;
    private CarUtil mCarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_button_menu);

        mButtonNoPlate = (Button)findViewById(R.id.bnoplate);
        mButtonNoPlate.setOnClickListener(this);
        mButtonName = (Button)findViewById(R.id.bdrivername);
        mButtonName.setOnClickListener(this);
        mButtonRoute = (Button) findViewById(R.id.broute);
        mButtonRoute.setOnClickListener(this);
        mButtonGps = (Button) findViewById(R.id.bgpsstatus);
        mButtonGps.setOnClickListener(this);
        mButtonOffense = (Button)findViewById(R.id.boffenses);
        mButtonOffense.setOnClickListener(this);
        mButtonNext = (Button)findViewById(R.id.bnext);
        mButtonNext.setOnClickListener(this);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String hex_val = b.getString("hex_val");

        mCarDetails = db.getCarDetailsByHexVal(hex_val);


        if(mCarDetails==null){
            //Toast.makeText(getApplicationContext(),"The vehicle is not registered",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("true", "true");
            intent.putExtras(bundle);
            startActivity(intent);
            //finish();
            finish();
        }else {
            setButtonFields(mCarDetails);
        }




    }

    private void setButtonFields(CarUtil carDetails){
        Driver driver = db.getDriverInfo(carDetails.getCarNoPlate());
        Offense offense = db.getOffenseInfo(carDetails.getCarNoPlate());
        mButtonNoPlate.setText(carDetails.getCarNoPlate());
        if(driver.getName() != null) {
            mButtonName.setText(driver.getName());
        }else {
            mButtonName.setText("UNKNOWN");
        }
        if(carDetails.getRoute() != null) {
            mButtonRoute.setText(carDetails.getRoute());
        }else {
            mButtonName.setText("UNKNOWN");
        }
        mButtonGps.setText("GPS TAMPERED");
        if(offense.getOffense() != null) {
            mButtonOffense.setText(offense.getOffense());
        }else {
            mButtonOffense.setText("OFFENSE");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bnoplate:
                //Toast.makeText(v.getContext(),"No plate",Toast.LENGTH_SHORT).show();
                CarUtil car = db.getCarDetailsByHexVal(mCarDetails.getCarHex());
                if(car == null){
                    Toast.makeText(getApplication(),"No car info available",Toast.LENGTH_SHORT).show();
                    break;
                }
                // custom dialog
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_plate_info);
                dialog.setTitle("Vehicle Information");

                // set the custom dialog components - text, image and button
                TextView plate = (TextView) dialog.findViewById(R.id.pTvPlate);
                plate.setText(car.getCarNoPlate());

                TextView model = (TextView) dialog.findViewById(R.id.pTvModel);
                model.setText(car.getCarModel());

                TextView make = (TextView) dialog.findViewById(R.id.pTvMake);
                make.setText(car.getCarMake());

                TextView color = (TextView) dialog.findViewById(R.id.pTvColor);
                color.setText(car.getCarColor());

                TextView route = (TextView) dialog.findViewById(R.id.pTvRoute);
                route.setText(car.getRoute());

                TextView year = (TextView) dialog.findViewById(R.id.pTvYear);
                year.setText(car.getCarYear());

                Button dialogButton = (Button) dialog.findViewById(R.id.pButtonOk);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


                // do your code
                break;

            case R.id.bdrivername:
                Driver driverInfo = db.getDriverInfo(mCarDetails.getCarNoPlate());
                if(driverInfo == null){
                    Toast.makeText(getApplication(),"No driver info available",Toast.LENGTH_SHORT).show();
                    break;
                }
                // custom dialog
                final Dialog dDialog = new Dialog(v.getContext());
                dDialog.setContentView(R.layout.dialog_driver_info);
                dDialog.setTitle("Driver Information");

                // set the custom dialog components - text, image and button
                TextView name = (TextView) dDialog.findViewById(R.id.dTvName);
                name.setText(driverInfo.getName());

                TextView lic = (TextView) dDialog.findViewById(R.id.dTvLicence);
                lic.setText(driverInfo.getLic_no());

                TextView addr = (TextView) dDialog.findViewById(R.id.dTvAddress);
                addr.setText(driverInfo.getAddress());

                TextView id = (TextView) dDialog.findViewById(R.id.dTvId);
                id.setText(driverInfo.getNat_id());


                Button driverButton = (Button) dDialog.findViewById(R.id.dButtonOk);
                // if button is clicked, close the custom dialog
                driverButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismiss();
                    }
                });

                dDialog.show();
                // do your code
                break;

            case R.id.boffenses:
                //Toast.makeText(v.getContext(),"Offenses",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ShowOffenseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("no_plate", mCarDetails.getCarNoPlate());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();


                // do your code
                break;

            case R.id.bnext:
                db.addCarUpdatesInfoDetails(mCarDetails.getCarMake(),mCarDetails.getCarModel(),mCarDetails.getCarNoPlate(),mCarDetails.getCarHex(),mCarDetails.getCarColor(),mCarDetails.getCarYear(),mCarDetails.getCarTrackNo(),mCarDetails.getCellNum(),mCarDetails.getSimNum(),mCarDetails.getRoute());
                Intent intentBack = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentBack);
                //finish();
                finish();
                // do your code
                break;

            default:
                break;
        }
    }
}
