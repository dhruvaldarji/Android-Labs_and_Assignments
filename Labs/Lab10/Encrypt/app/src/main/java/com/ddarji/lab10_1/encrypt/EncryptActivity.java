package com.ddarji.lab10_1.encrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class EncryptActivity extends AppCompatActivity {

    private final static String RSA = "RSA";
    public static PublicKey uk;
    public static PrivateKey rk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            generateKey();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button enButton = (Button) findViewById(R.id.enbutton);
        final Button deButton = (Button) findViewById(R.id.debutton);
        final EditText input = (EditText) findViewById(R.id.Input);
        final EditText Raw = (EditText) findViewById(R.id.raw);
        final EditText output = (EditText) findViewById(R.id.OriginText);
        enButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    Raw.setText(encrypt(input.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        });

        deButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    output.setText(String.valueOf(decrypt(Raw.getText().toString())));
                } catch (Exception e) {
                    e.printStackTrace();
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

    private static byte[] encrypt(String text, PublicKey pubRSA) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubRSA);
        return cipher.doFinal(text.getBytes());
    }

    public static String encrypt(String text) {
        try {
            return byte2hex(encrypt(text, uk));
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
        cipher.init(Cipher.DECRYPT_MODE, rk);
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
