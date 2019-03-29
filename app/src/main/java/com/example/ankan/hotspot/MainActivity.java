package com.example.ankan.hotspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    final int REQUEST_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPermissionFromDevice()){
            requestPermission();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOnHotspot(View view) {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                Log.i("TAG", "Wifi Hotspot is on now");
                mReservation = reservation;

               String Pss= reservation.getWifiConfiguration().preSharedKey;
               String S=reservation.getWifiConfiguration().SSID;

                Toast.makeText(MainActivity.this,Pss+" "+S,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Log.d("TAG", "onStopped: ");
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Log.d("TAG", "onFailed: ");
            }
        }, new Handler());
    }

    public void turnOffHotspot(View view) {
        if (mReservation != null) {
            mReservation.close();
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE

        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    private boolean checkPermissionFromDevice(){
        int access_coarse_result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fine_coarse_result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int wifi = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        int cwifi = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);
        return access_coarse_result == PackageManager.PERMISSION_GRANTED && fine_coarse_result==PackageManager.PERMISSION_GRANTED && wifi==PackageManager.PERMISSION_GRANTED && cwifi==PackageManager.PERMISSION_GRANTED;
    }
}
