package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class Starter extends Activity {
	SharedPreferences.Editor edit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter);
        
        edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
    }
    
    public void chooseCalibration(View view) {
    	edit.putBoolean(getString(R.string.calib_pref_name), (view.getId() == R.id.after_calibration_button));
    	edit.apply();
    	startActivity(new Intent(this,MainActivity.class));
    	finish();
    }
}
