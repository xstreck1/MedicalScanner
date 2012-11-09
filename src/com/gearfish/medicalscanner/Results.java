package com.gearfish.medicalscanner;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Results extends Activity {
	SharedPreferences prefs;
	
	TextView result_text;
	
    @Override
    public void onCreate(Bundle result_bundle) {
        super.onCreate(result_bundle);
        
        setContentView(R.layout.results);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        // Get the result picked in database or use the last one if none was picked
    	int result_num = prefs.getInt(getString(R.string.results_number), 0);
    	result_num = getIntent().getIntExtra(getString(R.string.result_choice), result_num);
    	String key = prefs.getString(Integer.toString(result_num), getString(R.string.err_result));
    	String value = getData(key);
        
    	// Display the result text
        result_text = (TextView) findViewById(R.id.resultText);
        result_text.setText(value);
    }
    
    @Override
    public void onResume() {
		super.onResume();
		
		int battery = BatteryReciever.value;
		if (battery <= 0)
			setContentView(R.layout.battery_out);
		else {
			if (battery >= 75)
				((ImageView) findViewById(R.id.battery)).setImageResource(R.drawable.battery_01);
			else if (battery >= 50)
				((ImageView) findViewById(R.id.battery)).setImageResource(R.drawable.battery_02);
			else if (battery >= 25)
				((ImageView) findViewById(R.id.battery)).setImageResource(R.drawable.battery_03);
			else
				((ImageView) findViewById(R.id.battery)).setImageResource(R.drawable.battery_04);
		}    
    }
    
    String getData(final String key) {
    	String value = getString(R.string.err_data);
    	
    	// Create the pull parser and associate it with the correct file
    	XmlPullParser xpp;
    	if (prefs.getBoolean(getString(R.string.calib_pref_name), false))
    		xpp = getResources().getXml(R.xml.post_calib);
    	else
    		xpp = getResources().getXml(R.xml.pre_calib);
    	
    	try {
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals("RESULT")) {
						if (xpp.getAttributeValue(0).equals(key)) {
							do { xpp.next(); }
							while (xpp.getEventType() != XmlPullParser.TEXT);
							value = xpp.getText() + "\n";
							
							do { xpp.next(); }
							while (xpp.getEventType() != XmlPullParser.TEXT);
							value += xpp.getText() + "\n";	
							
							do { xpp.next(); }
							while (xpp.getEventType() != XmlPullParser.TEXT);
							value += xpp.getText();	
						}
					}
				}
				xpp.next();
			}
			
    	} catch (Throwable t) {
    		Toast.makeText(this, "Parsing failed.", Toast.LENGTH_LONG).show();
    	}
    	
    	return value;
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
