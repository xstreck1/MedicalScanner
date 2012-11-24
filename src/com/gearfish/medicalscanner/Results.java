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

    enum Pic_type {
	human, enemy, error
    };

    Pic_type my_pic;

    @Override
    public void onCreate(Bundle result_bundle) {
	super.onCreate(result_bundle);

	setContentView(R.layout.results);
	prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	// Get the result picked in database or use the last one if none was
	// picked
	int result_num = prefs.getInt(getString(R.string.results_number), 0);
	result_num = getIntent().getIntExtra(getString(R.string.result_choice), result_num);
	String key = prefs.getString(Integer.toString(result_num), getString(R.string.err_result));
	String value = getData(key);

	// Control presence of the "nepritel" string
	my_pic = Pic_type.human;
	if (value.replaceAll("\\s", " ").matches(".*[Nn][Ee][Pp][Rr][Ii][Tt][Ee][Ll].*"))
	    my_pic = Pic_type.enemy;
	else if (value.matches(getString(R.string.err_data)))
	    my_pic = Pic_type.error;

	// Display the result text
	result_text = (TextView) findViewById(R.id.resultText);
	result_text.setText(value);
    }

    @Override
    public void onResume() {
	super.onResume();

	if (Battery.setActivity(this)) {
	    if (my_pic == Pic_type.human)
		((ImageView) findViewById(R.id.resultLogo)).setImageResource(R.drawable.results2a);
	    else if (my_pic == Pic_type.enemy)
		((ImageView) findViewById(R.id.resultLogo)).setImageResource(R.drawable.results2b);
	    else
		((ImageView) findViewById(R.id.resultLogo)).setImageResource(R.drawable.results2c);
	} else {
	    finish();
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
	    for (; xpp.getEventType() != XmlPullParser.END_DOCUMENT; xpp.next()) {
		if (xpp.getEventType() != XmlPullParser.START_TAG)
		    continue;
		if (xpp.getAttributeCount() == 0)
		    continue;
		else if (xpp.getAttributeValue(0).equals(key)) {
		    while(xpp.getEventType() != XmlPullParser.TEXT && xpp.getEventType() != XmlPullParser.END_DOCUMENT)
			xpp.next();
		    
		    value = xpp.getText();
		}
	    }
	}
	catch (Throwable t) {
	    t.printStackTrace();
	}

	return (value != null ? value : "");
    }

    /**
     * Back button was pushed.
     * 
     * @param view
     *            button that called the function
     */
    public void goBack(View view) {
	finish();
    }
}
