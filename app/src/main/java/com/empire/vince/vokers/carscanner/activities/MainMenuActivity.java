package com.empire.vince.vokers.carscanner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.UHFDemo.EPC;
import com.empire.vince.vokers.carscanner.history.HistoryActivity;
import com.empire.vince.vokers.carscanner.login.LoginActivity;
import com.empire.vince.vokers.carscanner.login.SQLiteHandler;
import com.empire.vince.vokers.carscanner.login.SessionManager;
import com.empire.vince.vokers.carscanner.model.Driver;
import com.empire.vince.vokers.carscanner.model.User;
import com.empire.vince.vokers.carscanner.search.Car;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;
import com.empire.vince.vokers.carscanner.utilities.AppController;
import com.empire.vince.vokers.carscanner.utilities.RingButton;
import com.empire.vince.vokers.carscanner.utilities.Util;
import com.handheld.UHF.UhfManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pda.serialport.SerialPort;
import cn.pda.serialport.Tools;

/**
 * Created by VinceGee on 08/27/2016.
 */
public class MainMenuActivity extends Activity implements OnClickListener {



    private UhfManager manager; // UHF manager,UHF Operating handle
    //private TextView textVersion;
    private ArrayList<EPC> listEPC;
    private ArrayList<String> listepc = new ArrayList<String>();
    private ArrayList<Map<String, Object>> listMap;
    private boolean runFlag = true;
    private boolean startFlag = false;
    private List<Car> productsList = new ArrayList<Car>();
    private ProgressDialog pDialog;
    private String what = "uhf";
    private String mTextValue;
    private SQLiteHandler db;
    private SessionManager session;
    private long exitTime = 0;
    private Button mUpdate;
    private String mEmail;
    private TextView tvBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOverflowShowingAlways();
        setContentView(R.layout.activity_main_button);
        final Context context = this;

        TextView nameView = (TextView) findViewById(R.id.username_textview);
        final Button logout = (Button) findViewById(R.id.logout_button);
        mUpdate = (Button) findViewById(R.id.buttonUpdate);
        tvBlink = (TextView) findViewById(R.id.textViewBlink);
        tvBlink .setVisibility(View.INVISIBLE);

        try{
            Bundle b = new Bundle();
            b = getIntent().getExtras();
            if(b.getString("true").equals("true") ){
                blinkTextView();
            }

        }catch (Exception e){
            Log.d("No extras"," Proceed normally");
        }




        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MainMenuActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                logoutUser();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        mUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MainMenuActivity.this)
                        .setTitle("Update")
                        .setMessage("Are you sure you want to sync database?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                //Toast.makeText(getApplicationContext(),"Development in Progress",Toast.LENGTH_LONG).show();

                                if (false/*!db.isContentEmpty(*/) {
                                    if (isInternetOn()) {
                                        ArrayList<Driver> wins = db.getDriversInfo();

                                        for (Driver wns : wins) {
                                            String sguard = wns.getName();
                                            String sno_plate = wns.getNo_plate();
                                            String wnsDate = wns.getLic_no();
                                            String wnsStatus = wns.getAddress();
                                            String wnsLocation = wns.getNat_id();


                                            addInfoToMySql(sguard, sno_plate, wnsDate, wnsStatus, wnsLocation);
                                        }// if(isDeleteDb){
                                        db.deleteDBInfo();
                                        logoutUser();
                                        // }
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "No Info. Check history...", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        // Fetching user details from SQLite
        // HashMap<String, String> user = db.getUserDetails();         // Update fullname
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fullname = preferences.getString("name", "");
        mEmail = preferences.getString("email", "");

        nameView.setText(fullname);

        initView();
        //start inventory thread
        Thread thread = new InventoryThread();
        thread.start();
        // init sound pool
        Util.initSoundPool(this);

        RingButton ringButton = (RingButton) findViewById(R.id.ringButton);
        ringButton.setOnClickListener(new RingButton.OnClickListener() {
            @Override
            public void clickUp() {
                if (!startFlag) {
                    startFlag = true;
                    //buttonStart.setText("stop inventory");
                } else {
                    startFlag = false;
                    //buttonStart.setText("inventory");
                }
                pDialog = new ProgressDialog(MainMenuActivity.this);
                pDialog.setMessage("Processing. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                try {
                    Thread.sleep(10);

                    //new LoadIdioms().execute();
                    //pDialog.dismiss();
                    //  addHexToCarActivity();
                    pDialog.dismiss();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                // Toast.makeText(getApplicationContext(), "Click up", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickDown() {
                //  Toast.makeText(getApplicationContext(), "Click down", Toast.LENGTH_SHORT).show();

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                userInput.setSelection(userInput.getText().length());
                TextView displayText = (TextView) promptsView
                        .findViewById(R.id.promptTextView);
                displayText.setText("Enter password to proceed:");


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if(mEmail != null && !userInput.getText().toString().equals("")){
                                            User user = db.getUserDetailsByEmailAndPassword(mEmail,userInput.getText().toString());
                                            if(user != null){
                                                Intent intent = new Intent(context, HistoryActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(context, "Password not correct. Retry.", Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Toast.makeText(context, "Enter details correctly.", Toast.LENGTH_LONG).show();
                                        }
                                        dialog.cancel();

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





            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);

                // Launching the login activity
        Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        SharedPreferences preference = getSharedPreferences("serialport",
                Context.MODE_PRIVATE);
        UhfManager.Port = preference.getInt(what + "port", SerialPort.com13);
        UhfManager.BaudRate = preference.getInt(what + "baudrate",
                SerialPort.baudrate115200);
        UhfManager.Power = preference.getInt(what + "power",
                SerialPort.Power_Rfid);// this power is power source

        String powerString = "";
        switch (UhfManager.Power) {
            case SerialPort.Power_3v3:
                powerString = "power_3V3";
                break;
            case SerialPort.Power_5v:
                powerString = "power_5V";
                break;
            case SerialPort.Power_Scaner:
                powerString = "scan_power";
                break;
            case SerialPort.Power_Psam:
                powerString = "psam_power";
                break;
            case SerialPort.Power_Rfid:
                powerString = "rfid_power";
                break;
            default:
                break;
        }
        TextView textView_title_config;
        //  textView_title_config = (TextView) findViewById(R.id.textview_title_config);
//textView_title_config.setText("Port:com" + UhfManager.Port
// + ";Baudrate:" + UhfManager.BaudRate + ";Power:" + powerString);
        manager = UhfManager.getInstance();
        if (manager == null) {
            //textVersion.setText(getString(R.string.serialport_init_fail_));
            //  setButtonClickable(buttonClear, false);
            // setButtonClickable(buttonStart, false);
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Get the Rf power, and set
        SharedPreferences shared = getSharedPreferences("UhfRfPower", 0);
        int value = shared.getInt("value", 26);
        Log.e("", "value" + value);
        manager.setOutputPower(value);
        byte[] version_bs = manager.getFirmware();
        setTitle("uhf demo");
        if (version_bs != null) {
            //textView_title_config.append("-"+new String(version_bs));
        }
    }

    private void initView() {
        listEPC = new ArrayList<EPC>();
    }

    @Override
    protected void onPause() {
        startFlag = false;
        manager.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        runFlag = false;
        if (manager != null) {
            manager.close();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * on actionbar show menu button
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime < 2000) {
                finish();
            } else {
                exitTime = System.currentTimeMillis();
                Toast.makeText(this, "Double click to exit!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void addHexToCarActivity(String hexVal) {
        Intent intent = new Intent(getApplicationContext(), VehicleButtonMenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hex_val", hexVal);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

    private void blinkTextView(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(tvBlink .getVisibility() == View.VISIBLE){
                            tvBlink .setVisibility(View.INVISIBLE);
                        }else{
                            tvBlink .setVisibility(View.VISIBLE);
                        }
                        blinkTextView();
                    }
                });
            }
        }).start();
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void addInfoToMySql(final String guard, final String numba_plate, final String time, final String status, final String location) {
        // Tag used to cancel the request
        String tag_string_req = "sync";
        Log.e("******", guard + numba_plate + time + status + location);
        pDialog.setMessage("Sync Info...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MainMenuActivity", "Register Response: " + response.toString());
                hideDialog();
                {
                    Toast.makeText(getApplicationContext(), "Info has been added successfully!", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Menu Button Activity", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Problem. Request Assistance", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                DateFormat format = DateFormat.getDateTimeInstance();


                Map<String, String> params = new HashMap<>();
                params.put("guard", guard);
                params.put("numba_plate", numba_plate);
                params.put("time", time);
                params.put("status", status);
                params.put("location", location);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        //if (pDialog.isShowing())

            try {
                if ((this.pDialog != null) && this.pDialog.isShowing()) {
                    this.pDialog.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.pDialog = null;
            }
        // pDialog.dismiss();
    }

    /**
     * Inventory EPC Thread
     */
    class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {

            super.run();

            OUT:
            while (runFlag) {   // true
                if (startFlag) {
                    // manager.stopInventoryMulti()
                    try {
                        epcList = manager.inventoryRealTime(); // inventory real time
                    }catch (Exception e){
                        Log.e("InventoryRealTime","UHFManager null");
                        manager = UhfManager.getInstance();
                        if (manager == null) {

                            return;
                        }

                        epcList = manager.inventoryRealTime(); // inventory real time
                    }
                    if (epcList != null && !epcList.isEmpty()) {
                        // play sound
                        Util.play(1, 0);
                        for (byte[] epc : epcList) {
                            String epcStr = Tools.Bytes2HexString(epc,
                                    epc.length);
                            // private int x = 0;
                            mTextValue = epcStr;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                  //  if (x == 0) {
                                        // mHexCode.setText(mTextValue);
                                        Log.e("Hex value", mTextValue);
                                        addHexToCarActivity(mTextValue);


                                        runFlag =!runFlag;
                                        // Toast.makeText(getApplication(),mTextValue,Toast.LENGTH_SHORT).show();
                                      //  x++;
                                   // }
                                }
                            });
                            break OUT;
                            //addToList(listEPC, epcStr);
                        }
                    }
                    epcList = null;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
