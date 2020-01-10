package com.empire.vince.vokers.carscanner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.model.Offense;

import java.text.ParseException;

//import butterknife.ButterKnife;
//import butterknife.InjectView;

public class ShowOffenseActivity extends Activity implements View.OnClickListener{
    private SQLiteHandler db;

    //@InjectView(R.id.oSettleOffButtonOk)
    Button mButtonOk;
    //@InjectView(R.id.oButtonCancel)
    Button mButtonCancel;
    //@InjectView(R.id.oPlate)
    TextView mPlateNo;
    //@InjectView(R.id.oName)
    TextView mOffenseName;
    //@InjectView(R.id.oDesc)
    TextView mOffenseDesc;
    //@InjectView(R.id.oAddedBy)
    TextView mAddedBy;
    //@InjectView(R.id.oLoc)
    TextView mOffenseLocation;
    //@InjectView(R.id.oFineDueDate)
    TextView mOffenseFineDueDate;
    //@InjectView(R.id.oTime)
    TextView mOffenseTime;
    //@InjectView(R.id.oFine)
    TextView mOffenseFine;
    //@InjectView(R.id.oNew)
            Button mBtnNew;
    private String mPassedPlateNumber;
    private Context context;
    private Offense offense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offense);

        //ButterKnife.inject(this);
        context = this;

        mButtonOk = (Button) findViewById(R.id.oSettleOffButtonOk);
        mButtonCancel = (Button) findViewById(R.id.oButtonCancel);
        mPlateNo = (TextView) findViewById(R.id.oPlate);
        mOffenseName = (TextView) findViewById(R.id.oName);
        mOffenseDesc = (TextView) findViewById(R.id.oDesc);
        mAddedBy = (TextView) findViewById(R.id.oAddedBy);
        mOffenseLocation = (TextView) findViewById(R.id.oLoc);
        mOffenseFineDueDate = (TextView) findViewById(R.id.oFineDueDate);
        mOffenseTime = (TextView) findViewById(R.id.oTime);
        mOffenseFine = (TextView) findViewById(R.id.oFine);
        mBtnNew = (Button) findViewById(R.id.oNew);


        mButtonOk.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
        mBtnNew.setOnClickListener(this);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        mPassedPlateNumber = b.getString("no_plate");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        offense = db.getOffenseInfo(mPassedPlateNumber);
        if(offense == null){
            Toast.makeText(getApplication(),"No offense available",Toast.LENGTH_SHORT).show();
        }else {

            mPlateNo.setText(offense.getNumberPlate()!=null?offense.getOffense():"Unknown");


            mOffenseDesc.setText(offense.getOffenseDetails()!=null?offense.getOffenseDetails():"Unknown");

            mOffenseTime.setText(offense.getOffenseDateTime()!=null?offense.getOffenseDateTime():"Unknown");

            mOffenseLocation.setText(offense.getOffenseLocation()!=null?offense.getOffenseLocation():"Unknown");

            mOffenseFine.setText(offense.getOffenseFine()!=null?"$ " + offense.getOffenseFine():"Unknown");

            mOffenseFineDueDate.setText(offense.getFineDueDate()!=null?offense.getFineDueDate():"Unknown");

            mAddedBy.setText(offense.getAddedBy()!=null?offense.getAddedBy():"Unknown");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oButtonCancel:
                CarUtil carUtil = db.getCarDetailsByNoPlate(mPassedPlateNumber);
                Intent intentBack = new Intent(v.getContext(),VehicleButtonMenuActivity.class);
                Bundle bundleItems = new Bundle();
                bundleItems.putString("hex_val",carUtil.getCarHex());
                intentBack.putExtras(bundleItems);
                startActivity(intentBack);
                finish();
                break;
            case R.id.oSettleOffButtonOk:
                //Dialog here
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                TextView displayText = (TextView) promptsView
                        .findViewById(R.id.promptTextView);
                displayText.setText("Amount Paid($):");


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String input = userInput.getText().toString();
                                        if(!input.equals("") ){
                                            try {
                                                Double amount = Double.parseDouble(offense.getOffenseFine());
                                                amount -= Double.parseDouble(input);

                                                db.updateOffenseDetails(amount+"",offense.getNumberPlate());
                                                Toast.makeText(context, "Account successful processed.", Toast.LENGTH_LONG).show();

                                                Intent intent = new Intent(context, ShowOffenseActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("no_plate", offense.getNumberPlate());
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();

                                            }catch (NumberFormatException e){
                                                e.printStackTrace();
                                                Toast.makeText(context, "Error occurred. Saved for review.", Toast.LENGTH_LONG).show();
                                            }

                                        }else {
                                            Toast.makeText(context, "Access denied", Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            case R.id.oNew:
                Intent intent = new Intent(v.getContext(), OffensesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("no_plate", mPassedPlateNumber);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }

    }
}
