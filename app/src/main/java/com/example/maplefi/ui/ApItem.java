package com.example.maplefi.ui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.maplefi.util.SecurityEstimater;

import java.io.Serializable;

public class ApItem implements Serializable {
    private String ap_name;
    private String capabilities;
    private int rssi;
    public int eap_type;

    public int rssi_level;
    public int rssi_score;

    public int[] sec_score;//int[5] 생성자
    public int sec_level;

    private final int RSSI_HIGH = -60;
    private final int RSSI_LOW = -70;
    private final int SECURE_HIGH = 200;
    private final int SECURE_LOW = 150;

    public ApItem(String name, String capabilities, int rssi, int eap_type) {
        this.ap_name = name;
        this.capabilities = capabilities;
        this.rssi = rssi;
        this.eap_type = eap_type;
        SecurityEstimater securityEstimater = new SecurityEstimater(name, capabilities, eap_type);

        // 신호강도 레벨 책정
        if(this.rssi > RSSI_HIGH){
            this.rssi_level = 3;
        }
        else if(this.rssi > RSSI_LOW){
            this.rssi_level = 2;
        }
        else {
            this.rssi_level = 1;
        }

        // 신호강도 점수 책정
        this.rssi_score = rssi; // score 대신 rssi 값 그대로 사용. 수정 필요

        // 보안 점수 책정
        this.sec_score = securityEstimater.getScore();
//        this.sec_score = {140,90,50,0,0};
        Log.d("TEST add", "ApItem: sec store 지정");

        // 보안 레벨 책정
        if(this.sec_score[0] > SECURE_HIGH){
            this.sec_level = 3;
        }
        else if(this.sec_score[0] > SECURE_LOW){
            this.sec_level = 2;
        }
        else {
            this.sec_level = 1;
        }
    }

    public void setName(String name){
        ap_name = name;
    }

    public String getSsid(){
        return this.ap_name;
    }
    public int getRssi(){
        return this.rssi;
    }
    public String getCaps(){
        return this.capabilities;
    }
    public int getEapType(){ return this.eap_type;}

    public int getRssiLevel(){
        return this.rssi_level;
    }
    public int getRssiScore(){
        return this.rssi_score;
    }

    public int getSecScore(int index){
        return this.sec_score[index];
    }
    public int getSecLevel(){
        return this.sec_level;
    }

}