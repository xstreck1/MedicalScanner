package com.gearfish.medicalscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Scanner extends Activity implements CameraPreview.OnQrDecodedListener { 
	private CameraPreview mCameraPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner);

		// Get access to the camera object
		mCameraPreview = (CameraPreview) findViewById(R.id.surface);
		mCameraPreview.setOnQrDecodedListener(this);
	}

	/**
	 *  Called when the string is actually obtained and decoded by the CameraPreview.
	 *  
	 *  @param s	string content of the code
	 */
	public void onQrDecoded(String result) {
        Intent mIntent = new Intent();
        mIntent.putExtra("result", result);
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
