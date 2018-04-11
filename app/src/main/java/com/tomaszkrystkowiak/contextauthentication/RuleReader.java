package com.tomaszkrystkowiak.contextauthentication;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class RuleReader {
    private Context mContext;
    private String [] sensors = new String[8];
    public int [][] rules = new int[70][8];
    private  int [] actualSensors = new int[8];
    RuleReader(Context mContext){
        this.mContext = mContext;
    }
    public void start(){
        inicjalizacjaTabeli();
        initializeSensors();
        odczyt();

    }


    public void initializeSensors(){
        sensors[0] = "activity";
        sensors[1] = "speakerphone";
        sensors[2] = "headphones";
        sensors[3] = "mode";
        sensors[4] = "noise";
        sensors[5] = "temperature";
        sensors[6] = "light";
        sensors[7] = "authentication";
    }

    public void odczyt() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("rules.txt")));

            int nrZasady = 0;
            String text;
            while ((text = reader.readLine()) != null) {
                int a = Integer.valueOf(String.valueOf(text.charAt(text.length() - 1)));
                String test3 = String.valueOf(a);
                String sensor = text.substring(0, text.length() - 2);
                if (sensor.equals("authentication")) {
                    rules[nrZasady][7] = a;
                    nrZasady++;
                } else {
                    for (int i = 0; i < sensors.length; i++) {
                        if (sensor.equals(sensors[i])) {
                            rules[nrZasady][i] = a;
                            System.out.println("Zapisano wartość " + a + " w [" + nrZasady + "][" + i + "]");
                        }
                    }
                }
            }
            System.out.println("Liczba zasad: "+nrZasady);
        }
        catch (IOException e){

        }

    }
    private void inicjalizacjaTabeli(){
        for(int i = 0;i < rules.length;i++){
            for(int j=0;j<8;j++){
                rules[i][j]= -1;
            }
        }
    }
    public String odczytTabeli(int i){
        StringBuilder sb = new StringBuilder();
        sb.append("Rule "+i+":"+"\n");
            for(int j=0;j<7;j++){
                if(rules[i][j]!=-1) {
                    sb.append(sensors[j]+ ": " + rules[i][j]+"\n");
                }
            }
        return sb.toString();
    }


}
