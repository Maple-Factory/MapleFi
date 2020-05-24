package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maplefi.ui.ApItem;
import com.example.maplefi.util.ListAdapterOld;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.SecurityEstimater;
import com.example.maplefi.util.WifiUtil;
import com.github.zagum.switchicon.SwitchIconView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    final public int PASSWORD_POPUP_ACTIVITY = 1;
    public int passwordTryNetId = -1;

    public WifiUtil wifiUtil;
    ListAdapterOld adapter = null;
    private ApItem nowApItem = null;
    private ArrayList<ApItem> apItems = new ArrayList<ApItem>();

    public SecurityEstimater securityEstimater;

    // NOW AP
    TextView nowSsidTextView;
    ImageView nowSecColorImgView;
    ImageView nowRssiImgView;
    ImageView nowMoreinfoImgButton;
    SwitchIconView nowConnectImgButton;
    ImageView scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        wifiUtil = new WifiUtil(getApplicationContext(), this);

        // - 사이드바 요소
        // ON OFF 버튼
        final SwitchIconView onoffButton = (SwitchIconView) findViewById(R.id.btn_onoff) ;
        if(wifiUtil.isWifiEnabled()) onoffButton.setIconEnabled(true);
        else onoffButton.setIconEnabled(false);
        onoffButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.setWifiEnabled(false);
                    onoffButton.setIconEnabled(false);
                    updateNowAp();
                    apItems.clear();
                    adapter.notifyDataSetChanged();
                }
                else {
                    wifiUtil.setWifiEnabled(true);
                    onoffButton.setIconEnabled(true);
                    updateNowAp();
                }
            }
        });
        // Scan 버튼 (임시)
        scanButton = findViewById(R.id.btn_scan) ;
        scanButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("scanButton", "onClick: 스캔버튼 클릭");
                // AP 스캔
                updateNowAp();  // 테스트 용도
                apItems.clear();
                adapter.notifyDataSetChanged();
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.scan();
                    Log.d("scanButton", "onClick: 와이파이 유틸 스캔 완료");

                    List<ScanResult> wifiList = wifiUtil.getScanResults();
                    Log.d("scanButton", "onClick: 리스트생성");
                    for (ScanResult scanResult : wifiList) {
//                        Log.d("scanButton", "onClick: 포문");
//                        Log.d("TEST",scanResult.toString());
                        if(scanResult.SSID.equals(""))
                            scanResult.SSID = "숨겨진 네트워크";   // 임시로 숨겨진 ap 단순히 처리. 수정 필요
                        else if(nowApItem != null)
                            if(scanResult.SSID.equals(nowApItem.getSsid()))
                                continue;
                        addItem(scanResult.SSID, scanResult.capabilities, scanResult.level,
                                Integer.parseInt(wifiUtil.parseEapType(scanResult.toString())));
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi가 꺼져있습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // - 연결된 AP 아이템 요소
        final TextView ssidTextView = (TextView) findViewById(R.id.tv_ssid);
        final ImageView colorImgView = (ImageView) findViewById(R.id.img_secColor);
        final ImageView rssiImgView = (ImageView) findViewById(R.id.img_strDegree);
        ImageView moreinfoImgButton = (ImageView) findViewById(R.id.imgb_moreinfo) ;
        SwitchIconView connectImgButton = (SwitchIconView) findViewById(R.id.imgb_connect) ;
        nowSsidTextView = ssidTextView;
        nowSecColorImgView = colorImgView;
        nowRssiImgView = rssiImgView;
        nowMoreinfoImgButton = moreinfoImgButton;
        nowConnectImgButton = connectImgButton;

        // - AP 리스트뷰 요소
        // 리사이클러뷰 리니어 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.recycleView_wifiListOld);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 리스트 어뎁더 객체 지정
        adapter = new ListAdapterOld(this, apItems, new ListAdapterOld.OnApItemClickListener() {
            @Override
            public void onMoreBtnClick(View v, int position) {
                // ApItem More Button Click Listener
                ApItem ap_item = apItems.get(position);
                callMoreActivity(ap_item);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onConBtnClick(View v, int position) {
                // ApItem Connect Button Click Listener
                ApItem apItem = apItems.get(position);
                connection(apItem.getSsid(), apItem.getCaps());

                adapter.notifyDataSetChanged();
                updateNowAp();
            }
//            // item 클릭 이벤트
//            @Override
//            public void onItemClick(View v, int position) {
////                Log.d("test", "onItemClick: 아이템 클릭");
//            }
        });
        recyclerView.setAdapter(adapter);

        // - 메인 액티비티 실행 내용
        updateNowAp();

        // - 테스트 샘플
//        addItem("TEST SAMPLE","[WPA-PSK-CCMP+TKIP][WPA2-PSK-CCMP+TKIP][WPS][ESS]",-50,-1);
//        addItem("IPTIME","[WEP][ESS]",-80);

        WifiReceiver receiverWifi = new WifiReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(receiverWifi, mIntentFilter);
    }

    public void addItem(String itemSsid, String capabilities, int rssi, int eapType){
        ApItem item = new ApItem(itemSsid, capabilities, rssi, eapType);
        Log.d("TEST add", "addItem: " + itemSsid);
        apItems.add(item);
        adapter.notifyDataSetChanged();
    }

    public void updateNowAp(){
        // 텀을 가진 후 업데이트 하는 로직 삭제 후 주기적 업데이트로 수정 필요
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                boolean changedNowApSsid = false;
                Log.d("TEST","Update Now AP");
                WifiInfo wifiInfo = wifiUtil.getConnectionInfo();
                if(wifiInfo.getNetworkId() != -1) {
                    Log.d("TEST","Update Ap, try_net_id [net_id:" + Integer.toString(wifiInfo.getNetworkId()) + "]");
                    passwordTryNetId = wifiInfo.getNetworkId();

                    String bssid = wifiInfo.getBSSID();
                    String ssid = wifiInfo.getSSID().replace("\"", "");
                    nowApItem = new ApItem(ssid, wifiUtil.getCapabilities(bssid),
                                           wifiInfo.getRssi(), wifiUtil.ssidToEap(ssid));
                }
                else {
                    nowApItem = null;
                }

                if(nowApItem == null){
                    nowSsidTextView.setText("연결된 와이파이가 없습니다.");
                    nowRssiImgView.setImageResource(R.drawable.wifi_x);
                    nowSecColorImgView.setImageResource(R.drawable.gray);
                    nowConnectImgButton.setIconEnabled(false);
                    nowMoreinfoImgButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    nowConnectImgButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else {
                    if(!nowSsidTextView.getText().equals(nowApItem.getSsid()))
                        changedNowApSsid = true;
                    nowSsidTextView.setText(nowApItem.getSsid());
                    int secLevel = nowApItem.getSecLevel();
                    switch (secLevel){
                        case 1:
                            nowSecColorImgView.setImageResource(R.drawable.red);
                            break;
                        case 2:
                            nowSecColorImgView.setImageResource(R.drawable.orange);
                            break;
                        case 3:
                            nowSecColorImgView.setImageResource(R.drawable.green);
                            break;
                        default:
                            nowSecColorImgView.setImageResource(R.drawable.gray);
                    }
                    int rssiLevel = nowApItem.getRssiLevel();
                    switch (rssiLevel){
                        case 0:
                        case 1:
                            nowRssiImgView.setImageResource(R.drawable.wifi_1);
                            break;
                        case 2:
                            nowRssiImgView.setImageResource(R.drawable.wifi_2);
                            break;
                        case 3:
                        case 4:
                            nowRssiImgView.setImageResource(R.drawable.wifi_3);
                            break;
                        default:
                            nowRssiImgView.setImageResource(R.drawable.wifi_x);
                    }
                    nowMoreinfoImgButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callMoreActivity(nowApItem);
                            adapter.notifyDataSetChanged(); // 필요한지 확인 필요
                        }
                    });
                    nowConnectImgButton.setIconEnabled(true, false);
                    nowConnectImgButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wifiUtil.disconnect();  // 임시로 단순 연결 해제 기능으로 사용. 수정 필요
                            nowConnectImgButton.setIconEnabled(false);
                            updateNowAp();
                        }
                    });
                }
                if(changedNowApSsid) {
                    Log.d("TEST","changedNowApSsid");
                    scanButton.performClick();
                }
                else {
                    Log.d("TEST","Not ChangedNowApSsid");
                }
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    public void connection(String ssid, String capabilities){
        // Profile Check
        if(wifiUtil.getProfileId(ssid) == -1){
            if(wifiUtil.isNeedPassword(capabilities)){
                // Get Password
                askPassword(ssid, capabilities);
            }
            else {
                // No Password New Connect
                int netId = wifiUtil.addProfile(ssid, capabilities);

                boolean isConnected;
                isConnected = wifiUtil.connect(netId);
                if(!isConnected)
                    wifiUtil.removeProfile(netId);
            }
        }
        else {  // Already Ap Profile
            Log.d("TEST"," Already Ap Profile ---------");
            boolean isConnected;
            isConnected = wifiUtil.connect(ssid);
            if(!isConnected) {
                Log.d("TEST"," RemoveProfile ---------");
                wifiUtil.removeProfile(ssid);
                Log.d("TEST"," RemoveProfile End ---------");
            }
        }
    }

    public void askPassword(String ssid, String capabilities) {
        Intent intent = new Intent(this, PasswdPopupActivity.class);
        intent.putExtra("ssid", ssid);
        intent.putExtra("capabilities", capabilities);

        startActivityForResult(intent, PASSWORD_POPUP_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PASSWORD_POPUP_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Log.d("TEST","RESULT_OK");
                String ssid = data.getStringExtra("ssid");
                String capabilities = data.getStringExtra("capabilities");
                String password = data.getStringExtra("password");

                if(!password.equals("")) {
                    int netId = wifiUtil.addProfile(ssid, capabilities, password);
                    if(netId != -1) {
                        Log.d("TEST", "Try Password [netId:" + Integer.toString(netId) + "]");
                        passwordTryNetId = netId;

                        boolean isConnected;
                        isConnected = wifiUtil.connect(netId);
                        if(!isConnected)
                            wifiUtil.removeProfile(netId);
                        updateNowAp();
                        Log.d("TEST", ssid + " " + capabilities + " " + password);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "패스워드의 길이가 부족합니다.", Toast.LENGTH_SHORT);  // Capabilities 에 맞는 비번 가이드라인 제시 추가 필요
                        toast.show();
                        updateNowAp();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "패스워드가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    updateNowAp();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void callMoreActivity(ApItem apItem) {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_ITEM", apItem);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            Log.d("TEST", "onReceive() in WifiReceiver");
            String action  = intent.getAction();
            if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
                Log.d("TEST", "Check SUPPLICANT_STATE_CHANGED_ACTION");
                SupplicantState suplState = ((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                switch(suplState){
                    case ASSOCIATED:Log.i("SupplicantState", "ASSOCIATED");
                        break;
                    case ASSOCIATING:Log.i("SupplicantState", "ASSOCIATING");
                        break;
                    case AUTHENTICATING:Log.i("SupplicantState", "Authenticating...");
                        break;
                    case COMPLETED:Log.i("SupplicantState", "Connected");
                        break;
                    case DISCONNECTED:Log.i("SupplicantState", "Disconnected");
                        break;
                    case DORMANT:Log.i("SupplicantState", "DORMANT");
                        break;
                    case FOUR_WAY_HANDSHAKE:Log.i("SupplicantState", "FOUR_WAY_HANDSHAKE");
                        break;
                    case GROUP_HANDSHAKE:Log.i("SupplicantState", "GROUP_HANDSHAKE");
                        break;
                    case INACTIVE:Log.i("SupplicantState", "INACTIVE");
                        break;
                    case INTERFACE_DISABLED:Log.i("SupplicantState", "INTERFACE_DISABLED");
                        break;
                    case INVALID:Log.i("SupplicantState", "INVALID");
                        break;
                    case SCANNING:Log.i("SupplicantState", "SCANNING");
                        break;
                    case UNINITIALIZED:Log.i("SupplicantState", "UNINITIALIZED");
                        break;
                    default:Log.i("SupplicantState", "Unknown");
                        break;
                }

                int suplError = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(suplError == WifiManager.ERROR_AUTHENTICATING){
                    Log.d("TEST", "[ERROR] ERROR_AUTHENTICATING!");

                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi 패스워드가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();

                    Log.d("TEST","############# passwordTryNetId : " + Integer.toString(passwordTryNetId));
                    if(passwordTryNetId != -1) {
                        wifiUtil.removeProfile(passwordTryNetId);
                        passwordTryNetId = -1;
                    }
                }
            }   // if Closed
        }   // onReceive Closed
    }   // WifiReceiver Class Closed
}
