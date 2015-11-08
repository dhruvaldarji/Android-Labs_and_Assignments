package com.ddarji.lab9.permission;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SMSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        final Button send = (Button) findViewById(R.id.send);
        final EditText text = (EditText) findViewById(R.id.Text);
        final EditText addr = (EditText) findViewById(R.id.addr);
        send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // get address and text
                String maddr = addr.getText().toString();
                String mtext = text.getText().toString();
                // send message
                if (maddr.trim().length() != 0 && mtext.trim().length() != 0) {
                    try {
                        PendingIntent pintent = PendingIntent.getBroadcast(SMSActivity.this, 0, new Intent(), 0);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(maddr, null, mtext, pintent, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(SMSActivity.this,
                            "Enter Address and Message.", Toast.LENGTH_SHORT)
                            .show();
            }

            ;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sm, menu);
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
