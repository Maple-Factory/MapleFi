package com.example.maplefi.ui;

import android.util.Log;

import com.example.maplefi.util.SecurityEstimater;

import java.io.Serializable;

public class ApItem implements Serializable {
    private String apName;
    private String capabilities;
    private int rssi;
    public int eapType;

    public int rssiLevel;
    public int rssiScore;

    public int secScore; //int[5] 생성자
    public int[] secScores;
    public int secLevel;

    private final int RSSI_SCORE_ONE = 75;
    private final int RSSI_SCORE_TWO = 50;
    private final int RSSI_SCORE_THREE = 25;
    private final int SECURE_HIGH = 200;
    private final int SECURE_LOW = 100;
    private final int RSSI_ADD = 85;
    private final double RSSI_MUT = 2.5;

    public ApItem(String name, String capabilities, int rssi, int eapType) {
        this.apName = name;
        this.capabilities = capabilities;
        this.rssi = rssi;
        this.eapType = eapType;
        SecurityEstimater securityEstimater = new SecurityEstimater(name, capabilities, eapType);

        // 신호강도 점수 책정
        this.rssiScore = (int) ((double)(this.rssi + RSSI_ADD) * RSSI_MUT);
        this.rssiScore = this.rssiScore > 100 ? 100 : (this.rssiScore < 0 ? 0 : this.rssiScore);

        // 신호강도 레벨 책정
        if(this.rssiScore == 100){
            this.rssiLevel = 4;
        }
        else if(this.rssiScore > RSSI_SCORE_ONE){
            this.rssiLevel = 3;
        }
        else if(this.rssiScore > RSSI_SCORE_TWO){
            this.rssiLevel = 2;
        }
        else if(this.rssiScore > RSSI_SCORE_THREE){
            this.rssiLevel = 1;
        }
        else {
            this.rssiLevel = 0;
        }

        // 보안 점수 책정
        this.secScore = securityEstimater.getFinal();
        this.secScores = securityEstimater.getScores();
        Log.d("TEST add", "ApItem: sec store 지정");

        // 보안 레벨 책정
        if(this.secScore > SECURE_HIGH){
            this.secLevel = 3;
        }
        else if(this.secScore > SECURE_LOW){
            this.secLevel = 2;
        }
        else {
            this.secLevel = 1;
        }
    }

    public void setName(String name){
        apName = name;
    }

    public String getSsid(){
        return this.apName;
    }
    public int getRssi(){
        return this.rssi;
    }
    public String getCaps(){
        return this.capabilities;
    }
    public int getEapType(){ return this.eapType;}

    public int getRssiLevel(){
        return this.rssiLevel;
    }
    public int getRssiScore(){
        return this.rssiScore;
    }

    public int getSecScores(int index){
        return this.secScores[index];
    }
    public int getSecLevel(){
        return this.secLevel;
    }
}