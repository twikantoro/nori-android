package com.wikand3ti.nori;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  private static Context context;
  private Bundle instate;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.instate = savedInstanceState;

    if(isStoragePermissionGranted(MainActivity.this)){
      // Initializes the Bridge
      this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
        // Additional plugins you've installed go here
        // Ex: add(TotallyAwesomePlugin.class);
      }});
    } else {
      // Show an activity that
      // if the user has granted permission, should restart the application
    }
  }

  public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
      Log.i("PERMISSION","WRITE GRANTED");
    } else {
      Log.i("PERMISSION","WRITE DECLINED");
    }
    Intent mStartActivity = new Intent(context, MainActivity.class);
    int mPendingIntentId = 123456;
    PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
    System.exit(0);
  }

  public boolean isStoragePermissionGranted(Activity activity){
    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
      return true;
    } else {
      ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
      return false;
    }
  }



}
