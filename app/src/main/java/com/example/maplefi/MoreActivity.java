package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maplefi.ui.ApItem;

public class MoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        Intent intent = getIntent();
        ApItem ap_item = (ApItem) intent.getSerializableExtra("AP_ITEM");

        final TextView textViewSsid = (TextView) findViewById(R.id.tv_ssid);
        textViewSsid.setText(ap_item.getSsid());

        final ImageView imgViewLight = (ImageView) findViewById(R.id.img_color);
        int secure_level = ap_item.getSecLevel();
        switch (secure_level){
            case 1:
                imgViewLight.setImageResource(R.drawable.red);
                break;
            case 2:
                imgViewLight.setImageResource(R.drawable.orange);
                break;
            case 3:
                imgViewLight.setImageResource(R.drawable.green);
                break;
            default:
                imgViewLight.setImageResource(R.drawable.red);
        }
        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);
        int rssi_level = ap_item.getRssiLevel();
        switch (rssi_level){
            case 1:
                imgRssi.setImageResource(R.drawable.wifi_1);
                break;
            case 2:
                imgRssi.setImageResource(R.drawable.wifi_2);
                break;
            case 3:
                imgRssi.setImageResource(R.drawable.wifi_3);
                break;
            default:
                imgRssi.setImageResource(R.drawable.wifi_x);
        }

        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        imgButtonConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wifiUtil.connect("","","");
            }
        });

        // -------------------------------------------------------------//
        final TextView textViewCap1 = (TextView) findViewById(R.id.tv_cap1);
        textViewCap1.setText("CAP1 : ");

        final TextView textViewCap2 = (TextView) findViewById(R.id.tv_cap2);
        textViewCap2.setText("CAP2 : ");

        final TextView textViewCap3 = (TextView) findViewById(R.id.tv_cap3);
        textViewCap3.setText("CAP3 : ");

        final TextView textViewCap4 = (TextView) findViewById(R.id.tv_cap4);
        textViewCap4.setText("CAP4 : ");


        final TextView textViewSecScore = (TextView) findViewById(R.id.tv_sec_score);
        textViewSecScore.setText(Integer.toString(ap_item.getSecScore())+" 점");

//        final ImageView imgViewLightSet = (ImageView) findViewById(R.id.img_sec_score);
//        imgViewLight.setImageResource(R.drawable.);

        final TextView textViewSpeedScore = (TextView) findViewById(R.id.tv_speed_score);
        textViewSpeedScore.setText(Integer.toString(ap_item.getRssiScore()) + " dBm");

        ImageButton imgButtonDetail = (ImageButton) findViewById(R.id.imgb_detail) ;

//        final TextView textViewSecScore = (TextView) findViewById(R.id.tv_sec_score);
//        textViewSecScore.setText("TEST eap type:"+Integer.toString(ap_item.getEapType()));
//        textViewSecScore.setText(Integer.toString(ap_item.getSecScore()));
//        final TextView textViewSpdScore = (TextView) findViewById(R.id.tv_speed_score);
//        textViewSpdScore.setText("TEST caps:"+ap_item.getCaps());
//        textViewSpdScore.setText(Integer.toString(ap_item.getRssiScore()));

        final ImageView imgDetailDash = (ImageView) findViewById(R.id.dash_detail);
        imgDetailDash.setVisibility(View.INVISIBLE);

        final LinearLayout linearLayoutDetail = (LinearLayout) findViewById(R.id.detailLayout);
        linearLayoutDetail.setVisibility(View.INVISIBLE);

        final TextView textViewReport = (TextView) findViewById(R.id.tv_detail_desc);

        //보안방식에 따른 레포트
        //TODO : 없는거 제외하곤 report set text 제대로 안됨. 수정요함
        if(ap_item.getCaps().contains("WEP")){
            Log.d("test", "onCreate: wep");
            textViewReport.setText(getString(R.string.wep_report));
            Log.d("test", "onCreate: "+textViewReport.toString());
        }else if(ap_item.getCaps().contains("WPA")){
            Log.d("test", "onCreate: wpa");
            if(ap_item.getCaps().contains("TKIP")){
                Log.d("test", "onCreate: wpa-tkip");
                textViewReport.setText(getString(R.string.wpa_report) + " " + getString(R.string.tkip_report));
                Log.d("test", "onCreate: "+textViewReport.toString());
            }else if(ap_item.getCaps().contains("PSK")){
                Log.d("test", "onCreate: wpa-psk");
                textViewReport.setText(getString(R.string.wpa_report) + "" + getString(R.string.psk_report));
                Log.d("test", "onCreate: "+textViewReport.toString());
            }
        }else if(ap_item.getCaps().contains("WPA2")){
            Log.d("test", "onCreate: wpa2");
            textViewReport.setText(getString(R.string.wpa2_report));
            Log.d("test", "onCreate: "+textViewReport.toString());
            if(ap_item.getCaps().contains("EAP")){
                Log.d("test", "onCreate: eap");
                textViewReport.setText(getString(R.string.eap_report));
                Log.d("test", "onCreate: "+textViewReport);
            }
        }else{
            Log.d("test", "onCreate: else");
            textViewReport.setText(getString(R.string.none_report));
            Log.d("test", "onCreate: "+textViewReport.toString());
        }

        imgButtonDetail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearLayoutDetail.getVisibility() == View.VISIBLE){
                    linearLayoutDetail.setVisibility(View.INVISIBLE);
                    imgDetailDash.setVisibility(View.INVISIBLE);
                }
                else {
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    imgDetailDash.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
