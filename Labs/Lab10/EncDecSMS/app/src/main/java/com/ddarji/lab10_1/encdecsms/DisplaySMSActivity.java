package com.ddarji.lab10_1.encdecsms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DisplaySMSActivity extends AppCompatActivity {

    EditText secretKey;
    TextView senderNum;
    TextView encryptedMsg;
    TextView decryptedMsg;
    Button submit;
    Button cancel;
    String originNum = "";
    String msgContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onreceive);

        senderNum = (TextView) findViewById(R.id.senderNum);
        encryptedMsg = (TextView) findViewById(R.id.encryptedMsg);
        decryptedMsg = (TextView) findViewById(R.id.decryptedMsg);
        secretKey = (EditText) findViewById(R.id.secretKey);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        // get the Intent extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // get the sender phone number from extra
            originNum = extras.getString("originNum");
            // get the encrypted message body from extra
            msgContent = extras.getString("msgContent");
            // set the text fields in the UI
            senderNum.setText(originNum);
            encryptedMsg.setText(msgContent);
        } else {
            // if the Intent is null, there should be something wrong
            Toast.makeText(getBaseContext(), "Error Occurs!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        // when click on the cancel button, return
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // when click on the submit button decrypt the message body
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // user input the AES secret key
                String secretKeyString = secretKey.getText().toString();
                // key length should be 16 characters as defined by AES-128-bit
                if (secretKeyString.length() > 0
                        && secretKeyString.length() == 16) {
                    try {
                        // convert the encrypted String message body to a byte
                        // array
                        byte[] msg = hex2byte(msgContent.getBytes());
                        // decrypt the byte array
                        byte[] result = decryptSMS(secretKey.getText()
                                .toString(), msg);
                        // set the text view for the decrypted message
                        decryptedMsg.setText(new String(result));
                    } catch (Exception e) {
                        // in the case of message corrupted or invalid key
                        // decryption cannot be carried out
                        decryptedMsg.setText(R.string.message_cannot_be_decrypted);
                    }
                } else
                    Toast.makeText(getBaseContext(),
                            "You must provide a 16-character secret key!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Utility function: convert hex array to byte array
     * @param b : Hex Array
     * @return Byte Array
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("hello");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * Decryption function
     * @param secretKeyString : Secret Key String
     * @param encryptedMsg : Encrypted Message
     * @return Decrypted Byte Array
     * @throws Exception
     */
    public static byte[] decryptSMS(String secretKeyString, byte[] encryptedMsg)
            throws Exception {
        // generate AES secret key from the user input secret key
        Key key = generateKey(secretKeyString);
        // get the cipher algorithm for AES
        @SuppressLint("GetInstance") Cipher c = Cipher.getInstance("AES");
        // specify the decryption mode
        c.init(Cipher.DECRYPT_MODE, key);
        // decrypt the message
        return c.doFinal(encryptedMsg);
    }

    /**
     * Generate Secret Key
     * @param secretKeyString : Secret Key String
     * @return Secret Key
     * @throws Exception
     */
    private static Key generateKey(String secretKeyString) throws Exception {
        // generate AES secret key from a String
        return new SecretKeySpec(secretKeyString.getBytes(), "AES");

    }

}
