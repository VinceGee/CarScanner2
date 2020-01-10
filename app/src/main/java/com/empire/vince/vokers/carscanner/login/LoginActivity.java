package com.empire.vince.vokers.carscanner.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.empire.vince.vokers.carscanner.activities.MainMenuActivity;
import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.admin.AdminProperties;
import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.utilities.AppConfig;
import com.empire.vince.vokers.carscanner.utilities.JSONParser;
import com.empire.vince.vokers.carscanner.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

//import butterknife.ButterKnife;
//import butterknife.InjectView;

/**
 * Created by VinceGee on 05/30/2016.
 */
public class LoginActivity extends Activity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    // JSON Node names

    //@InjectView(R.id.email)
    EditText inputEmail;
    //@InjectView(R.id.password)
    EditText inputPassword;
    //@InjectView(R.id.btnLogin)
    Button btnLogin;
    //(R.id.loginProgressBar)
    ProgressBar mProgressBar;

    JSONParser jParser = new JSONParser();
    JSONParser jsonParser = new JSONParser();
    JSONArray users = null;
    JSONArray cars = null;
    JSONArray offenses = null;
    JSONArray drivers = null;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        //ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();

        }
        //update content table
        if(db.isUserContentEmpty()){
            updateSqLiteDb();
        }


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(mProgressBar.getVisibility() == View.INVISIBLE){
                    mProgressBar.setVisibility(View.VISIBLE);
                }else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    if (email.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this, AdminProperties.class);
                        startActivity(intent);
                        finish();
                    } else if (!db.isUserContentEmpty()) {
                        checkLocalLogin(email, password);
                    } else {
                        updateSqLiteDb();
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!db.isUserContentEmpty()) {
                            checkLocalLogin(email, password);
                        } else {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),
                                    "No Internet. Try Again Later", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                } else {
                    // Prompt user to enter credentials
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials correctly!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        inputEmail.setText("");
        inputPassword.setText("");
    }

    private void updateSqLiteDb() {
        if (db.isUserContentEmpty()) {
            new LoadUsers().execute();
        }
        if (db.isCarContentEmpty()) {
            new LoadCarDetails().execute();
        }
        if (db.isOffenseContentEmpty()) {
            new LoadOffensesDetails().execute();
        }
        if (db.isDriverContentEmpty()) {
            new LoadDriverDetails().execute();
        }
    }


    private void checkLocalLogin(final String email, final String password) {
        User user = db.getUserDetailsByEmailAndPassword(email,password);
        if(user!=null){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);  //storing details in the shared preferences
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("name", user.getName());
            editor.putString("email", user.getEmail());
            editor.apply();

            session.setLogin(true);
            mProgressBar.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Incorrect Username & Password", Toast.LENGTH_LONG).show();
        }
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

    /**
     * Background Async Task to Load Users by making HTTP Request
     */
    class LoadUsers extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         */


        /**
         * getting Idioms from url
         */
        protected JSONObject doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            // params.put("keyword", mTextValue);
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
            //value captured from previous intent
            //Log.e("AAAA Search keyword: ", mTextValue);
            //params.add(new BasicNameValuePair("keyword", searchkey));
            // getting JSON string from URL


            JSONObject json = jParser.makeHttpRequest(AppConfig.URL_USERS, "GET", params);

            if (json == null) {
                Log.e("JSON result", AppConfig.URL_USERS);
                return json;
            }

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");
                if (success == 1) {
                    users = json.getJSONArray("users");

                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String password = c.getString("plain_passw");


                        db.addUser(name, email, password);
                    }
                } else {
                    Log.e("Json failer", " Success not one");
                    // no users found
                    //do something
                }
            } catch (JSONException e) {
                //Toast.makeText(getActivity(), "Network problem. Try again later.", Toast.LENGTH_LONG);

                Log.e("Exception in users"," might be incorrect json passed");
            }

            //return "success";
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject jsonObject) {
            // dismiss the dialog after getting the related users
            //  pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    Log.e("User table size:",""+ db.getUserDetails().size());

                }
            });

        }


    }

    class LoadCarDetails extends AsyncTask<String, String, JSONObject> {
        /**
         * getting Idioms from url
         */
        protected JSONObject doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_DETAILS, "GET", params);

            if (json == null) {
                return json;
            }

            try {
                int success = json.getInt("success");
                if (success == 1) {


                    cars = json.getJSONArray("mota");

                    // looping through All Products
                    for (int i = 0; i < cars.length(); i++) {
                        JSONObject c = cars.getJSONObject(i);

                        // Storing each json item in variable
                        //String id = c.getString("id");
                        String carMake = c.getString("car_make");
                        String carModel = c.getString("car_model");
                        String numberPleti = c.getString("number_pleti");
                        String hexValue = c.getString("hexvalue");
                        String carColor = c.getString("car_color");
                        String carYear = c.getString("car_year");
                        String trackNo = c.getString("tracker_number");
                        String cellNum = c.getString("cell_number");
                        String simNum = c.getString("sim_number");
                        String route = c.getString("route");
                        // String status = "out";
                        //adding to list
                        db.addCarStaticDetails(carMake, carModel, numberPleti, hexValue, carColor, carYear, trackNo, cellNum, simNum, route);
                    }
                } else {
                    Log.e("Json failer", " Success not one");
                    // no users found
                    //do something
                }
            } catch (JSONException e) {
                Log.e("Exception  *********", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject jsonObject) {

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    Log.e("Car table size",""+ db.getCarStaticDetails().size());


                }
            });

        }


    }

    class LoadOffensesDetails extends AsyncTask<String, String, JSONObject> {
        /**
         * getting Idioms from url
         */
        protected JSONObject doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_OFFENSES, "GET", params);

            if (json == null) {
                return json;
            }

            try {
                int success = json.getInt("success");
                if (success == 1) {
                    offenses = json.getJSONArray("offenses");

                    // looping through All Products
                    for (int i = 0; i < offenses.length(); i++) {
                        JSONObject c = offenses.getJSONObject(i);

                        // Storing each json item in variable
                        //String id = c.getString("id");
                        String numberPleti = c.getString("number_pleti");
                        String oClass = c.getString("class");
                        String offense = c.getString("offense");
                        String offenseDetails = c.getString("offense_details");
                        String offenseDateTime = c.getString("offense_date_time");
                        String offenseLocation = c.getString("offense_location");
                        String offenseFine = c.getString("offense_fine");
                        String fineDueDate = c.getString("fine_due_date");
                        String addedBy = c.getString("added_by");
                        // String status = "out";
                        //adding to list
                        db.addOffensesStaticDetails(numberPleti, oClass, offense, offenseDetails, offenseDateTime, offenseLocation, offenseFine, fineDueDate, addedBy);
                    }
                } else {
                    Log.e("Json failer", " Success not one");
                    // no users found
                    //do something
                }
            } catch (JSONException e) {
                Log.e("Exception  *********", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject jsonObject) {

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */


                    Log.e("Offenses table size ", db.getOffensesStaticDetails().size()+"");

                }
            });

        }
    }

    class LoadDriverDetails extends AsyncTask<String, String, JSONObject> {
        /**
         * getting Idioms from url
         */
        protected JSONObject doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_DRIVERS, "GET", params);

            if (json == null) {
                return json;
            }

            try {
                int success = json.getInt("success");
                if (success == 1) {
                    drivers = json.getJSONArray("drivers");

                    // looping through All Products
                    for (int i = 0; i < drivers.length(); i++) {
                        JSONObject c = drivers.getJSONObject(i);


                        // Storing each json item in variable
                        //String id = c.getString("id");
                        String numberPleti = c.getString("number_pleti");
                        String name = c.getString("d_full_name");
                        String licence = c.getString("licence_no");
                        String drivers_add = c.getString("driver_addr");
                        String nat_id = c.getString("driver_nat_id");
                        String phone = c.getString("driver_cell");


                        db.addDriversStaticDetails(numberPleti, name, licence, drivers_add, nat_id, phone);
                    }
                } else {
                    Log.e("Json failer", " Success not one");
                    // no users found
                    //do something
                }
            } catch (JSONException e) {
                Log.e("Exception  *********", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject jsonObject) {

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */


                    Log.e("Drivers table size:", db.getDriversInfo().size()+"");

                }
            });

        }
    }
}
