package com.example.bgm.devicenetworkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import util.DNService;
import util.U;
import util.myPhoneStateListener;

public class MainActivity extends ActionBarActivity {

    public static boolean isProcess = false;
    public static String dnText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        U.setTitleAppVersion(this);
        try {
            // register Sig Listener
            myPhoneStateListener pslistener = new myPhoneStateListener(this);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        } catch (Exception e) {
            U.e(e.toString());
            U.setTextViewTextWithTS(this, e);
        }
    }

    public synchronized void processDN() {
        if (isProcess == false) {
            try {
                String o = DNService.process(this);
                U.setTextViewTextWithTS(this, o);
                isProcess = true;
                dnText = o;
            } catch (Exception e) {
                U.e(e.toString());
                U.setTextViewTextWithTS(this, e);
            }
        } else {
            U.d("already processed");
            U.setTextViewTextWithTS(this, "already processed(restart if you need reprocess)\n" + dnText);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
