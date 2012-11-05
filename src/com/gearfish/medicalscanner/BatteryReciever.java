package com.gearfish.medicalscanner;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryReciever extends BroadcastReceiver {
			private Timer minuter; ///< Controls length of displaying of this screen.
			private final static int SECOND = 1000;
			private final static int SECONDS_PER_MINUTE = 60;
			
			static int start_time = 0;
			static int remaining_time = 0;
			static int value = 0;
	
			private int scale = -1;
			private int level = -1;

	        public BatteryReciever(int time_in_mins) {
	        	start_time = remaining_time = time_in_mins*SECONDS_PER_MINUTE;
	        	
	        	setTimer();
	        }
	        
	        /**
	         * Start a new timer responsible of counting down the minutes
	         */
	        private void setTimer() {
	        	minuter = new Timer();
	        	minuter.schedule(new TimerTask(){
	    			@Override
	    			public void run() {
	    				if (remaining_time-- > 0)
	    					setTimer();
	    				value = Math.min(value, ((remaining_time*100)/start_time));
	    			}
	    		}, SECOND);
	        }
	        
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	            Log.e("BatteryManager", "level is "+level+"/"+scale);
	           
	            // Give the remaining number of percent of battery as a min(runtime,real battery)
	            value = Math.min(((scale*100)/level),((remaining_time*100)/start_time));
	        }
};
