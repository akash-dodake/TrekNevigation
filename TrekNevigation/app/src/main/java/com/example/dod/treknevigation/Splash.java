package com.example.dod.treknevigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by sony on 29-09-2015.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);
        Thread timer = new Thread() {
            public void run() {
                try {

                    sleep(400);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {

                    Intent iii = new Intent("com.example.dod.treknevigation.MainActivity");
                    startActivity(iii);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
