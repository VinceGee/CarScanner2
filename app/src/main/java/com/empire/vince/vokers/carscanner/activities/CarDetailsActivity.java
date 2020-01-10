package com.empire.vince.vokers.carscanner.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;
import com.empire.vince.vokers.carscanner.utilities.VokersTextView;

import java.text.DateFormat;
import java.util.Date;

public class CarDetailsActivity extends AppCompatActivity {
    private VokersTextView mCarName;
    private VokersTextView mDriverName;
    private VokersTextView mNumberPlate;
    private VokersTextView mCheckInOrOut;
    private LinearLayout mLinearLayout;
    private boolean check = true;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private boolean checkUpCode = false;
    private ProgressDialog progress;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_details);

        mCarName = (VokersTextView) findViewById(R.id.car_name_desc);
        mDriverName = (VokersTextView) findViewById(R.id.car_drivername);
        mNumberPlate = (VokersTextView) findViewById(R.id.big_numberplate);
        mCheckInOrOut = (VokersTextView) findViewById(R.id.check_in_or_out);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        //String name = b.getString("name");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final String car_name = b.getString("car_name");
        String car_driver = b.getString("car_driver");
        final String number_plate = b.getString("number_plate");
        String hex_val = b.getString("hex_val");
        String id = b.getString("id");
        status = b.getString("status");
        final String location = b.getString("location");

        if (status.equalsIgnoreCase("in")) {
            mLinearLayout.setBackgroundResource(R.color.red);
            mCheckInOrOut.setText(R.string.check_out);
            status = new String("out");

        } else {
            mLinearLayout.setBackgroundResource(R.color.green);
            mCheckInOrOut.setText(R.string.check_in);
            status = new String("in");


        }

        mCarName.setText(car_name);
        mDriverName.setText(car_driver);
        mNumberPlate.setText(number_plate);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String fullname = preferences.getString("name", "");

        //Getting today's date
        DateFormat format = DateFormat.getDateTimeInstance();
        // System.out.println(format.format(date));
        final String today = format.format(new Date());

        //System.out.println(dateInstance.format(Calendar.getInstance().getTime()));


        mLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                checkUpCode = false;
                new AlertDialog.Builder(CarDetailsActivity.this)
                        .setTitle("Check " + status.toUpperCase())
                        .setMessage("You want to check " + status.toLowerCase() + " this vehicle?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                startDialog(view);
                                db.updateCarDetails(status,number_plate);
                             //   db.addDetails(fullname, number_plate, today, status, getLocation().toUpperCase());
                                //checkUpCode = true;
                                dialog.dismiss();
                                loadOutDialog();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                checkUpCode = false;
                                hideDialog();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                if (checkUpCode) {
                }
                // Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
            }
        });



    }
    public String getLocation(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loc = preferences.getString("LOCATION", "");
        return loc==null? AppConfig.LOCATION:loc;
    }
    public void startDialog(View view){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                       // progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

    }
    private void hideDialog() {
        //if (pDialog.isShowing())

        try {
            if ((this.progress!= null) && this.progress.isShowing()) {
                this.progress.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.progress = null;
        }
        // pDialog.dismiss();
    }

    private void loadOutDialog(){
        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        hideDialog();
        //finish();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
