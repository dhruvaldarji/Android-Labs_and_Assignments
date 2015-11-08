package com.ddarji.lab11.keymanager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyManagerActivity extends AppCompatActivity {

    // Components for Main Page
    ImageButton imgBtn1;
    ImageButton imgBtn2;
    ImageButton imgBtn3;

    // Components for Asymmetric Sub application
    ImageButton abtn1;
    Button genAsymBtn;
    EditText priET;
    EditText pubET;
    private final static String RSA = "RSA";
    private final static String AES = "AES";
    public static PublicKey uk = null;
    public static PrivateKey rk = null;
    RadioGroup encryptRG;
    RadioGroup decryptRG;
    EditText orgMsgET;
    Button beginBtn;
    TextView warnTV;
    EditText enReET;
    EditText deReET;

    // Components for Symmetric Sub application
    ImageButton sbtn1;
    Button genSymBtn;
    Button beginSymBtn;
    EditText secretET;
    EditText orgMsgSymET;
    TextView warnSymTV;
    SecretKey sk = null;
    EditText enReSymET;
    EditText deReSymET;

    // Components for Diffie-Hellman Sub application
    ImageButton ebtn1;
    Button stepBtn;
    int step;
    TextView joshTV;
    TextView jackTV;
    EditText pET;
    EditText gET;
    EditText aET;
    EditText bET;
    TextView warningTV;
    TextView keyTV;
    BigInteger A, B, p, g, a, b, S1, S2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imgBtn1 = (ImageButton) findViewById(R.id.imgBtn1);
        imgBtn1.setOnClickListener(new ClickEvent());
        imgBtn2 = (ImageButton) findViewById(R.id.imgBtn2);
        imgBtn2.setOnClickListener(new ClickEvent());
        imgBtn3 = (ImageButton) findViewById(R.id.imgBtn3);
        imgBtn3.setOnClickListener(new ClickEvent());
    }

    /*
    * layoutSwitch function is used to switch layouts among Main Page,
    * Symmetric Sub-application, Asymmetric Sub application, and Diffie-HellmanSub application.
    */
    @SuppressLint("SetTextI18n")
    void layoutSwitch(String where) {
        // load components for each layout before switching
        if (Objects.equals(where, "ASYM")) {
            setContentView(R.layout.asym);
            abtn1 = (ImageButton) findViewById(R.id.abtn1);
            abtn1.setOnClickListener(new ClickEvent());
            genAsymBtn = (Button) findViewById(R.id.genAsymBtn);
            genAsymBtn.setOnClickListener(new ClickEvent());
            priET = (EditText) findViewById(R.id.priET);
            pubET = (EditText) findViewById(R.id.pubET);
            priET.setEnabled(false);
            pubET.setEnabled(false);
            // encryptRG = (RadioGroup)findViewById(R.id.encryptRG);
            // decryptRG = (RadioGroup)findViewById(R.id.decryptRG);
            orgMsgET = (EditText) findViewById(R.id.orgMsgET);
            beginBtn = (Button) findViewById(R.id.beginBtn);
            beginBtn.setOnClickListener(new ClickEvent());
            warnTV = (TextView) findViewById(R.id.warnTV);
            enReET = (EditText) findViewById(R.id.enReET);
            deReET = (EditText) findViewById(R.id.deReET);
        } else if (Objects.equals(where, "SYM")) {
            setContentView(R.layout.sym);
            sbtn1 = (ImageButton) findViewById(R.id.sbtn1);
            sbtn1.setOnClickListener(new ClickEvent());
            genSymBtn = (Button) findViewById(R.id.genSymBtn);
            genSymBtn.setOnClickListener(new ClickEvent());
            secretET = (EditText) findViewById(R.id.secretET);
            orgMsgSymET = (EditText) findViewById(R.id.orgMsgSymET);
            beginSymBtn = (Button) findViewById(R.id.beginSymBtn);
            beginSymBtn.setOnClickListener(new ClickEvent());
            warnSymTV = (TextView) findViewById(R.id.warnSymTV);
            enReSymET = (EditText) findViewById(R.id.enReSymET);
            deReSymET = (EditText) findViewById(R.id.deReSymET);
        } else if (Objects.equals(where, "MAIN")) {
            setContentView(R.layout.main);
            imgBtn1 = (ImageButton) findViewById(R.id.imgBtn1);
            imgBtn1.setOnClickListener(new ClickEvent());
            imgBtn2 = (ImageButton) findViewById(R.id.imgBtn2);
            imgBtn2.setOnClickListener(new ClickEvent());
            imgBtn3 = (ImageButton) findViewById(R.id.imgBtn3);
            imgBtn3.setOnClickListener(new ClickEvent());
        } else if (Objects.equals(where, "EXCHANGE")) {
            setContentView(R.layout.exchange);
            ebtn1 = (ImageButton) findViewById(R.id.ebtn1);
            ebtn1.setOnClickListener(new ClickEvent());
            stepBtn = (Button) findViewById(R.id.stepBtn);
            stepBtn.setOnClickListener(new ClickEvent());
            stepBtn.setText("Begin Step by Step Key Exchange");
            step = 0;
            joshTV = (TextView) findViewById(R.id.joshTV);
            joshTV.setText("");
            jackTV = (TextView) findViewById(R.id.jackTV);
            jackTV.setText("");
            pET = (EditText) findViewById(R.id.pET);
            pET.setEnabled(true);
            gET = (EditText) findViewById(R.id.gET);
            gET.setEnabled(true);
            aET = (EditText) findViewById(R.id.aET);
            aET.setEnabled(true);
            bET = (EditText) findViewById(R.id.bET);
            bET.setEnabled(true);
            warningTV = (TextView) findViewById(R.id.warningTV);
            warningTV.setText("");
            keyTV = (TextView) findViewById(R.id.keyTV);
            keyTV.setText("");
        }
    }

    // hex2byte function is used to convert hex to byte array
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

    // byte2hex function is used to convert byte array to hex
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp;
        for (byte aB : b) {
            stmp = Integer.toHexString(aB & 0xFF);
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
            Log.e("byte:", "0" + stmp);
        }
        return hs.toUpperCase();
    }

    // generateKey function is used to generate Asymmetric key pair, public key and private key
    public void generateKey() {
        KeyPairGenerator gen;
        try {
            gen = KeyPairGenerator.getInstance(RSA);
            gen.initialize(64, new SecureRandom());
            KeyPair keyPair = gen.generateKeyPair();
            uk = keyPair.getPublic();
            rk = keyPair.getPrivate();
            priET.setText(byte2hex(rk.getEncoded()));
            pubET.setText(byte2hex(uk.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // generateSymKey function is used to generate Symmetric key, the secret key.
    public void generateSymKey() {
        KeyGenerator gen;
        try {
            gen = KeyGenerator.getInstance("AES");
            gen.init(128, new SecureRandom());
            sk = gen.generateKey();
            secretET.setText(byte2hex(sk.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // encrypt function is used to perform encryption using Public key
    private static byte[] encrypt(String text, PublicKey pubRSA) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubRSA);

        Log.d("DEBUG: ASYM Encrypt", "Text: " + text + ", Bytes: " + text.getBytes());

        return cipher.doFinal(text.getBytes());
    }

    // encrypt function is used to catch exception if there is an error with the encryption
    public static String encrypt(String text, PublicKey kk, int i) {
        try {
            return byte2hex(encrypt(text, kk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // encryptSym function is used to encrypt message using secret key
    private static byte[] encryptSym(String text, SecretKey pubRSA) throws Exception {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, pubRSA);
        return cipher.doFinal(text.getBytes());
    }

    // encryptSym function is used to catch exception if there is error with the encryption
    public static String encryptSym(String text, SecretKey kk, int i) {
        try {
            Log.e("result:", byte2hex(encryptSym(text, kk)));
            return byte2hex(encryptSym(text, kk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decrypt function is used to catch exception when there is an error with decryption
    public static String decrypt(String data, PrivateKey kk, int i) {
        try {
            Log.d("DEBUG: ASYM Decrypt", "Data: "+data+", Bytes: "+data.getBytes());
            return new String(decrypt(hex2byte(data.getBytes()), kk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decrypt function is used to perform the real description with Private key
    private static byte[] decrypt(byte[] src, PrivateKey kk) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, kk);
        return cipher.doFinal(src);
    }

    // decryptSym function is ued to catch exception when there is an error with decryption
    public static String decryptSym(String data, SecretKey kkk, int i) {
        try {
            return new String(decryptSym(hex2byte(data.getBytes()), kkk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decryptSym function is used to perform real decryption with Secret key
    private static byte[] decryptSym(byte[] src, SecretKey kk) throws Exception {
        @SuppressLint("GetInstance") Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, kk);
        return c.doFinal(src);
    }

    /*
     * checkBeforeEncrypt function is used to make sure every parameter for
     * encryption has been ready. (Asymmetric method)
     */
    @SuppressLint("SetTextI18n")
    public boolean checkBeforeEncrypt() {
        if (orgMsgET.getText().toString().equals("")) {
            warnTV.setText("Warning: Please Enter Original Message!");
            warnTV.setTextColor(Color.rgb(255, 0, 0));
            return false;
        } else if ((uk == null) || (rk == null)) {
            warnTV.setText("Warning: Please Generate Key First!");
            warnTV.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        warnTV.setText("");
        return true;
    }

    /*
     * beginEncryptAndDecrypt function is used to get Text and generate
     * encrypted result and decrypted result. (Asymmetric method)
     */
    public void beginEncryptAndDecrypt() {
        if (!checkBeforeEncrypt()) {
            return;
        }

        String encryptText;
        encryptText = encrypt(orgMsgET.getText().toString(), uk, 0);
        enReET.setText(encryptText);

        String decryptText;
        decryptText = decrypt(encryptText, rk, 0);
        deReET.setText(decryptText);
    }

    /*
     * checkBeforeEncrypt function is used to make sure every parameter for
     * encryption has been ready. (Symmetric method)
     */
    @SuppressLint("SetTextI18n")
    public boolean checkSymBeforeEncrypt() {
        if (orgMsgSymET.getText().toString().equals("")) {
            warnSymTV.setText("Warning: Please Enter Original Message!");
            warnSymTV.setTextColor(Color.rgb(255, 0, 0));
            return false;
        } else if (sk == null) {
            warnSymTV.setText("Warning: Please Generate Key First!");
            warnSymTV.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        warnSymTV.setText("");
        return true;
    }

    /*
     * beginSymEncryptAndDecrypt function is used to get Text and generate
     * encrypted result and decrypted result. (Symmetric method)
     */
    public void beginSymEncryptAndDecrypt() {
        if (!checkSymBeforeEncrypt()) {
            return;
        }

        String encryptText;
        encryptText = encryptSym(orgMsgSymET.getText().toString(), sk, 0);
        enReSymET.setText(encryptText);

        String decryptText;
        decryptText = decryptSym(encryptText, sk, 0);
        deReSymET.setText(decryptText);
    }

    /*
     * checkKeyExchangeParam function is used to make sure all parameters ready for Diffie-Hellman method.
     */
    @SuppressLint("SetTextI18n")
    public boolean checkKeyExchangeParam() {
        if (pET.getText().toString().equals("")
                || gET.getText().toString().equals("")
                || gET.getText().toString().equals("")
                || gET.getText().toString().equals("")) {
            warningTV.setText("Warning: Please check the parameters!");
            warningTV.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        warningTV.setText("");
        return true;
    }

    // updateStatus function is used to perform each step of Diffie-Hellman key exchange.
    @SuppressLint("SetTextI18n")
    public void updateStatus() {
        if (!checkKeyExchangeParam()) {
            return;
        }
        switch (step) {
            case 0: {
                stepBtn.setText("Next");
                joshTV.setText("Current Hold: p = " + pET.getText().toString()
                        + ", g = " + gET.getText().toString() + ", a = "
                        + aET.getText().toString() + "." + "\n"
                        + "Action: Send p,g to Jack.");
                jackTV.setText("Current Hold:" + "b =" + bET.getText().toString()
                        + ".");
                p = new BigInteger(pET.getText().toString().replaceAll(" ", ""));
                g = new BigInteger(gET.getText().toString().replaceAll(" ", ""));
                a = new BigInteger(aET.getText().toString().replaceAll(" ", ""));
                b = new BigInteger(bET.getText().toString().replaceAll(" ", ""));
                A = g.modPow(a, p);
                B = g.modPow(b, p);
                S1 = B.modPow(a, p);
                S2 = A.modPow(b, p);
                step++;
                break;
            }
            case 1: {
                joshTV.setText("Caculation: A = g^a mod p. \n"
                        + "Current Hold: p = " + pET.getText().toString()
                        + ", g = " + g.toString() + ", a = " + a.toString()
                        + ", A = " + A.toString() + "." + "\n"
                        + "Action: Caculate A and Send A to Jack.");
                jackTV.setText("Current Hold: b = " + b.toString() + ", p = "
                        + p.toString() + ", g = " + g.toString() + ".");
                step++;
                break;
            }
            case 2: {
                joshTV.setText("Current Hold: p = " + p.toString() + ", g = "
                        + g.toString() + ", a = " + a.toString() + ", A = "
                        + A.toString() + ".");
                jackTV.setText("Caculation: B = g^b mod p. \n"
                        + "Current Hold: p = " + p.toString() + ", g = "
                        + g.toString() + ", a = " + a.toString() + ", A = "
                        + A.toString() + ", B = " + B.toString() + ".");
                step++;
                break;
            }
            case 3: {
                joshTV.setText("Caculation: S = B^a mod p. \n"
                        + "Current Hold: p = "
                        + p.toString()
                        + ", g = "
                        + g.toString()
                        + ", a = "
                        + a.toString()
                        + ", A = "
                        + A.toString()
                        + ", B = "
                        + B.toString()
                        + ", S = "
                        + S1.toString()
                        + "."
                        + "\n"
                        + "Action: Caculate S(The secret key between josh and jack.)");
                jackTV.setText("Caculation: S = A^b mod p. \n"
                        + "Current Hold: p = "
                        + p.toString()
                        + ", g = "
                        + g.toString()
                        + ", a = "
                        + a.toString()
                        + ", A = "
                        + A.toString()
                        + ", B = "
                        + B.toString()
                        + ", S = "
                        + S2.toString()
                        + "."
                        + "\n"
                        + "Action: Caculate S(The secret key between josh and jack.)");
                keyTV.setText("Secret Key S = " + S1.toString());
                step++;
                break;
            }
            default: {
                step = 0;
                stepBtn.setText("Begin Step by Step Key Exchange");
                keyTV.setText("");
                break;
            }
        }
    }

    // class ClickEvent is used to react to any actions of mouse clicking
    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == imgBtn1) {
                layoutSwitch("ASYM");
            } else if (v == imgBtn2) {
                layoutSwitch("SYM");
            } else if (v == abtn1) {
                layoutSwitch("MAIN");
            } else if (v == sbtn1) {
                layoutSwitch("MAIN");
            } else if (v == ebtn1) {
                layoutSwitch("MAIN");
            } else if (v == genAsymBtn) {
                generateKey();
            } else if (v == beginBtn) {
                beginEncryptAndDecrypt();
            } else if (v == genSymBtn) {
                generateSymKey();
            } else if (v == beginSymBtn) {
                beginSymEncryptAndDecrypt();
            } else if (v == imgBtn3) {
                layoutSwitch("EXCHANGE");
            } else if (v == stepBtn) {
                updateStatus();
            }
        }
    }

}
