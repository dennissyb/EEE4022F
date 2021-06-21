package com.example.thethakeyboard2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.params.InputConfiguration;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ThethaKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard special_keyboard;
    private boolean isCaps = false;
    private int keyboardState;
    private InputMethodManager inputMethodManager;
    private int currentToken;
    private static final String TAG = ThethaKeyboard.class.getSimpleName();


    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.querty, R.integer.keyboard_normal);
        special_keyboard = new Keyboard(this, R.xml.querty, R.integer.keyboard_symbol);
        kv.setKeyboard(keyboard);
        keyboardState = R.integer.keyboard_normal;
        kv.setOnKeyboardActionListener(this);

        return kv;
    }

    public void senWords(String words)
    {

    }

    public void SwitchKeyBoardView () {
        if(kv != null) {
            if(keyboardState == R.integer.keyboard_normal){
                //change to symbol keyboard
                if(special_keyboard== null){
                    special_keyboard = new Keyboard(this, R.xml.querty, R.integer.keyboard_symbol);
                }

                kv.setKeyboard(special_keyboard);
                keyboardState = R.integer.keyboard_symbol;
            } else {
                if(keyboard== null){
                    keyboard = new Keyboard(this, R.xml.querty, R.integer.keyboard_normal);
                }

                kv.setKeyboard(keyboard);
                keyboardState = R.integer.keyboard_normal;
            }
            //no shifting
            kv.setShifted(false);
        }

    }
    @Override
    public void onPress(int primaryCode) {
    }

//    public void sendPost() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                    conn.setRequestProperty("Accept","application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//
//                    JSONObject jsonParam = new JSONObject();
//                    // JSONArray
//                    //Checkout Tutorial http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
//                    jsonParam.put("timestamp", 1488873360);
//                    jsonParam.put("latitude", 0D);
//                    jsonParam.put("longitude", 0D);
//
//                    Log.i("JSON", jsonParam.toString());
//                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//                    os.writeBytes(jsonParam.toString());
//
//                    os.flush();
//                    os.close();
//
//                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
//                    Log.i("MSG" , conn.getResponseMessage());
//
//                    conn.disconnect();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int i, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch(i)
        {
            case Keyboard.KEYCODE_MODE_CHANGE:
                SwitchKeyBoardView();
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();

                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));
                makeTheCall();
                break;
            default:
                char code = (char)i;
                if(Character.isLetter(code) && isCaps)
                    code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code),i);

        }



    }

    private void makeTheCall (){
        HttpHandler handler = new HttpHandler();
        handler.httpServiceCall("Hello There, Data from android app");
    }

    private void playClick(int i) {

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(i)
        {
            case 32 :
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10 :
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);

        }

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}