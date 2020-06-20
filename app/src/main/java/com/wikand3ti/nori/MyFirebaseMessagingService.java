package com.wikand3ti.nori;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //writeToFile(token);
//        try {
//            sendRegistrationToServer(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        writeFile(token);
    }

    public void writeToPreferences(String token){
//        SharedPreferences shardpref = getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
    }

    private boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Yes, it is writable");
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }


    public void writeFile(String data){
        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File dir = new File(Environment.getExternalStorageDirectory()+"/nori");
            if(!dir.exists()){
                dir.mkdirs();
            }
            try{
                File textFile = new File(Environment.getExternalStorageDirectory()+"/nori", "noriconfig.txt");
                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(data.getBytes());
                fos.close();

                Log.i("WRITE","Token written to "+textFile.getAbsolutePath());
            }catch (Exception e){
                e.printStackTrace();
            }

            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Log.i("WRITE","Cannot Write to External Storage");
        }
    }

    public void writeToFile(String data)
    {
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/noriconfig/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "config.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void sendRegistrationToServer(String token) throws Exception {

        String url = "https://nori-api.web.app/notif/submitVapid?id_pengguna="+token+"&token="+token;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Request header
        con.setRequestProperty("User-Agent", "USER_AGENT");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }
}