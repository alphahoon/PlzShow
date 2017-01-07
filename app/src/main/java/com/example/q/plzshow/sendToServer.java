package com.example.q.plzshow;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by q on 2017-01-07.
 */

public class sendToServer {

    public sendToServer() {
        super();
    }

    public void send(final JSONObject obj){
        new Thread() {
            public void run(){
                try {
                    URL url = new URL("http://52.78.200.87:3000");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestProperty("Content-Length", Integer.toString(obj.toString().getBytes().length));

                    connection.setDoInput(true);
                    connection.setDoOutput(true);


                    Log.d("cs496_server", obj+"");
                    Log.d("cs496_server2", obj.toString());
                    Log.d("cs496_server3", obj.toString().getBytes()+"");


                    OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                    outputStream.write(obj.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    JSONObject json = new JSONObject();
                    StringBuffer response = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append("\n");
                    }
                    reader.close();
                    json = new JSONObject(response.toString());
                    Log.d("Response", json+"");

                    connection.disconnect();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Thread.currentThread().interrupt();
            }
        }.start();
    }
}
