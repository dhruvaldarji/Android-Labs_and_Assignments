package com.ddarji.hello_world.hello_world;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.Date;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Hello_World_Activity extends ActionBarActivity {

    EditText text1;

    TextView view1;

    String msg = "Dhruval's Android : ";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello__world);

        text1=(EditText) findViewById(R.id.editText1);
        view1=(TextView) findViewById(R.id.textView1);

        Log.d(msg, "The onCreate() event");
    }

    public void onClick(View view){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        if(text1.getText().toString().length()==0){ // EditText is empty
            view1.setText("Hello Default User! \r\nNow is "+currentDateTimeString);
        }
        else {
            view1.setText("Hello "+ text1.getText().toString() +"! \nNow is "+currentDateTimeString);
        }
        text1.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello__world, menu);
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

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }
    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }
    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }
    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }
    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

}
