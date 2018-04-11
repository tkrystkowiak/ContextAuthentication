package com.tomaszkrystkowiak.contextauthentication;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivitiesIntentService extends IntentService {
    private static final String TAG = "ActivitiesIntentService";
    public ActivitiesIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent i = new Intent("ACTION");
        DetectedActivity detectedActivity = result.getMostProbableActivity();
        i.putExtra("EXTRA",detectedActivity);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
