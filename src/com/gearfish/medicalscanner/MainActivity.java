// Error on depleted battery starts the scanner working

package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView logo_view;
	private AnimationDrawable logo_anim;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);  
    }
    
    @Override
	public void onWindowFocusChanged(boolean has_focus) {
		super.onWindowFocusChanged(has_focus);

		if (has_focus && Battery.setActivity(this)) {
			logo_view = (ImageView) findViewById(R.id.logoAnimation);
			if (logo_view != null) {
				logo_anim = (AnimationDrawable) logo_view.getDrawable();
				logo_anim.start();
			}
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
		setContentView(R.layout.activity_main);
        
        // If the code was read and the activity was not killed for any other reason (button push), pass the value to the reader 
        if (resultCode == RESULT_OK) {   		    		
        	Intent mIntent = new Intent(this, Processing.class);
        	startActivity(mIntent);
        }
    }
    
    /**
     * Call for activity that is able to read the QR code.
     * 
     * @param view caller of this function - only the startScan button
     */
    public void startScan(View view) {
    	// Block scan if the battery is depleted.
    	if(!Battery.setActivity(this))
    		return;
    	
		setContentView(R.layout.loading);
		
		Intent intent = new Intent(this, Scanner.class);
		startActivityForResult(intent, 0);
	}
    
    /**
     * Call for the activity that will display older results.
     * 
     * @param view	the button responsible for calling this
     */
	public void goToDatabase(View view) {
		Intent intent = new Intent(this, Database.class);
		startActivityForResult(intent, 0);
	}	
}
