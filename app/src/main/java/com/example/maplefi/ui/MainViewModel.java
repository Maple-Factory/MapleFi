package com.example.maplefi.ui;

import android.util.Log;

import com.example.maplefi.R;
import com.example.maplefi.databinding.ActivityMainBinding;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.WifiUtil;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import java.util.ArrayList;

public class MainViewModel extends BaseViewModel {
    public WifiUtil wifiUtil;
    private MainActivityNavigator navigator;

//    public final ObservableField<ApItem> now_ap_item;
    public final ApItem now_ap_item;
//    public final ObservableArrayList<ApItem> ap_item_list;


    public MainViewModel(MainActivityNavigator navigator, WifiUtil wifiUtil) {
        this.navigator = navigator;
        this.wifiUtil = wifiUtil;

//        now_ap_item = new ObservableField<>(new ApItem("Free"));
        now_ap_item = new ApItem("Free");

//        ap_item_list = new ObservableArrayList<>();
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

    public void btnMoreClick() {
        Log.d("TEST","More BUTTON");
        callMoreActivity();
    }

    public void callMoreActivity() {
        navigator.callMoreActivity();
    }
}
