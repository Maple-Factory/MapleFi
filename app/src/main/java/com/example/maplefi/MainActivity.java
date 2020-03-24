package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.maplefi.databinding.ActivityMainBinding;
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
        startActivity(new Intent(getApplicationContext(), MoreActivity.class));

    }
}
