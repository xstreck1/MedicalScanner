package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class Starter extends Activity {
	SharedPreferences.Editor edit;
	BatteryReciever receiver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter);
        
        edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
    }
    
    public void chooseCalibration(View view) {
    	edit.putBoolean(getString(R.string.calib_pref_name), (view.getId() == R.id.after_calibration_button));
    	edit.apply();
    	
    	String time_string = (((EditText)findViewById(R.id.timingField)).getText()).toString();
    	if (Integer.valueOf(time_string) <= 0)
    		finish();

	    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    receiver = new BatteryReciever(Integer.valueOf(time_string));
	    registerReceiver(receiver, filter);
    	
    	startActivity(new Intent(this,MainActivity.class));
    }
    
    public void onDestroy() {
    	super.onDestroy();
    	
    	unregisterReceiver(receiver);
    }
}
