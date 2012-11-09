package com.gearfish.medicalscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner);

		// Get access to the camera object
		mCameraPreview = (CameraPreview) findViewById(R.id.surface);
		mCameraPreview.setOnQrDecodedListener(this);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		edit = prefs.edit();
	}
	
	/**
	 *  Called when the string is actually obtained and decoded by the CameraPreview.
	 *  
	 *  @param s	string content of the code
	 */
	public void onQrDecoded(String result) {
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
		
		// Try to get the camera - if you fail, alert the user 
		if (!mCameraPreview.acquireCamera(getWindowManager().getDefaultDisplay().getRotation())) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.dlg_alert_msg)).setCancelable(false)
				   .setPositiveButton(getResources().getString(R.string.dlg_alert_ok_btn_caption),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Scanner.this.finish();
							dialog.dismiss();
						}
				   });
			AlertDialog alert = builder.create();	
			alert.show();
			return;
		}
		
		int battery = BatteryReciever.value;
		if (battery <= 0)
			setContentView(R.layout.battery_out);
		else {
			if (battery >= 75)
				((ImageView) findViewById(R.id.battery))
						.setImageResource(R.drawable.battery_01);
			else if (battery >= 50)
				((ImageView) findViewById(R.id.battery))
						.setImageResource(R.drawable.battery_02);
			else if (battery >= 25)
				((ImageView) findViewById(R.id.battery))
						.setImageResource(R.drawable.battery_03);
			else
				((ImageView) findViewById(R.id.battery))
						.setImageResource(R.drawable.battery_04);
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
