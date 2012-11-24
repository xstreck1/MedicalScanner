package com.gearfish.medicalscanner;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * This class holds a list of previous results. If such is picked, it is
 * displayed in the result screen. Results themselves are formatted with a
 * prefix and reordered so the one that was added last is displayed first (as a
 * stack).
 * 
 * @author punyone
 * 
 */
public class Database extends ListActivity {
    SharedPreferences prefs;

    ListAdapter results; // List containing keys for the results.
    String[] results_list; // A list with numbers and keys.

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	createResultsList();
	setListAdapter(new ArrayAdapter<String>(this, R.layout.my_list_item, results_list));

	setContentView(R.layout.database);
    }

    @Override
    public void onResume() {
	super.onResume();

	// If started with empty battery, finish it.
	Battery.setActivity(this);
    }

    /**
     * Read from the shared preferences all the data that has been read up till
     * know and display them. Ordering of the data is from the newest to the
     * oldest.
     */
    void createResultsList() {
	// Get preferences and from them the last result
	int result_num = prefs.getInt(getString(R.string.results_number), 0);

	// If there is no result up till now, explain it
	if (result_num == 0) {
	    results_list = new String[1];
	    results_list[0] = getString(R.string.no_res);
	}

	// Otherwise display all the screenings from the newest one together
	// with the newest having the number 1
	else {
	    results_list = new String[result_num];
	    int string_num = 1; // Current result number.

	    // Cycle through results (in the opposite direction - the one added
	    // first is processed last.
	    for (int i = result_num; i > 0; i--, string_num++) {
		// Create a numeric prefix.
		if (string_num < 10)
		    results_list[string_num - 1] = "00";
		else if (string_num < 100)
		    results_list[string_num - 1] = "0";
		else
		    results_list[string_num - 1] = "";
		results_list[string_num - 1] += Integer.toString(string_num) + "   ";

		// Add the content.
		results_list[string_num - 1] += prefs.getString(Integer.toString(i), getString(R.string.wrong_res));
	    }
	}
    }

    /**
     * Back button was pushed.
     * 
     * @param view
     *            button that called the function
     */
    public void goBack(View view) {
	finish();
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
	// Check if there is any result, otherwise ban the output as a whole
	if (prefs.getInt(getString(R.string.results_number), 0) == 0)
	    return;

	Intent mIntent = new Intent(this, Results.class);

	// Fill in which one was actually picked (in the opposite order) and
	// pass that information.
	int results_num = prefs.getInt(getString(R.string.results_number), 0);
	mIntent.putExtra(getString(R.string.result_choice), results_num - position);
	startActivity(mIntent);
	finish();
    }
}
