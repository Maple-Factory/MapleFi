package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        imgButtonConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wifiUtil.connect("","","");
            }
        });

        final TextView textViewSecScore = (TextView) findViewById(R.id.sec_score_tv);
        textViewSecScore.setText("TEST eap type:"+Integer.toString(ap_item.getEapType()));
//        textViewSecScore.setText(Integer.toString(ap_item.getSecScore()));
        final TextView textViewSpdScore = (TextView) findViewById(R.id.speed_score_tv);
        textViewSpdScore.setText("TEST caps:"+ap_item.getCaps());
//        textViewSpdScore.setText(Integer.toString(ap_item.getRssiScore()));
        TextView textViewReport = (TextView) findViewById(R.id.report_tv);
        //보안방식에 따른 레포트
        //TODO : 없는거 제외하곤 report set text 제대로 안됨. 수정요함
        if(ap_item.getCaps().contains("WEP")){
            Log.d("test", "onCreate: wep");
            textViewReport.setText(R.string.wep_report);
            Log.d("test", "onCreate: "+textViewReport.toString());
        }else if(ap_item.getCaps().contains("WPA")){
            Log.d("test", "onCreate: wpa");
            if(ap_item.getCaps().contains("TKIP")){
                Log.d("test", "onCreate: wpa-tkip");
                textViewReport.setText((R.string.wpa_report+"" +
                        +R.string.tkip_report).toString());
                Log.d("test", "onCreate: "+textViewReport.toString());
            }else if(ap_item.getCaps().contains("PSK")){
                Log.d("test", "onCreate: wpa-psk");
                textViewReport.setText((R.string.wpa_report+"" +
                        +R.string.psk_report).toString());
                Log.d("test", "onCreate: "+textViewReport.toString());
            }
        }else if(ap_item.getCaps().contains("WPA2")){
            Log.d("test", "onCreate: wpa2");
            textViewReport.setText(R.string.wpa2_report);
            Log.d("test", "onCreate: "+textViewReport.toString());
            if(ap_item.getCaps().contains("EAP")){
                Log.d("test", "onCreate: eap");
                textViewReport.setText(R.string.eap_report);
                Log.d("test", "onCreate: "+textViewReport);
            }
        }else{
            Log.d("test", "onCreate: else");
            textViewReport.setText(R.string.none_report);
            Log.d("test", "onCreate: "+textViewReport.toString());
        }

    }
}
