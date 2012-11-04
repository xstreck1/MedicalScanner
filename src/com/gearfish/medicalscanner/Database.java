package com.gearfish.medicalscanner;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Database extends ListActivity {
	ListAdapter results;
	SharedPreferences prefs;
	String [] results_list; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        createResultsList();
        
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results_list));
        setContentView(R.layout.database);			
    }
    
    @Override
    public void onResume() {
		super.onResume();
		
		((TextView) findViewById(R.id.battery)).setText(String.valueOf(BatteryReciever.value) + "%");   
    }
    
    /**
     * Read from the shared preferences all the data that has been read up till know and display them.
     * Ordering of the data is from the newest to the oldest.
     */
    void createResultsList() {
        // Get preferences and from them the last result
    	int result_num = prefs.getInt(getString(R.string.results_number), 0);
    	// If there is no result up till now, explain it
    	if (result_num == 0) {
    		results_list = new String [1];
    		results_list[0] = getString(R.string.no_res);
    	}
    	// Otherwise display all the screenings from the newest one together with the newest having the number 1
    	else {
    		results_list = new String [result_num];
    		int string_num = 0;
    		for (int i = result_num; i > 0; i--, string_num++) {
    			results_list[string_num] = Integer.toString(string_num + 1) + ". "; 
    			results_list[string_num] += prefs.getString(Integer.toString(i), getString(R.string.wrong_res));
    		}
    	} 
    }
    
    /**
     * Back button was pushed.
     * 
     * @param view button that called the function
     */
	public void goBack(View view) {		
        finish();
	}	
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
    	Intent mIntent = new Intent(this, Results.class);
    	// Fill in value of reordered position
    	int results_num = prefs.getInt(getString(R.string.results_number), 0);
    	mIntent.putExtra(getString(R.string.result_choice), results_num - position);
    	startActivity(mIntent);	
    	finish();		
	}
}
