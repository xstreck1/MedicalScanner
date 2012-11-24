package com.gearfish.medicalscanner;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * This class sets camera preview as its background together with an auto-focus.
 * The activity ends if the user presses corresponding button or if a correct
 * string (one that can be found in the database) is read.
 * 
 * @author punyone
 * 
 */
public class Scanner extends Activity implements CameraPreview.OnQrDecodedListener {
    private CameraPreview mCameraPreview;
    private SharedPreferences.Editor edit;
    private SharedPreferences prefs;

    private final int WAIT = 500; // Time to wait between two cam acquisitions.

    private XmlPullParser xpp; // Parser used for XML file database of strings.

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.scanner);

	// Get access to the camera object
	mCameraPreview = (CameraPreview) findViewById(R.id.surface);
	mCameraPreview.setOnQrDecodedListener(this);

	// Get the preferences
	prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	edit = prefs.edit();

	// Get the parser
	if (prefs.getBoolean(getString(R.string.calib_pref_name), false))
	    xpp = getResources().getXml(R.xml.post_calib);
	else
	    xpp = getResources().getXml(R.xml.pre_calib);
    }

    /**
     * This function goes through the database and checks whether the string is
     * contained within it.
     * 
     * @param scanned
     *            string that has been obtained from the QR code
     * 
     * @return true if the string is a result, false otherwise
     */
    private boolean isResult(String scanned) {
	try {
	    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
		if (xpp.getEventType() == XmlPullParser.START_TAG)
		    if (xpp.getName().equals("RESULT"))
			// If the match was found.
			if (xpp.getAttributeValue(0).equals(scanned))
			    return true;
		xpp.next();
	    }
	} catch (Throwable t) {
	    t.printStackTrace();
	}
	return false;
    }

    /**
     * Called when the string is actually obtained and decoded by the
     * CameraPreview.
     * 
     * @param result
     *            string content of the code
     */
    public void onQrDecoded(String result) {
	if (!isResult(result))
	    return;

	// Get a current number of results or use 0 if there was a problem
	String num_name = getString(R.string.results_number);
	int result_num = prefs.getInt(num_name, 0) + 1;

	// Insert new result data and increase the counter
	edit.putString(Integer.toString(result_num), result);
	edit.putInt(num_name, result_num);
	edit.apply();

	// End this app with OK result.
	Intent mIntent = new Intent();
	setResult(RESULT_OK, mIntent);

	finish();
    }

    @Override
    protected void onResume() {
	super.onResume();
	Log.d(getComponentName().flattenToShortString(), "onResume()");

	// End if there is no battery left.
	if (!Battery.setActivity(this))
	    goBack();

	else { // Repeat camera acquisitions until you get it.
	    while (!mCameraPreview.acquireCamera(getWindowManager().getDefaultDisplay().getRotation())) {
		try {
		    Thread.sleep(WAIT);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    goBack();
		    return;
		}
	    }
	}
    }

    @Override
    protected void onPause() {
	super.onPause();
	Log.d(getComponentName().flattenToShortString(), "onPause()");

	mCameraPreview.releaseCamera();
    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();

	goBack();
    }

    /**
     * Adapter for goBack function.
     * 
     * @param view
     *            the back button object.
     */
    public void goBack(View view) {
	goBack();
    }

    /**
     * This function finishes this application and tells the caller that it has
     * not finished successfully.
     */
    public void goBack() {
	Intent mIntent = new Intent();
	setResult(RESULT_CANCELED, mIntent);
	finish();
    }
}
