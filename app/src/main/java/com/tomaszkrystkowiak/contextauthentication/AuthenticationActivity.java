package com.tomaszkrystkowiak.contextauthentication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String TAG = "AuthenticationActivity";
    private LightMeter lightMeter;
    private AudioSetMeter audioSetMeter;
    private int[] conditions = new int[7];
    private Weather weather;
    private GoogleApiClient mGoogleApiClient;
    private ActivityDetectionBroadcastReceiver mBroadcastReceiver;
    private SoundMeter soundMeter = new SoundMeter();
    private TextView authentication;
    private TextView test;
    private ArrayList noiseList;
    private RuleReader ruleReader;
    String[] mPermission = {Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        noiseList = new ArrayList();
        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .addApi(Awareness.API)
                .build();
        lightMeter = new LightMeter(this);
        audioSetMeter = new AudioSetMeter(this);
        authentication = (TextView)findViewById(R.id.authenticationView);
        test = (TextView)findViewById(R.id.textView4);
        ruleReader = new RuleReader(this);
        ruleReader.start();

        noiseMeasuring();
        measuring();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("ACTION"));
        lightMeter.start();
        audioSetMeter.start();
        try {
            soundMeter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        removeActivityUpdates();
        lightMeter.stop();
        soundMeter.stop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        weatherUpdate();
        requestActivityUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(@NonNull Status status) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 3 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED) {

            }
        }

    }

    protected void testing() {
        int liczbaKontrolna = 0;
        for (int a = 0; a <= 5; a++) {
            conditions[0] = a;
            for (int b = 0; b <= 1; b++) {
                conditions[1] = b;
                for (int c = 0; c <= 1; c++) {
                    conditions[2] = c;
                    for (int d = 0; d <= 2; d++) {
                        conditions[3] = d;
                        for (int e = 0; e <= 1; e++) {
                            conditions[4] = e;
                            for (int f = 0; f <= 1; f++) {
                                conditions[5] = f;
                                for (int g = 0; g <= 2; g++) {
                                    conditions[6] = g;
                                    liczbaKontrolna++;
                                    System.out.println("Wektor nr: "+liczbaKontrolna);
                                    System.out.println("Stan tabeli conditions:"+"\n"+
                                            "Acivity: "+conditions[0]+"\n"+
                                            "Speakerphone: "+conditions[1]+"\n"+
                                            "Headphones: "+conditions[2]+"\n"+
                                            "Mode: "+conditions[3]+"\n"+
                                            "Noise: "+conditions[4]+"\n"+
                                            "Temperature: "+conditions[5]+"\n"+
                                            "Light: "+conditions[6]+"\n"+
                                            "__________________________________");
                                    setMethod2();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setMethod2(){
        for(int i = 0;i < ruleReader.rules.length;i++){
            int liczbaKontrolna1 = 0;
            int liczbaKontrolna2 = 0;
            for(int j=0;j<7;j++){
                if(ruleReader.rules[i][j]!=-1){
                    liczbaKontrolna1++;
                    if (ruleReader.rules[i][j]==conditions[j]){
                        liczbaKontrolna2++;
                    }
                }
            }
            if(liczbaKontrolna1!=0 && liczbaKontrolna1==liczbaKontrolna2){
                System.out.println(ruleReader.odczytTabeli(i));
                    switch(ruleReader.rules[i][7]){
                    case 0:
                        authentication.setText("Voice Authentication.");
                        break;
                    case 1:
                        authentication.setText("Fingerprint Authentication.");
                        break;
                    case 2:
                        authentication.setText("Password Authentication.");
                        break;
                    case 4:
                        authentication.setText("Face Authentication.");
                        break;
                }
                test.setText(ruleReader.odczytTabeli(i));
                break;
            }
        }
    }

    private void setMethod(){
        for(int i = 0;i < ruleReader.rules.length;i++){
            int liczbaKontrolna1 = 0;
            int liczbaKontrolna2 = 0;
            for(int j=0;j<7;j++){
                if(ruleReader.rules[i][j]!=-1){
                    liczbaKontrolna1++;
                    if (ruleReader.rules[i][j]==conditions[j]){
                        liczbaKontrolna2++;
                    }
                }
            }
            if(liczbaKontrolna1!=0 && liczbaKontrolna1==liczbaKontrolna2){
                System.out.println("Metoda autentykacji nr: "+ruleReader.rules[i][7]);
                switch(ruleReader.rules[i][7]){
                    case 0:
                        authentication.setText("Voice Authentication.");
                        break;
                    case 1:
                        authentication.setText("Fingerprint Authentication.");
                        break;
                    case 2:
                        authentication.setText("Password Authentication.");
                        break;
                    case 4:
                        authentication.setText("Face Authentication.");
                        break;
                }
                test.setText(ruleReader.odczytTabeli(i));
                break;
            }
        }
    }

    private void noiseMeasuring(){
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                noiseList.add(soundMeter.getAmplitude());
                double suma = 0;
                double srednia;
                for(int i=0; i< noiseList.size(); i++)
                {
                    double amp = (double) noiseList.get(i);
                    suma = suma + amp;
                }
                srednia = suma/(noiseList.size());
                Log.i(TAG, "Sredni poziom hałasu =" + srednia);
                if (srednia<=5000){
                    conditions[4] = 0;
                }
                else{
                    conditions[4] = 1;
                }
                if(noiseList.size()>5){
                    noiseList.remove(0);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void weatherUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (!weatherResult.getStatus().isSuccess()) {
                            Log.i(TAG, "Błąd odczytywania pogody");
                        }
                        weather = weatherResult.getWeather();
                        double temp = weather.getTemperature(2);
                        if(temp <= 0){
                            conditions[5] = 0;
                        }
                        else{
                            conditions[5] = 1;
                        }
                    }
                });
    }

    private void checkLight() {
        int lx;
        lx = lightMeter.getLightLevel();
        conditions[6] = lx;
    }

    private void checkAudioSet(){
        //słuchawki
        conditions[2] = audioSetMeter.getHeadphones();
        //głośnik
        conditions[1] = audioSetMeter.getSpeakerphone();
        //tryb
        conditions[3] = audioSetMeter.getPhoneMode();
    }

    private void measuring(){
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                weatherUpdate();
                checkAudioSet();
                checkLight();
                setMethod();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void requestActivityUpdates() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "GoogleApiClient not yet connected", Toast.LENGTH_SHORT).show();
        } else {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 0, getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    public void removeActivityUpdates() {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, getActivityDetectionPendingIntent()).setResultCallback(this);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DetectedActivity detectedActivity = intent.getParcelableExtra("EXTRA");
            conditions[0] = detectedActivity.getType();
        }
    }
}
