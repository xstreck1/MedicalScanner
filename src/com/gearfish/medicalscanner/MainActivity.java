package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    	
    	if (has_focus) {
    		int battery = BatteryReciever.value;
    		if (battery <= 0)
    			setContentView(R.layout.battery_out);
    		else {
	    		setContentView(R.layout.activity_main);

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
	    		
	    		logo_view = (ImageView) findViewById(R.id.logoAnimation);
	    		logo_anim = (AnimationDrawable) logo_view.getDrawable();
	    		logo_anim.start();
    		}
    	}    	
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
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
		setContentView(R.layout.loading);
		((TextView) findViewById(R.id.battery)).setText(String.valueOf(BatteryReciever.value) + "%");   
		
		Intent intent = new Intent(this, Scanner.class);
		startActivityForResult(intent, 0);
	}
    
    /**
     * Call for the activity that will display older results.
     * 
     * @param view 
     */
	public void goToDatabase(View view) {
		Intent intent = new Intent(this, Database.class);
		startActivityForResult(intent, 0);
	}	
}
