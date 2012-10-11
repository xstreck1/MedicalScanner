package com.gearfish.medicalscanner;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class Database extends ListActivity {
	ListAdapter results;
	SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String [] results_list; 
        
        // Get preferences and from them the last result
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	int result_num = prefs.getInt(getString(R.string.results_number), 0);
    	if (result_num == 0) {
    		results_list = new String [1];
    		results_list[0] = getString(R.string.no_res);
    	}
    	else {
    		results_list = new String [result_num];
    		for (int i = 0; i < result_num; i++) {
    			results_list[i] = prefs.getString(Integer.toString(i+1), getString(R.string.wrong_res));
    		}
    	}
        
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results_list));
        setContentView(R.layout.database);			
    }
    
    /**
     * Back button was pushed.
     * 
     * @param view button that called the function
     */
	public void goBack(View view) {		
        finish();
	}	
}
