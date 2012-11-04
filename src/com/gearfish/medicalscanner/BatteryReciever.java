package com.gearfish.medicalscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryReciever extends BroadcastReceiver {
			static int value = 0;
	
			int scale = -1;
	        int level = -1;

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	            Log.e("BatteryManager", "level is "+level+"/"+scale);
	           
	            value = ((scale*100)/level);
	        }
};
