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

public class Scanner extends Activity implements CameraPreview.OnQrDecodedListener { 
	private CameraPreview mCameraPreview;
	SharedPreferences.Editor edit;
	SharedPreferences prefs;
	
	final int WAIT = 500; // Time to wait between two camera acquisitions
	
	XmlPullParser xpp; // Parser used for XML file database of strings

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
	 * This function goes through the database and checks whether the string is contained within it.
	 *
	 * @param scanned	string that has been obtained from the QR code
	 * 
	 * @return true if the string is a result, false otherwise
	 */
	private boolean isResult(String scanned) {	    	
		try {
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType() == XmlPullParser.START_TAG)
					if (xpp.getName().equals("RESULT")) 
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
	 *  Called when the string is actually obtained and decoded by the CameraPreview.
	 *  
	 *  @param s	string content of the code
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
		
		Intent mIntent = new Intent();
        setResult(RESULT_OK, mIntent);
        
        finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(getComponentName().flattenToShortString(), "onResume()");
		
		// Try to get the camera - if you fail, end building the application
		if (!mCameraPreview.acquireCamera(getWindowManager().getDefaultDisplay().getRotation())) {
			if (!Battery.setActivity(this))
				goBack();
			else try {
				Thread.sleep(WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				goBack();
				return;
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
	
	public void goBack(View view){
		goBack();
	}
	
	public void goBack() {	
        Intent mIntent = new Intent();
        setResult(RESULT_CANCELED, mIntent);
        finish();
	}
}
