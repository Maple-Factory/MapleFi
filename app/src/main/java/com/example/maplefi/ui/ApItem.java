package com.example.maplefi.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class ApItem {
    public String ap_name = "";
    private ImageButton moreinf;
    private ImageButton ap_connect;
    private Drawable ap_state;
//    public ObservableField<String> ap_name = new ObservableField<>();
//    public int rssi;
//    public int sec_level;

    public ApItem(String name) {
        this.ap_name = name;
//        ap_name.set("");
//        rssi = 0;
//        sec_level = 0;
    }

//    public void setName(String name){
//        ap_name = name;
//    }


    public void setAp_state(Drawable ap_icon){
        ap_state = ap_icon;
    }
    //public void setMoreinf()
    //public void setAp_connect()

    public String getItem_ssid(){
        return this.ap_name;
    }
    public Drawable getAp_state(){
        return this.ap_state;
    }
//    public ImageButton getMoreinf(){
//        return this.moreinf;
//    }
//    public ImageButton getAp_connect(){
//        return this.ap_connect;
//    }
}
