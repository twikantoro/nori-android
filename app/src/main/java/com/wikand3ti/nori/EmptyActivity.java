package com.wikand3ti.nori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class EmptyActivity extends AppCompatActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        ActivityCompat.requestPermissions(EmptyActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.i("PERMISSION","WRITE GRANTED");
        } else {
            Log.i("PERMISSION","WRITE DECLINED");
        }
        Intent activityMain = new Intent(context, MainActivity.class);
        startActivity(activityMain);
    }
}