package com.tomaszkrystkowiak.contextauthentication;

import android.content.Context;
import android.media.AudioManager;

public class AudioSetMeter {

    private Context mContext;
    private AudioManager mAM;

    AudioSetMeter(Context mContext){
        this.mContext = mContext;
    }

    public void start(){
        mAM = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getHeadphones(){
        if(mAM.isWiredHeadsetOn()){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int getSpeakerphone(){
        if(mAM.isSpeakerphoneOn()){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int getPhoneMode(){
        int mode = -1;
        switch(mAM.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                mode = 0;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                mode = 1;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                mode = 2;
                break;
        }
        return mode;
    }

}
