package com.gearfish.medicalscanner;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Database extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.database, menu);
        return true;
    }
}
