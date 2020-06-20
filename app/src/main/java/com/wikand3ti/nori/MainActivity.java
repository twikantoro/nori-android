package com.wikand3ti.nori;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  private static Context context;
  private Bundle instate;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //this.instate = savedInstanceState;

    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      // Ex: add(TotallyAwesomePlugin.class);
    }});

    if(isStoragePermissionGranted(MainActivity.this)){
      // Initializes the Bridge
      fcmGetToken();
    } else {
      // Show an activity that
      // if the user has granted permission, should restart the application
//      Intent intent = new Intent(this, EmptyActivity.class);
//      startActivity(intent);
    }
    Log.i("STORAGE","LOCATION:"+Environment.getExternalStorageDirectory()+"/nori");
  }

  public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
      Log.i("PERMISSION","WRITE GRANTED");
    } else {
      Log.i("PERMISSION","WRITE DECLINED");
//      Intent intent = new Intent(this, MainActivity.class);
//      startActivity(intent);
    }
//    Intent activityMain = new Intent(context, MainActivity.class);
//    startActivity(activityMain);
    fcmGetToken();
  }

  public void fcmGetToken(){
    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
      @Override
      public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
          Log.w("TAG", "getInstanceId failed", task.getException());
          return;
        }

        // Get new Instance ID token
        String token = task.getResult().getToken();

        // Log and toast
        Log.i("TAG","FCM token"+token);
        writeFile(token);
      }
    });
  }

  public boolean isStoragePermissionGranted(Activity activity){
    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
      return true;
    } else {
      ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
      return false;
    }
  }

  public void writeFile(String data){

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

  }


}
