package com.example.maplefi.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import java.io.Serializable;

public class ApItem implements Serializable {
    private String ap_name;
    private String capabilities;
    private int rssi;

    public int rssi_level;
    public int rssi_score;

    public int sec_score;
    public int sec_level;

    private final int RSSI_HIGH = -60;
    private final int RSSI_LOW = -70;

    public ApItem(String name, String capabilities, int rssi) {
        this.ap_name = name;
        this.capabilities = capabilities;
        this.rssi = rssi;

        if(this.rssi > RSSI_HIGH){
            this.rssi_level = 3;
        }
        else if(this.rssi > RSSI_LOW){
            this.rssi_level = 2;
        }
        else {
            this.rssi_level = 1;
        }

        this.rssi_score = rssi; // score 대신 rssi 값 그대로 사용. 수정 필요
        this.sec_score = 100;
        this.sec_level = 3;
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

    public int getRssiLevel(){
        return this.rssi_level;
    }
    public int getRssiScore(){
        return this.rssi_score;
    }

    public int getSecScore(){
        return this.sec_score;
    }
    public int getSecLevel(){
        return this.sec_level;
    }

}
