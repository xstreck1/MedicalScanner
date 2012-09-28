package com.gearfish.medicalscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView logo_view;
	private AnimationDrawable logo_anim;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
    }
    
    @Override
    public void onWindowFocusChanged(boolean has_focus) {
    	super.onWindowFocusChanged(has_focus);
    	
    	if (has_focus) {
    		logo_view = (ImageView) findViewById(R.id.logoAnimation);
    		logo_anim = (AnimationDrawable) logo_view.getDrawable();
    		logo_anim.start();
    	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        
        if (resultCode == RESULT_OK) {
        	Intent send_to_reader = new Intent(this, Results.class);
        	send_to_reader.putExtra("result", intent.getExtras().getString("result"));
        	startActivity(send_to_reader);
        }
        
        /*Bundle extras = intent.getExtras();
        if (extras.containsKey("result"))
        	logo_view.setText(extras.getCharSequence("result"));
        else 
        	logo_view.setText("Nebyl nalezen subjekt.");*/
    }
    
    public void startScan(View view) {
		Intent intent = new Intent(this, Scanner.class);
		startActivityForResult(intent, 0);
	}
    
	public void goToDatabase(View view) {
		/* move to the database */
	}	
}
