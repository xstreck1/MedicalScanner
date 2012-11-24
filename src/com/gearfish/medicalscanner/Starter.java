package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

/**
 * The class is responsible for managing the preferences for the application
 * overall and also starting it.
 * 
 * @author punyone
 * 
 */
class Starter extends Activity {
    SharedPreferences.Editor edit;
    Battery receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.starter);

	edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

	receiver = new Battery();
	IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	registerReceiver(receiver, filter);
    }

    /**
     * Picked by either of the calibration buttons - chooses which dictionary
     * will be used in the application.
     * 
     * @param view
     *            starting button - its character gives the preference
     */
    public void chooseCalibration(View view) {
	// Erase previous measurements.
	edit.clear();
	edit.apply();

	// Set new preference (true if the button was after calibration).
	edit.putBoolean(getString(R.string.calib_pref_name), (view.getId() == R.id.after_calibration_button));
	edit.apply();

	// Get the time to run and apply it.
	String time_string = (((EditText) findViewById(R.id.timingField)).getText()).toString();
	if (Integer.valueOf(time_string) <= 0)
	    return;
	receiver.setTime(Integer.valueOf(time_string));

	startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onDestroy() {
	super.onDestroy();

	unregisterReceiver(receiver);
    }
}
