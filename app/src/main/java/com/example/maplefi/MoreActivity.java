package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;

import com.example.maplefi.databinding.ActivityMoreBinding;
import com.example.maplefi.ui.MoreViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MoreActivity extends AppCompatActivity {
    private ActivityMoreBinding binding;
    private MoreViewModel mmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mmodel = new MoreViewModel(intent.getStringExtra("AP_NAME"),intent.getStringExtra("AP_SEC_SCORE"),intent.getStringExtra("AP_SPEED"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        binding.setMmodel(mmodel);
        mmodel.onCreate();
    }
}
