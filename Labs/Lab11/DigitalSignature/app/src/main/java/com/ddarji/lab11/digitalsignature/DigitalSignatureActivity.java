package com.ddarji.lab11.digitalsignature;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class DigitalSignatureActivity extends AppCompatActivity {

    private final static String RSA = "RSA";
    public static PublicKey uk;
    public static PrivateKey rk;
    public static int stepcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            generateKey();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ImageButton exam = (ImageButton) findViewById(R.id.ibutton);
        final EditText oritext = (EditText) findViewById(R.id.oritext);
        final Button sent = (Button) findViewById(R.id.sendbutton);
        final EditText ctext = (EditText) findViewById(R.id.changabletext);
        final Button cont = (Button) findViewById(R.id.continuebutton);
        final Button next = (Button) findViewById(R.id.stepbutton);
        final TextView stext = (TextView) findViewById(R.id.steptext);
        final LinearLayout main = (LinearLayout) findViewById(R.id.linearLayout1);
        final LinearLayout sender = (LinearLayout) findViewById(R.id.linearLayout2);
        final LinearLayout hacker = (LinearLayout) findViewById(R.id.linearLayout3);
        final LinearLayout reciever = (LinearLayout) findViewById(R.id.linearLayout4);
        exam.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                main.setVisibility(View.GONE);
                sender.setVisibility(View.VISIBLE);
            }
        });
        sent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sender.setVisibility(View.GONE);
                hacker.setVisibility(View.VISIBLE);
                ctext.setText(oritext.getText().toString());

            }
        });
        cont.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                hacker.setVisibility(View.GONE);
                reciever.setVisibility(View.VISIBLE);
                stext.setText("This is the text from you though the hacker  \n"
                        + ctext.getText().toString());
                stepcount = 0;
            }
        });
        next.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                if (stepcount == 0) {
                    stext.setText("Here is the public key \n" + uk.toString());
                    stepcount++;
                } else if (stepcount == 1) {
                    try {
                        stext.setText("Jack can use the public key to decrypt the hashcode \n"
                                + decrypt(encrypt(myhash(oritext
                                .getEditableText().toString()
                                .getBytes()))));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    stepcount++;
                } else if (stepcount == 2) {
                    try {
                        stext.setText("Then Jack can process the received text by the hash function\n"
                                + myhash(ctext.getEditableText().toString()
                                .getBytes()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    stepcount++;
                } else if (stepcount == 3) {
                    try {
                        stext.setText("The last step, compare two hashcodes, if they are the same, "
                                + "it is a complete text from you, if not there must be a hacker to change it."
                                + "    \n Origin Text 's hashcode: \n"
                                + myhash(oritext.getEditableText().toString().getBytes())
                                + "   \n =?    \n Recieve Text's hashcod:\n "
                                + myhash(ctext.getEditableText().toString().getBytes()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    stepcount = 0;
                }
            }
        });
    }

    public static void generateKey() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(RSA);
        gen.initialize(512, new SecureRandom());
        KeyPair keyPair = gen.generateKeyPair();
        uk = keyPair.getPublic();
        rk = keyPair.getPrivate();
    }

    public final String myhash(byte[] by) throws NoSuchAlgorithmException {
        byte[] output1;
        // MD5
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(by);
        output1 = md5.digest();
        // create hex output
        StringBuilder hexString1 = new StringBuilder();
        for (int i = 0; i < md5.digest().length; i++)
            hexString1.append(Integer.toHexString(0xFF & output1[i]));
        return hexString1.toString();
    }

    private static byte[] encrypt(String text, PrivateKey priRSA) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, priRSA);
        return cipher.doFinal(text.getBytes());
    }

    public static String encrypt(String text) {
        try {
            return byte2hex(encrypt(text, rk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, uk);
        return cipher.doFinal(src);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp;
        for (byte aB : b) {
            stmp = Integer.toHexString(aB & 0xFF);
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
        }
        return hs.toUpperCase();
    }

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

}
