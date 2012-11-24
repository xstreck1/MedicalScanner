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

    private static int running_time = 1; // Runtime given in seconds.
    private static int remaining_time = 1; // What is remaining of the runtime.

    private static int value = 0; // Current value of the battery on the scale
				  // from 0 to 100.

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

    /**
     * An update which is conducted every second - checks the current battery
     * status and posts an update to the handler that then updates the picture.
     * 
     * @see TimerTask is an one use only object!
     */
    private class UpdateTask extends TimerTask {
	@Override
	public void run() {
	    // Count down seconds - stop at zero.
	    if (remaining_time-- > 0)
		setTimer();

	    // Update data.
	    value = Math.min(value, ((remaining_time * 100) / running_time));
	    handler.sendEmptyMessage(0);
	}
    };

    /**
     * Schedules updates.
     */
    static Timer minuter = new Timer();

    /**
     * Set times and start first counting.
     * 
     * @param time_in_mins
     *            how many minutes the app should work
     */
    public void setTime(int time_in_mins) {
	running_time = remaining_time = time_in_mins * SECONDS_PER_MINUTE;
	setTimer();
    }

    /**
     * Start a new timer responsible of counting down the time. The timer
     * updates itself each minute.
     */
    private void setTimer() {
	minuter.schedule(new UpdateTask(), SECOND);
	value = 100;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	Log.e("BatteryManager", "level is " + level + "/" + scale);

	// Give the remaining number of percent of battery as a min(runtime,real
	// battery)
	value = Math.min(((level * 100) / scale), ((remaining_time * 100) / running_time));
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

	if (value <= 0) {
	    // End the activity if it is not the main one. Otherwise set the final picture.
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
	    if (value >= 75)
		picture_ID = R.drawable.battery_01;
	    else if (value >= 50)
		picture_ID = R.drawable.battery_02;
	    else if (value >= 25)
		picture_ID = R.drawable.battery_03;
	    else
		picture_ID = R.drawable.battery_04;

	    // Set the picture.
	    battery_view.setImageResource(picture_ID);
	    return true;
	}
    }
};
