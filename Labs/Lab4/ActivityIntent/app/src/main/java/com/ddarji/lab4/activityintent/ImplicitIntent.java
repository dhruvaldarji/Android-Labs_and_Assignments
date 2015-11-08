package com.ddarji.lab4.activityintent;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ImplicitIntent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_intent);

        Button btnA = (Button) findViewById(R.id.button1);
        btnA.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //set implicit intent to match activity A
                Intent intent = new Intent();
                //set action
                intent.setAction("com.wyou.action");
                //set category
                intent.addCategory("com.wyou.category");
                //set data
                intent.setData(Uri.parse("wyou://www.wyou.com/wyou"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Activity is matched!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        Button btnB = (Button) findViewById(R.id.button2);
        btnB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //set implicit intent to match activity B
                Intent intent = new Intent();
                //set action
                intent.setAction("com.wyou.actionB");
                //set category
                intent.addCategory("com.wyou.categoryB");
                //set data
                intent.setData(Uri.parse("wyou://www.wyou.com/wyou"));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Activity is matched!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button btnC = (Button) findViewById(R.id.button3);
        btnC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //set implicit intent to match activity C
                Intent intent = new Intent();
                //set action
                intent.setAction("com.wyou.action");
                //set category
                intent.addCategory("com.wyou.category");

                //set data to match "text/*", so this intent will invoke ActivityC
                intent.setDataAndType(Uri.parse("wyou://www.wyou.com/wyou"), "text/plain");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Activity is matched!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_implicit_intent, menu);
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
