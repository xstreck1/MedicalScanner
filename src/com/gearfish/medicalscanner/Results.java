package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class Results extends Activity {
	SharedPreferences prefs;
	
	TextView result_text;
	
    @Override
    public void onCreate(Bundle result_bundle) {
        super.onCreate(result_bundle);
        
        setContentView(R.layout.results);
        
        // Get preferences and from them the last result
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	int result_num = prefs.getInt(getString(R.string.results_number), 0);
    	String result = prefs.getString(Integer.toString(result_num), getString(R.string.err_result));
        
    	// Display the result text
        result_text = (TextView) findViewById(R.id.resultText);
        result_text.setText(result);
    }
    
    /**
     * Back button was pushed.
     * 
     * @param view button that called the function
     */
	public void goBack(View view) {		
        finish();
	}	
}
