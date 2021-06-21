package com.example.thethakeyboard2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Dennis Sibiya on 24/05/2021.
 */
public class HttpHandler extends AsyncTask<Void,Void,Void> {

    private static final String TAG = HttpHandler.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList;
    Context context;

    public HttpHandler() {
    }

    public boolean checkConnection ()
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        boolean isMetered = false;
            if (isConnected) {
                isMetered = cm.isActiveNetworkMetered();
            }
        return true;
    }

    public void httpServiceCall(String words) {
        StringBuffer response = null;
       String reqUrl = "https://larmas.nlp-ebe-uct.com/Dictionary/add_text_from_keyboard";

        try {
            JSONObject postData = new JSONObject();
            postData.put("", "hello there, words from android");

            JSONArray jsonArray = new JSONArray();

            jsonArray.put(postData);

            JSONObject textObject = new JSONObject();
            textObject.put("text", jsonArray);

            URL url = new URL(reqUrl);

            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept","application/json");
            System.out.println("Connecting to server .....");
            connection.connect();


            //connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            //connection.setRequestProperty("Connection", "Keep-Alive");
            //Request
            System.out.println("connected");

            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );

           /// OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
            System.out.println("ere");

            writer.flush();
            writer.close();

            //Response
            System.out.println("0");

            InputStream is = connection.getInputStream();
            System.out.println("1");

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            System.out.println("2");

            String line;
            response = new StringBuffer();
            //Expecting answer of type JSON single line {"json_items":[{"status":"OK","message":"<Message>"}]}
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            System.out.println(response.toString()+"\n");
          //  connection.disconnect(); // close the connection after usage

        } catch (Exception e){
            System.out.println(this.getClass().getSimpleName() +" "+ e.getMessage());
        }


//        HttpURLConnection urlConnection = null;
//        if (checkConnection()) {
//            try {
//                JSONObject postData = new JSONObject();
//                postData.put("", "hello there, words from android");
//
//                JSONArray jsonArray = new JSONArray();
//
//                jsonArray.put(postData);
//
//                JSONObject textObject = new JSONObject();
//                textObject.put("text", jsonArray);
//
//                URL url = new URL(reqUrl);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty( "charset", "utf-8");
//                urlConnection.setUseCaches( false );
//                urlConnection.setInstanceFollowRedirects( false );
//
//                Log.i("Connection", "Connected");
//
//
//                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
//                    Log.i("data", wr.toString());
//                }
//
//                // read the response
//                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//                Log.i("Connection", out.toString());
//
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                        out, "UTF-8"));
//                writer.write(postData.toString());
//                writer.flush();
//
//                int code = urlConnection.getResponseCode();
//                if (code != 201) {
//                    throw new IOException("Invalid response from server: " + code);
//                }
//
//                BufferedReader rd = new BufferedReader(new InputStreamReader(
//                        urlConnection.getInputStream()));
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    Log.i("data", line);
//                }
//            } catch (MalformedURLException e) {
//                Log.e(TAG, "MalformedURLException: " + e.getMessage());
//            } catch (ProtocolException e) {
//                Log.e(TAG, "ProtocolException: " + e.getMessage());
//            } catch (IOException e) {
//                Log.e(TAG, "IOException: " + e.getMessage());
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
//        if (urlConnection != null) {
//            urlConnection.disconnect();
//        }
    }

    private String convertStreamToString (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

        @Override
        protected Void doInBackground(Void... voids) {
          return null;
        }

}