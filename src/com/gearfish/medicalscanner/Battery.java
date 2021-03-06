package com.gearfish.medicalscanner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * This class is used for management of runtime w.r.t. real and virtual battery
 * level. The class itself is updated on start by every activity which uses
 * battery (all of them actually) and then either posts battery change on them
 * or blocks the view completely if there is a such a scenario.
 * 
 * @author punyone
 * 
 */
class Battery extends BroadcastReceiver {
    private final static int SECOND = 1000;
    private final static int SECONDS_PER_MINUTE = 60;
    private final static int STEPS = 100; // Lenght of the scale of battery
					  // values.

    private static int battery = 100; // Real battery status as given by last update. Initially on 100.

    private static Activity current_act = null; // Activity that is currently
						// active.

    /**
     * A handler that updates the picture dependent on the current level.
     */
    private static Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    setPicture();
	}
    };

    @Override
    public void onReceive(Context context, Intent intent) {
	// Move the scale by 10% so it ends with 10% of energy if necessary.
	int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	battery = Math.max(0, level - 10);

	Log.e("BatteryManager", "level is " + level + "/" + scale);
    }

    /**
     * Set an activity which is now running for battery management.
     * 
     * @param passed_activity
     *            currently running activity
     * @return true if everything went OK
     */
    public static boolean setActivity(Activity passed_activity) {
	current_act = passed_activity;
	return setPicture();
    }

    /**
     * Static function that is responsible for choosing the right picture of the
     * battery in the current activity. There are four pictures in dependence on
     * what is the current status of the batter. If the battery is depleted,
     * "please charge" screen is shown.
     * 
     * @param passed_activity
     *            an activity that holds battery layout and is being shown now
     * @return true if the screen remained active, false if the please charge
     *         screen is shown or if there is no battery layout
     */
    public static boolean setPicture() {
	if (current_act == null)
	    return false;

	if (battery <= 0) {
	    // End the activity if it is not the main one. Otherwise set the
	    // final picture.
	    if (current_act.getClass() != MainActivity.class)
		current_act.finish();
	    else {
		current_act.setContentView(R.layout.battery_out);
	    }
	    return false;
	} else {
	    // End if there is no battery.
	    ImageView battery_view = (ImageView) current_act.findViewById(R.id.battery);
	    if (battery_view == null)
		return false;

	    // Pick the picture.
	    int picture_ID = 0;
	    if (battery >= 75)
		picture_ID = R.drawable.battery_01;
	    else if (battery >= 50)
		picture_ID = R.drawable.battery_02;
	    else if (battery >= 25)
		picture_ID = R.drawable.battery_03;
	    else
		picture_ID = R.drawable.battery_04;

	    // Set the picture.
	    battery_view.setImageResource(picture_ID);
	    return true;
	}
    }
};
