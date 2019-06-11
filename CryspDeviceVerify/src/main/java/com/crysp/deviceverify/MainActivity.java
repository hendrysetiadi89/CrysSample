package com.crysp.deviceverify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.crysp.sdk.CryspAPI;


public class MainActivity extends FragmentActivity {

    public static StringBuilder sb = new StringBuilder();
    final Context context = this;
    boolean firstRun = false;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#424242")));

        FragmentManager manager = getSupportFragmentManager();


        String serverURL = CryspAPI.getInstance().getServerURL(MainActivity.this);
        //CryspAPI.getInstance().setServerURL(MainActivity.this, "https://xas-australia.crysp.com/");

        CryspAPI.getInstance().setServerURL(MainActivity.this, serverURL);

        FragmentTransaction transactionm = MainActivity.this.getSupportFragmentManager().beginTransaction();
        transactionm.replace(R.id.activity_home_fragment_container, loginFragment.newInstance(null), "loginscreen");
        transactionm.commit();

        //		}
        //	}, 1000);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        if (manager.findFragmentByTag("settings") != null
                && manager.findFragmentByTag("settings").isVisible()) {

        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
