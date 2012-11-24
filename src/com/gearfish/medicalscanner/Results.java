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

/**
 * This class finds the data content based on the key either provided from the
 * database or from the result screen. Display the data and based on their
 * nature set the bottom picture.
 * 
 * @author punyone
 * 
 */
public class Results extends Activity {
    SharedPreferences prefs;

    /**
     * Determines which picture should be displayed based on the output.
     * 
     * @author punyone
     * 
     */
    enum Pic_type {
	human, enemy, error
    };

    Pic_type my_pic;

    @Override
    public void onCreate(Bundle result_bundle) {
	super.onCreate(result_bundle);

	setContentView(R.layout.results);
	prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	// Get the data:
	// Find out what the last result was - use 0 if you can't find it.
	int result_num = prefs.getInt(getString(R.string.results_number), 0);
	// Get a result number if it was chosen (as opposed to invoked from the
	// scanner).
	result_num = getIntent().getIntExtra(getString(R.string.result_choice), result_num);
	// Get key for the results itself.
	String key = prefs.getString(Integer.toString(result_num), getString(R.string.err_result));
	// Get the keyed data from the XML file.
	String value = getData(key);

	// Control presence of the "nepritel" string
	my_pic = Pic_type.human;
	if (value.replaceAll("\\s", " ").matches(".*[Nn][Ee][Pp][Rr][Ii][Tt][Ee][Ll].*"))
	    my_pic = Pic_type.enemy;
	else if (value.matches(getString(R.string.err_data)))
	    my_pic = Pic_type.error;

	// Display the result text
	TextView result_text = (TextView) findViewById(R.id.resultText);
	result_text.setText(value);
    }

    @Override
    public void onResume() {
	super.onResume();

	// Set the bottom picture based on the content of the result - also, if
	// there is no more battery, return from this activity.
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

    /**
     * This function reads key value from the database. Error string is set if
     * the key is not found or if the text is empty.
     * 
     * @param key
     *            string that is searched for
     * @return the content of the key if it is found, otherwise an error string
     */
    String getData(final String key) {
	String value = getString(R.string.err_data);

	// Create the pull parser and associate it with the correct file
	XmlPullParser xpp;
	if (prefs.getBoolean(getString(R.string.calib_pref_name), false))
	    xpp = getResources().getXml(R.xml.post_calib);
	else
	    xpp = getResources().getXml(R.xml.pre_calib);

	try {
	    // Go through all the tags.
	    for (; xpp.getEventType() != XmlPullParser.END_DOCUMENT; xpp.next()) {
		// Wait for starting tag.
		if (xpp.getEventType() != XmlPullParser.START_TAG)
		    continue;
		// Use only those that have an attribute.
		if (xpp.getAttributeCount() == 0)
		    continue;
		// Wait for the one whose attribute matches the key.
		else if (xpp.getAttributeValue(0).equals(key)) {
		    // Get the text node.
		    xpp.next();
		    if (xpp.getEventType() != XmlPullParser.TEXT)
		    	return value;
			
		    // Get the data or leave the error string if the data are
		    // null.
		    return (xpp.getText() == null) ? value : xpp.getText();
		}
	    }
	} catch (Throwable t) {
	    t.printStackTrace();
	}
	return value;
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
