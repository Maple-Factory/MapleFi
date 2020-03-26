package com.example.maplefi.ui;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class ApItem {
    public String ap_name = "";
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
}
