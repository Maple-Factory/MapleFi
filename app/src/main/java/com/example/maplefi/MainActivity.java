package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.maplefi.databinding.ActivityMainBinding;
import com.example.maplefi.ui.ApItem;
import com.example.maplefi.ui.MainViewModel;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.WifiUtil;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private ActivityMainBinding binding;
    private MainViewModel model;
    private WifiUtil wifiUtil ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wifiUtil = new WifiUtil(getApplicationContext(), this);
        model = new MainViewModel(this, wifiUtil);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(model);
        model.onCreate();
    }

    @Override
    public void callMoreActivity() {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_NAME",model.now_ap_item.ap_name);
        intent.putExtra("AP_SEC_SCORE","75");
        intent.putExtra("AP_SPEED","7");
        startActivity(intent);
    }
}
