package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        Intent intent = getIntent();

        final TextView textViewSsid = (TextView) findViewById(R.id.tv_ssid);
        textViewSsid.setText(intent.getStringExtra("AP_NAME"));

        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);

        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        imgButtonMoreinf.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        imgButtonConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wifiUtil.connect("","","");
            }
        });

        final TextView textViewSecScore = (TextView) findViewById(R.id.sec_score_tv);
        textViewSecScore.setText(intent.getStringExtra("AP_SEC_SCORE"));
        final TextView textViewSpdScore = (TextView) findViewById(R.id.speed_score_tv);
        textViewSpdScore.setText(intent.getStringExtra("AP_SPEED"));

    }
}
