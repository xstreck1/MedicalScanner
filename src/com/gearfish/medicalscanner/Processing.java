package com.gearfish.medicalscanner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class Processing extends Activity {
	final static long WAIT_TIME = 5000;
	
	Timer delay; ///< Controls length of displaying of this screen.
	
    @Override
    public void onCreate(Bundle result_bundle) {
        super.onCreate(result_bundle);
        
        setContentView(R.layout.processing);
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
		
		// Start timer for processing animation
		delay = new Timer();
		delay.schedule(new TimerTask(){
			@Override
			public void run() {
				displayResult();
			}
		}, WAIT_TIME);
	}
	
    @Override
    public void onWindowFocusChanged(boolean has_focus) {
    	super.onWindowFocusChanged(has_focus);
    	
    	// Ran the animation
    	if (has_focus) {    		
    		ImageView anim_view = (ImageView) findViewById(R.id.processingAnimation);
    		AnimationDrawable animation = (AnimationDrawable) anim_view.getDrawable();
    		animation.start();
    	}    	
    }
	
    /** 
     * After the timer ends, continue to the result screen.
     */
	public void displayResult() {
    	Intent mIntent = new Intent(this, Results.class);
    	delay.cancel();
    	startActivity(mIntent);	
    	finish();
	}
}
