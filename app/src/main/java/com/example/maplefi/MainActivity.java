package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maplefi.ui.ApItem;
import com.example.maplefi.ui.Apinfo;
import com.example.maplefi.util.ListAdapter;
import com.example.maplefi.util.ListAdapterOld;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.SecurityEstimater;
import com.example.maplefi.util.WifiUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private WifiUtil wifiUtil ;
    ListAdapterOld adapter = null;
    private ArrayList<ApItem> apList = new ArrayList<ApItem>();
    public SecurityEstimater securityEstimater;
    private ArrayList<Apinfo> apinfoList = new ArrayList<Apinfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wifiUtil = new WifiUtil(getApplicationContext(), this);

        setContentView(R.layout.activity_main);

        // Side Bar Elements
        Button buttonOnoff = (Button) findViewById(R.id.btn_onoff) ;
        buttonOnoff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.setWifiEnabled(false);
                }
                else {
                    wifiUtil.setWifiEnabled(true);
                }
            }
        });

        // Now Ap Elements
        final TextView textViewSsid = (TextView) findViewById(R.id.tv_ssid);
        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);

        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        imgButtonMoreinf.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMoreActivity("main-imgbtn-onclick test",1,1);
                //security estimater check 용
                addApinfo("ssid","wpa","psk","ccmp",30);
                addApinfo("","wep","tkip","ccmp",40);
                securityEstimater = new SecurityEstimater(apinfoList);
            }
        });
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        imgButtonConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wifiUtil.connect("","","");
            }
        });

        // Ap List Elements
        // 리사이클러뷰 리니어 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.wifiListOld);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 리스트 어뎁더 객체 지정
        adapter = new ListAdapterOld(apList, new ListAdapterOld.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //버튼 클릭될때 호출됨
                addItem("click test", getDrawable(R.drawable.wifi_full));
                Log.d("debug", "onItemClick: ");  // debug
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

        // item 추가
        addItem("test",getDrawable(R.drawable.wifi_full));
//        Log.d("'debug", "onCreate: additem test1");//debug
        addItem("test2",getDrawable(R.drawable.wifi_full));
//        Log.d("debug", "onCreate: additem test2");//debug
        addItem("test3",getDrawable(R.drawable.wifi_full));
//        Log.d(TAG, "onCreate: additem test3");//debug
        adapter.notifyDataSetChanged();
    }

    @Override
    public void callMoreActivity(String ap_name, int ap_sec_score, int ap_speed) {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_NAME","AP_NAME");
        intent.putExtra("AP_SEC_SCORE","75");
        intent.putExtra("AP_SPEED","7");
        startActivity(intent);
    }

    public void addItem(String item_ssid, Drawable ap_state){
        ApItem item = new ApItem(item_ssid);

        item.setAp_state(ap_state);

        apList.add(item);
//        Log.d("debug", "addItem: "+item_ssid);  //debug
    }

    public  void addApinfo(String ssid, String pwEncType, String protocolEncType, String packetEncType, int rssi){
        Apinfo info = new Apinfo();
        info.setSsid(ssid);
        info.setPwEncType(pwEncType);
        info.setProtocolEncType(protocolEncType);
        info.setPacketEncType(packetEncType);
        info.setRssi(rssi);
        info.setGradeZero();

        apinfoList.add(info);
        for(int i = 0; i < (apinfoList.size()); i++){
            Log.d("debug", "addApinfo: ssid="+apinfoList.get(i).getSsid()+"pwEncType="+apinfoList.get(i).getPwEncType()+"protocolType"+apinfoList.get(i).getProtocolEncType());

        }

    }
}
