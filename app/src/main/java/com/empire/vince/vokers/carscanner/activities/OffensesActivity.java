package com.empire.vince.vokers.carscanner.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.model.Offense;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;

import java.util.Locale;

//import butterknife.ButterKnife;
//import butterknife.InjectView;

public class OffensesActivity extends Activity implements View.OnClickListener{

    //@InjectView(R.id.cName)
    EditText mOffenseName;
    //@InjectView(R.id.cAmount)
    EditText mOffenseAmount;
    //@InjectView(R.id.cDesc)
    EditText mOffenseDesc;
    //@InjectView(R.id.cBtnOkay)
    Button mBtnAppy;
    //@InjectView(R.id.cBtnCancel)
    Button mBtnCancel;
    private SQLiteHandler db;
    Offense mOffenseDetails;
    CarUtil mCarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offenses);
        //ButterKnife.inject(this);

        mOffenseName = (EditText) findViewById(R.id.cName);
        mOffenseAmount = (EditText) findViewById(R.id.cAmount);
        mOffenseDesc = (EditText) findViewById(R.id.cDesc);
        mBtnAppy = (Button) findViewById(R.id.cBtnOkay);
        mBtnCancel = (Button) findViewById(R.id.cBtnCancel);

        mBtnAppy.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String no_plate = b.getString("no_plate");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        mOffenseDetails = db.getOffenseInfo(no_plate);
        mCarDetails = db.getCarDetailsByNoPlate(no_plate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cBtnCancel:
                Intent intent = new Intent(v.getContext(), ShowOffenseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("no_plate", mCarDetails.getCarNoPlate());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.cBtnOkay:
                String name = mOffenseName.getText().toString();
                String amount = mOffenseAmount.getText().toString();//0713668062
                String desc = mOffenseDesc.getText().toString();

                if(name.equals("") || amount.equals("") || desc.equals("")){
                    Toast.makeText(getApplicationContext(),"Fill all the fields details.",Toast.LENGTH_LONG).show();
                    break;
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String addedBy = preferences.getString("name", "");

                String location = AppConfig.LOCATION;
                db.addOffensesUpdatesInfoDetails(mOffenseDetails.getNumberPlate(),mOffenseDetails.getDriverClass(),name,desc,mOffenseDetails.getOffenseDateTime(),location,amount,mOffenseDetails.getFineDueDate(),addedBy);

                Intent intent1 = new Intent(getApplicationContext(),VehicleButtonMenuActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("hex_val", mCarDetails.getCarHex());
                intent1.putExtras(bundle1);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }
}
