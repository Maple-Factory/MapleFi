package com.example.maplefi.ui;

import android.util.Log;

import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.WifiUtil;

public class MainViewModel extends BaseViewModel {
    public WifiUtil wifiUtil;
    private MainActivityNavigator navigator;

    public MainViewModel(MainActivityNavigator navigator, WifiUtil wifiUtil) {
        this.navigator = navigator;
        this.wifiUtil = wifiUtil;
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

    public void btnOnoffClick() {
        Log.d("TEST","ONOFF BUTTON");
        if(MainViewModel.this.wifiUtil.isWifiEnabled()) {
            MainViewModel.this.wifiUtil.setWifiEnabled(false);
        }
        else {
            MainViewModel.this.wifiUtil.setWifiEnabled(true);
        }
    }

    public void callMoreActivity() {
        navigator.callMoreActivity();
    }
}
