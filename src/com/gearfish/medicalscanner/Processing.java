package com.gearfish.medicalscanner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * This class is responsible for the mere processing animation. The animation is
 * timed and after the time runs out, a result string is displayed.
 * 
 * @author punyone
 * 
 */
public class Processing extends Activity {
    private final static long WAIT_TIME = 5000; // Number of seconds (5) of the loading
					// animation.
    private Timer delay; // Controls length of displaying of this screen.

    @Override
    public void onCreate(Bundle result_bundle) {
	super.onCreate(result_bundle);

	setContentView(R.layout.processing);
    }

    @Override
    public void onResume() {
	super.onResume();
	Battery.setActivity(this);

	// Start timer for processing animation
	delay = new Timer();
	delay.schedule(new TimerTask() {
	    @Override
	    public void run() {
		displayResult();
	    }
	}, WAIT_TIME);
    }

    @Override
    public void onWindowFocusChanged(boolean has_focus) {
	super.onWindowFocusChanged(has_focus);

	// Run the animation (safely).
	if (has_focus) {
	    ImageView anim_view = (ImageView) findViewById(R.id.processingAnimation);
	    if (anim_view == null)
		return;
	    AnimationDrawable animation = (AnimationDrawable) anim_view.getDrawable();
	    if (animation == null)
		return;
	    animation.start();
	}
    }

    /**
     * After the timer ends, continue to the result screen.
     */
    private void displayResult() {
	Intent mIntent = new Intent(this, Results.class);
	if(Battery.setActivity(this))
	    startActivity(mIntent);
	finish();
    }
}
