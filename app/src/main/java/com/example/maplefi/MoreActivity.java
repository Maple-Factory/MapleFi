package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maplefi.ui.ApItem;
import com.example.maplefi.util.DetailReport;

public class MoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        Intent intent = getIntent();
        ApItem apItem = (ApItem) intent.getSerializableExtra("AP_ITEM");

        final ImageView backButton = (ImageView) findViewById(R.id.btn_back) ;
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // AP 기본 정보
        final TextView ssidTextView = (TextView) findViewById(R.id.tv_ssid);
        ssidTextView.setText(apItem.getSsid());

        final ImageView secColorImgView = (ImageView) findViewById(R.id.img_secColor);
        int secLevel = apItem.getSecLevel();
        switch (secLevel){
            case 1:
                secColorImgView.setImageResource(R.drawable.red);
                break;
            case 2:
                secColorImgView.setImageResource(R.drawable.orange);
                break;
            case 3:
                secColorImgView.setImageResource(R.drawable.green);
                break;
            default:
                secColorImgView.setImageResource(R.drawable.gray);
        }
        final ImageView rssiImgView = (ImageView) findViewById(R.id.img_strDegree);
        int rssiLevel = apItem.getRssiLevel();
        switch (rssiLevel){
            case 0:
            case 1:
                rssiImgView.setImageResource(R.drawable.wifi_1);
                break;
            case 2:
                rssiImgView.setImageResource(R.drawable.wifi_2);
                break;
            case 3:
            case 4:
                rssiImgView.setImageResource(R.drawable.wifi_3);
                break;
            default:
                rssiImgView.setImageResource(R.drawable.wifi_x);
        }

//        SwitchIconView connectImgButton = (SwitchIconView) findViewById(R.id.imgb_connect) ;
//        connectImgButton.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                wifiUtil.disconnect();
//
//
//            }
//        });

        // -------------------------------------------------------------//
        // AP 상세 정보
        final TextView evalItemTextView1 = (TextView) findViewById(R.id.tv_evalItem1);
        evalItemTextView1.setText(" Password : " + apItem.getSecScores(1));
//        textViewCap1.setText("Password : 90");

        final TextView evalItemTextView2 = (TextView) findViewById(R.id.tv_evalItem2);
        evalItemTextView2.setText(" Packet : " + apItem.getSecScores(2));
//        textViewCap2.setText("Packet : 50");

        final TextView evalItemTextView3 = (TextView) findViewById(R.id.tv_evalItem3);
        evalItemTextView3.setText(" Packet snif : " + apItem.getSecScores(3));
//        textViewCap3.setText("Packet snif : 0");

        final TextView evalItemTextView4 = (TextView) findViewById(R.id.tv_evalItem4);
        evalItemTextView4.setText(" Hide : " + apItem.getSecScores(4));
//        textViewCap4.setText("Hide : 0");

        // AP 보안 점수
        final TextView secScoreTextView = (TextView) findViewById(R.id.tv_secScore);
        secScoreTextView.setText(Integer.toString(apItem.getSecScores(0)) + " 점");
//        textViewSecScore.setText("140 점");


        // AP 보안 신호등
        final ImageView imgViewLightSet = (ImageView) findViewById(R.id.img_secColorLight);
        switch (apItem.getSecLevel()){
            case 1:
                imgViewLightSet.setImageResource(R.drawable.light_red);
                break;
            case 2:
                imgViewLightSet.setImageResource(R.drawable.light_orange);
                break;
            case 3:
                imgViewLightSet.setImageResource(R.drawable.light_green);
                break;
            default:
                imgViewLightSet.setImageResource(R.drawable.light_default);
        }

        // AP rssi 레벨
        final ImageView imgViewRssiSet = (ImageView) findViewById(R.id.img_rssiLevel);
        switch (apItem.getRssiLevel()){
            case 1:
                imgViewRssiSet.setImageResource(R.drawable.rssi_1);
                break;
            case 2:
                imgViewRssiSet.setImageResource(R.drawable.rssi_2);
                break;
            case 3:
                imgViewRssiSet.setImageResource(R.drawable.rssi_3);
                break;
            case 4:
                imgViewRssiSet.setImageResource(R.drawable.rssi_full);
                break;
            default:
                imgViewRssiSet.setImageResource(R.drawable.rssi_0);
        }

        // AP 신호 강도 점수
        final TextView strScoreTextView = (TextView) findViewById(R.id.tv_strScore);
        switch (apItem.getRssiLevel()){
            case 0:
            case 1:
            case 2:
                strScoreTextView.setText("나쁨("+apItem.getRssiScore()+")");
                break;
            case 3:
                strScoreTextView.setText("보통("+apItem.getRssiScore()+")");
                break;
            case 4:
                strScoreTextView.setText("좋음("+apItem.getRssiScore()+")");
                break;
            default:
                strScoreTextView.setText("측정 중");
        }

        // AP 디테일보기 버튼
        ImageView detailImgButton = (ImageView) findViewById(R.id.imgb_detail) ;

        // AP 디테일 정보
        final ImageView detailDashImgView = (ImageView) findViewById(R.id.img_dashDetail);
        detailDashImgView.setVisibility(View.INVISIBLE);

        final LinearLayout detailLinearLayout = (LinearLayout) findViewById(R.id.linLayout_detailReport);
        detailLinearLayout.setVisibility(View.INVISIBLE);

        final TextView reportTextView = (TextView) findViewById(R.id.tv_detailReport);
        DetailReport detailReport = new DetailReport(apItem.getSsid(), apItem.getCaps(), apItem.getEapType());
        reportTextView.setText("[상세 리포트]\n" + detailReport.getReport());
//        reportTextView.setText("[DEBUG] " + apItem.getCaps() + "\n" + detailReport.getReport());


        // 디테일 설명 펴기/접기 버튼 이벤트
        detailImgButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailLinearLayout.getVisibility() == View.VISIBLE){
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    detailDashImgView.setVisibility(View.INVISIBLE);
                }
                else {
                    detailLinearLayout.setVisibility(View.VISIBLE);
                    detailDashImgView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
