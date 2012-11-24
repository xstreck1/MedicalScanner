package com.gearfish.medicalscanner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.ImageView;

public class Battery extends BroadcastReceiver {
	private Timer minuter; // /< Controls length of displaying of this screen.
	private final static int SECOND = 1000;
	private final static int SECONDS_PER_MINUTE = 60;

	private static int start_time = 0;
	private static int remaining_time = 0;

	private int scale = -1;
	private int level = -1;

	private static int value = 0;

	private static Activity current_act = null;

	public Battery(int time_in_mins) {
		start_time = remaining_time = time_in_mins * SECONDS_PER_MINUTE;

		setTimer();
	}

	/**
	 * Start a new timer responsible of counting down the minutes
	 */
	private void setTimer() {
		minuter = new Timer();
		minuter.schedule(new TimerTask() {
			@Override
			public void run() {
				if (remaining_time-- > 0)
					setTimer();
				value = Math.min(value, ((remaining_time * 100) / start_time));
			}
		}, SECOND);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		Log.e("BatteryManager", "level is " + level + "/" + scale);

		// Give the remaining number of percent of battery as a min(runtime,real
		// battery)
		value = Math.min(((scale * 100) / level),
				((remaining_time * 100) / start_time));
	}

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
			// Set final screen and end.
			current_act.setContentView(R.layout.battery_out);
			return false;
		} else {
			// End if there is no battery.
			ImageView battery_view = (ImageView) current_act
					.findViewById(R.id.battery);
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