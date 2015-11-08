package com.ddarji.assignment1.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

import java.text.DecimalFormat;


public class Main extends Activity implements OnClickListener{

    private TextView mCalculatorDisplay;
    private Boolean userIsInTheMiddleOfTypingANumber = false;
    private CalculatorProcessor mCalculatorProcessor;
    private static final String DIGITS = "0123456789.";

    // Define new Decimal Format.
    DecimalFormat df = new DecimalFormat("@###########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);

        // Instantiate Calculator Processor
        mCalculatorProcessor = new CalculatorProcessor();

        // Define Calculator Display
        mCalculatorDisplay = (TextView) findViewById(R.id.textView1);

        // Set formats.
        df.setMinimumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        df.setMaximumIntegerDigits(10);

        //Define Buttons
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);

        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonPercent).setOnClickListener(this);
        findViewById(R.id.buttonToggleSign).setOnClickListener(this);
        findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
        findViewById(R.id.buttonEquals).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);

        // Define these buttons only if present.
        // The following buttons only exist in layout-land (Landscape mode) and require extra attention.
        if (findViewById(R.id.buttonClearMemory) != null) {
            findViewById(R.id.buttonClearMemory).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonAddToMemory) != null) {
            findViewById(R.id.buttonAddToMemory).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSubtractFromMemory) != null) {
            findViewById(R.id.buttonSubtractFromMemory).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonRecallMemory) != null) {
            findViewById(R.id.buttonRecallMemory).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSquareRoot) != null) {
            findViewById(R.id.buttonSquareRoot).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSquared) != null) {
            findViewById(R.id.buttonSquared).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonInvert) != null) {
            findViewById(R.id.buttonInvert).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSine) != null) {
            findViewById(R.id.buttonSine).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonCosine) != null) {
            findViewById(R.id.buttonCosine).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonTangent) != null) {
            findViewById(R.id.buttonTangent).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonPi) != null) {
            findViewById(R.id.buttonPi).setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gui, menu);
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

    /**
     * When screen changes, save values.
     * @param outState : Outgoing State
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save variables on screen orientation change
        outState.putDouble("OPERAND", mCalculatorProcessor.getResult());
        outState.putDouble("MEMORY", mCalculatorProcessor.getMemory());
    }

    /**
     * When screen changes restore values.
     * @param savedInstanceState : Incoming State
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore variables on screen orientation change
        mCalculatorProcessor.setOperand(savedInstanceState.getDouble("OPERAND"));
        mCalculatorProcessor.setMemory(savedInstanceState.getDouble("MEMORY"));
        mCalculatorDisplay.setText(df.format(mCalculatorProcessor.getResult()));
    }

    /**
     * Vibrate device when button is clicked (This is for devices running API < v21.)
     * Process button click.
     * @param v
     */
    @Override
    public void onClick(View v) {

        // Define button.
        Button button = (Button) v;

        //Get button text.
        String buttonPressed = button.getText().toString();

        // Check to see what was pressed and process.
        if (DIGITS.contains(buttonPressed)) {

            // digit was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                if (buttonPressed.equals(".") && mCalculatorDisplay.getText().toString().contains(".")) {
                    // ERROR PREVENTION
                    // Do nothing to eliminate entering multiple decimals.
                } else {
                    mCalculatorDisplay.append(buttonPressed);
                    vibrate(50);
                }

            } else {

                if (buttonPressed.equals(".")) {
                    // ERROR PREVENTION
                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
                    // before the decimal
                    mCalculatorDisplay.setText(0 + buttonPressed);
                } else {
                    mCalculatorDisplay.setText(buttonPressed);
                }

                vibrate(50);

                userIsInTheMiddleOfTypingANumber = true;
            }

        } else {
            // operation was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                mCalculatorProcessor.setOperand(Double.parseDouble(mCalculatorDisplay.getText().toString()));
                userIsInTheMiddleOfTypingANumber = false;
            }

            mCalculatorProcessor.performOperation(buttonPressed);
            mCalculatorDisplay.setText(df.format(mCalculatorProcessor.getResult()));

            vibrate(50);

        }

    }


    /**
     * Vibrate device for 'length' ms.
     * @param length : Milliseconds to vibrate
     */
    private void vibrate(int length){
        try {
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(length);
        } catch (Exception e){
            Log.w("Vibrate Error", e);
        }
    }
}
