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
//    public int rssiScore;

    public int[] secScores; //int[5] 생성자
    public int secLevel;

    private final int RSSI_HIGH = -60;
    private final int RSSI_LOW = -70;
    private final int SECURE_HIGH = 200;
    private final int SECURE_LOW = 100;

    public ApItem(String name, String capabilities, int rssi, int eapType) {
        this.apName = name;
        this.capabilities = capabilities;
        this.rssi = rssi;
        this.eapType = eapType;
        SecurityEstimater securityEstimater = new SecurityEstimater(name, capabilities, eapType);

        // 신호강도 레벨 책정
        if(this.rssi > RSSI_HIGH){
            this.rssiLevel = 3;
        }
        else if(this.rssi > RSSI_LOW){
            this.rssiLevel = 2;
        }
        else {
            this.rssiLevel = 1;
        }

        // 신호강도 점수 책정
//        this.rssiScore = rssi; // score 대신 rssi 값 그대로 사용. 수정 필요

        // 보안 점수 책정
        this.secScores = securityEstimater.getScores();
//        this.secScores = {140,90,50,0,0};
        Log.d("TEST add", "ApItem: sec store 지정");

        // 보안 레벨 책정
        if(this.secScores[0] > SECURE_HIGH){
            this.secLevel = 3;
        }
        else if(this.secScores[0] > SECURE_LOW){
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
//    public int getRssiScore(){
//        return this.rssiScore;
//    }

    public int getSecScore(int index){
        return this.secScores[index];
    }
    public int getSecLevel(){
        return this.secLevel;
    }
}