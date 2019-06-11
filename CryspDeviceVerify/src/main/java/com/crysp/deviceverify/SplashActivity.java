package com.crysp.deviceverify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }

}