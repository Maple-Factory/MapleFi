package com.example.maplefi.ui;

public class MoreViewModel extends BaseViewModel {
    public String ap_name;
    public String ap_sec_score;
    public String ap_speed;

    public MoreViewModel(String name, String score, String speed){
        this.ap_name = name;
        this.ap_sec_score = score;
        this.ap_speed = speed;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onDestroy() {}
}
