package com.example.maplefi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

        final Button backButton = (Button) findViewById(R.id.btn_back) ;
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
                secColorImgView.setImageResource(R.drawable.red);
        }
        final ImageView rssiImgView = (ImageView) findViewById(R.id.img_strDegree);
        int rssiLevel = apItem.getRssiLevel();
        switch (rssiLevel){
            case 1:
                rssiImgView.setImageResource(R.drawable.wifi_1);
                break;
            case 2:
                rssiImgView.setImageResource(R.drawable.wifi_2);
                break;
            case 3:
                rssiImgView.setImageResource(R.drawable.wifi_3);
                break;
            default:
                rssiImgView.setImageResource(R.drawable.wifi_x);
        }

        ImageButton connectImgButton = (ImageButton) findViewById(R.id.imgb_connect) ;
        connectImgButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wifiUtil.connect("","","");
            }
        });

        // -------------------------------------------------------------//
        // AP 상세 정보
        final TextView evalItemTextView1 = (TextView) findViewById(R.id.tv_evalItem1);
        evalItemTextView1.setText(" Password : " + apItem.getSecScore(1));
//        textViewCap1.setText("Password : 90");

        final TextView evalItemTextView2 = (TextView) findViewById(R.id.tv_evalItem2);
        evalItemTextView2.setText(" Packet : " + apItem.getSecScore(2));
//        textViewCap2.setText("Packet : 50");

        final TextView evalItemTextView3 = (TextView) findViewById(R.id.tv_evalItem3);
        evalItemTextView3.setText(" Packet snif : " + apItem.getSecScore(3));
//        textViewCap3.setText("Packet snif : 0");

        final TextView evalItemTextView4 = (TextView) findViewById(R.id.tv_evalItem4);
        evalItemTextView4.setText(" Hide : " + apItem.getSecScore(4));
//        textViewCap4.setText("Hide : 0");

        // AP 보안 점수
        final TextView secScoreTextView = (TextView) findViewById(R.id.tv_secScore);
        secScoreTextView.setText(Integer.toString(apItem.getSecScore(0)) + " 점");
//        textViewSecScore.setText("140 점");


        // AP 보안 신호등
//        final ImageView imgViewLightSet = (ImageView) findViewById(R.id.img_sec_score);
//        imgViewLight.setImageResource(R.drawable.);

        // AP 신호 강도 점수
        final TextView strScoreTextView = (TextView) findViewById(R.id.tv_strScore);
        switch (apItem.getRssiLevel()){
            case 1:
                strScoreTextView.setText("나쁨");
                break;
            case 2:
                strScoreTextView.setText("중간");
                break;
            case 3:
                strScoreTextView.setText("좋음");
                break;
            default:
                strScoreTextView.setText("측정 중");
        }

        // AP 디테일보기 버튼
        ImageButton detailImgButton = (ImageButton) findViewById(R.id.imgb_detail) ;

        // AP 디테일 정보
        final ImageView detailDashImgView = (ImageView) findViewById(R.id.img_dashDetail);
        detailDashImgView.setVisibility(View.INVISIBLE);

        final LinearLayout detailLinearLayout = (LinearLayout) findViewById(R.id.linLayout_detailReport);
        detailLinearLayout.setVisibility(View.INVISIBLE);

        final TextView reportTextView = (TextView) findViewById(R.id.tv_detailReport);
        DetailReport detailReport = new DetailReport(apItem.getSsid(), apItem.getCaps(), apItem.getEapType());
        reportTextView.setText("[DEBUG] " + apItem.getCaps() + "\n" + detailReport.getReport());
//        reportTextView.setText("인증 및 암호화 방식으로 WPA2를 사용한 것과 암호화 규칙으로 CCMP를 사용한 것은 좋으나, " +
//                               "PSK 키 관리 방식을 사용하여 해커에게 도청당할 위험이 약간 있고, " +
//                               "네트워크 이름이 숨겨져 있지 않아 해커가 접근하기 용이합니다.\n\n" +
//                               "더 나은 보안을 위해서는 와이파이의 네트워크(디바이스) 숨기기 기능을 활성화하시고 " +
//                               "기업의 경우, EAP 키 관리 방식 적용을 추천합니다.\n\n" +
//                               "추천 형태: 숨겨진 네트워크, \n[WPA2-EAP-CCMP] or [WPA2-PSK-CCMP]");

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
