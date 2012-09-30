package com.gearfish.medicalscanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gearfish.medicalscanner.R.id;

public class Results extends Activity {
	TextView result_text;
	
    @Override
    public void onCreate(Bundle result_bundle) {
        super.onCreate(result_bundle);
        
        setContentView(R.layout.results);
        
        result_text = (TextView) findViewById(id.resultText);
        result_text.setText(getIntent().getExtras().getCharSequence("result"));
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
