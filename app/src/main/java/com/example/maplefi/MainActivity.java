package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maplefi.ui.ApItem;
import com.example.maplefi.ui.Apinfo;
import com.example.maplefi.util.ListAdapterOld;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.SecurityEstimater;
import com.example.maplefi.util.WifiUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private WifiUtil wifiUtil;
    ListAdapterOld adapter = null;

    public SecurityEstimater securityEstimater;
    private ArrayList<Apinfo> apinfoList = new ArrayList<Apinfo>();
    private ApItem now_ap_item = null;
    private ArrayList<ApItem> ap_items = new ArrayList<ApItem>();

    // NOW AP
    TextView textViewNowSsid;
    ImageView imgNowRssi;
    ImageButton imgButtonNowMoreinf;
    ImageButton imgButtonNowConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiUtil = new WifiUtil(getApplicationContext(), this);

        // - 사이드바 요소
        // ON OFF 버튼
        final Button buttonOnoff = (Button) findViewById(R.id.btn_onoff) ;
        if(wifiUtil.isWifiEnabled()) buttonOnoff.setText("off");
        else buttonOnoff.setText("on");
        buttonOnoff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.setWifiEnabled(false);
                    buttonOnoff.setText("on");  // to String.xml 리팩토링 필요
                    updateNowAp();
                    ap_items.clear();
                    adapter.notifyDataSetChanged();
                }
                else {
                    wifiUtil.setWifiEnabled(true);
                    buttonOnoff.setText("off"); // to String.xml 리팩토링 필요
                }
            }
        });
        // Scan 버튼 (임시)
        final Button buttonScan = (Button) findViewById(R.id.btn_scan) ;
        buttonScan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //security estimater check 용
                addApinfo("ssid","wpa","psk","ccmp",30);
                addApinfo("","wep","tkip","ccmp",40);
                securityEstimater = new SecurityEstimater(apinfoList);
              
                // AP 스캔
                updateNowAp();  // 테스트 용도
                ap_items.clear();
                adapter.notifyDataSetChanged();
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.scan();
                    List<ScanResult> wifiList = wifiUtil.getScanResults();
                    for (ScanResult scanResult : wifiList) {
                        if(!scanResult.SSID.equals(""))   // 임시로 숨겨진 ap 스킵. 수정 필요
                            addItem(scanResult.SSID, scanResult.capabilities, scanResult.level);
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi가 꺼져있습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // - 연결된 AP 아이템 요소
        final TextView textViewSsid = (TextView) findViewById(R.id.tv_ssid);
        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);
        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        textViewNowSsid = textViewSsid;
        imgNowRssi = imgRssi;
        imgButtonNowMoreinf = imgButtonMoreinf;
        imgButtonNowConnect = imgButtonConnect;

        // - AP 리스트뷰 요소
        // 리사이클러뷰 리니어 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.wifiListOld);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 리스트 어뎁더 객체 지정
        adapter = new ListAdapterOld(ap_items, new ListAdapterOld.OnApItemClickListener() {
            @Override
            public void onMoreBtnClick(View v, int position) {
                // ApItem More Button Click Listener
                ApItem ap_item = ap_items.get(position);
                callMoreActivity(ap_item);

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onConBtnClick(View v, int position) {
                // ApItem Connect Button Click Listener
                ApItem ap_item = ap_items.get(position);
                wifiUtil.connect(ap_item.getSsid(),"",ap_item.getCaps());   // 패스워드 체크 로직 필요
                adapter.notifyDataSetChanged();

                updateNowAp();
            }
        });
        recyclerView.setAdapter(adapter);

        // - 메인 액티비티 실행 내용
        updateNowAp();

        // - 테스트 샘플
//        addItem("CJWIFI_9C1A","[WPA-PSK-CCMP+TKIP]",-50);
//        addItem("IPTIME","[WEP][ESS]",-80);
    }

    @Override
    public void callMoreActivity(ApItem ap_item) {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_ITEM", ap_item);
        startActivity(intent);
    }

    public void addItem(String item_ssid, String capabilities, int rssi){
        ApItem item = new ApItem(item_ssid, capabilities, rssi);
        ap_items.add(item);
        adapter.notifyDataSetChanged();
    }

    public void updateNowAp(){
        // 텀을 가진 후 업데이트 하는 로직 삭제 후 주기적 업데이트로 수정 필요
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("TEST","Update Now AP");
                WifiInfo wifiInfo = wifiUtil.getConnectionInfo();
                if(wifiInfo.getNetworkId() != -1) {
                    String bssid = wifiInfo.getBSSID();
                    now_ap_item = new ApItem(wifiInfo.getSSID().replace("\"", ""), wifiUtil.getCapabilities(bssid), wifiInfo.getRssi());
                }
                else {
                    now_ap_item = null;
                }

                if(now_ap_item == null){
                    textViewNowSsid.setText("연결된 와이파이가 없습니다.");
                    imgNowRssi.setImageResource(R.drawable.wifi_x);
                    imgButtonNowMoreinf.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    imgButtonNowConnect.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                }
                else {
                    textViewNowSsid.setText(now_ap_item.getSsid());
                    int rssi_level = now_ap_item.getRssiLevel();
                    switch (rssi_level){
                        case 1:
                            imgNowRssi.setImageResource(R.drawable.wifi_1);
                            break;
                        case 2:
                            imgNowRssi.setImageResource(R.drawable.wifi_2);
                            break;
                        case 3:
                            imgNowRssi.setImageResource(R.drawable.wifi_3);
                            break;
                        default:
                            imgNowRssi.setImageResource(R.drawable.wifi_x);
                    }
                    imgButtonNowMoreinf.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callMoreActivity(now_ap_item);
                        }
                    });
                    imgButtonNowConnect.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wifiUtil.disconnect();  // 임시로 단순 연결 해제 기능으로 사용. 수정 필요
                            updateNowAp();
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    public  void addApinfo(String ssid, String pwEncType, String packetRule, String packetEncType, int rssi){
        Apinfo info = new Apinfo();
        info.setSsid(ssid);
        info.setPwEncType(pwEncType);
        info.setPacketRule(packetRule);
        info.setPacketEncType(packetEncType);
        info.setRssi(rssi);
        info.setGradeZero();

        apinfoList.add(info);
        for(int i = 0; i < (apinfoList.size()); i++){
            Log.d("debug", "addApinfo: ssid="+apinfoList.get(i).getSsid()+"pwEncType="+apinfoList.get(i).getPwEncType()+"packetRule="+apinfoList.get(i).getProtocolEncType());
        }
    }
}
