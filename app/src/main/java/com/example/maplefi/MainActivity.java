package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private WifiUtil wifiUtil;
    ListAdapterOld adapter = null;
    private ApItem now_ap_item = null;
    private ArrayList<ApItem> ap_items = new ArrayList<ApItem>();

    public SecurityEstimater securityEstimater;
    private ArrayList<Apinfo> apinfoList = new ArrayList<Apinfo>();

    // NOW AP
    TextView textViewNowSsid;
    ImageView imgNowcolor;
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
                    updateNowAp();
                }
            }
        });
        // Scan 버튼 (임시)
        final Button buttonScan = (Button) findViewById(R.id.btn_scan) ;
        buttonScan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //security estimater check 용
                Log.d("scanbutton", "onClick: click");
                addApinfo("ssid","wpa","psk","ccmp",30);
                Log.d("scanbutton", "onClick: appApinfo 1");
                addApinfo("","wep","tkip","ccmp",40);
                Log.d("scanbutton", "onClick: appApinfo 2");

                // AP 스캔
                updateNowAp();  // 테스트 용도
                ap_items.clear();
                adapter.notifyDataSetChanged();
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.scan();

                    List<ScanResult> wifiList = wifiUtil.getScanResults();
                    for (ScanResult scanResult : wifiList) {
                        Log.d("TEST",scanResult.toString());
                        int position = 0;
                        if(!scanResult.SSID.equals(""))   // 임시로 숨겨진 ap 스킵. 수정 필요
                            addItem(scanResult.SSID, scanResult.capabilities, scanResult.level,  Integer.parseInt(wifiUtil.parseEapType(scanResult.toString())));
                            Log.d("TEST", "onClick: before security");
                            securityEstimater = new SecurityEstimater(ap_items, position);
                            //ISSUE : position이 0인 와이파이만 점수가 제대로 나오고 나머진 다 0으로 나옴
//                            ap_items.get(position).setSec_score(SecurityEstimater.getScore(ap_items.get(position)));
                            position++;
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
        final ImageView imgcolor = (ImageView) findViewById(R.id.img_color);
        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);
        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        textViewNowSsid = textViewSsid;
        imgNowcolor = imgcolor;//신호등
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
                connection(ap_item.getSsid(), ap_item.getCaps());

                adapter.notifyDataSetChanged();
                updateNowAp();
            }
            //item 클릭 이벤트
            @Override
            public void onItemClick(View v, int position) {
//                Log.d("test", "onItemClick: 아이템 클릭");
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

    public void addItem(String item_ssid, String capabilities, int rssi, int eap_type){
        ApItem item = new ApItem(item_ssid, capabilities, rssi, eap_type);
        ap_items.add(item);
        adapter.notifyDataSetChanged();
    }

    public void connection(String ssid, String capabilities){
        // Test
//        String TAG = "TEST";
//        Log.d(TAG,"connection debug");
//        WifiManager wifiManager = wifiUtil.getWifiManager();

        // Profile Check
        if(wifiUtil.getProfileId(ssid) == -1){
            if(wifiUtil.isNeedPassword(capabilities)){
                // Get Password
                // ISSUE - Can't Wait Dismiss
//                String password = askPassword();
//                Log.d("TEST","TEST askPassword END......");
//                int net_id = wifiUtil.addProfile(ssid, capabilities, password);
//                wifiUtil.connect(net_id);
            }
            else {
                // No Password New Connect
                int net_id = wifiUtil.addProfile(ssid, capabilities);
                wifiUtil.connect(net_id);
            }
        }
        else {
            wifiUtil.connect(ssid);
        }
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
                    // ISSUE - WifiInfo has not eap_type of toString
                    now_ap_item = new ApItem(wifiInfo.getSSID().replace("\"", ""), wifiUtil.getCapabilities(bssid), wifiInfo.getRssi(), 0); // Integer.parseInt(wifiUtil.parseEapType(wifiInfo.toString())));
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
    public String askPassword(){
        Log.d("TEST","TEST askPassword");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editTextPasswd = new EditText(this);
        editTextPasswd.setText("");
        alert.setView(editTextPasswd);

        alert.setTitle("패스워드 입력");
        alert.setMessage("와이파이 패스워드를 입력해주세요.");

        alert.setPositiveButton("연결", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = editTextPasswd.getText().toString();
                Log.d("TEST","onclick ok " + value);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("TEST","onclick no");
                dialog.dismiss();
            }
        });

        alert.show();

        String password = editTextPasswd.getText().toString();
        Log.d("TEST","END " + password);
        return password;
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
