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
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    final public int PASSWORD_POPUP_ACTIVITY = 1;
    public int password_try_net_id = -1;

    public WifiUtil wifiUtil;
    ListAdapterOld adapter = null;
    private ApItem now_ap_item = null;
    private ArrayList<ApItem> ap_items = new ArrayList<ApItem>();

    public SecurityEstimater securityEstimater;

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
                Log.d("buttonScan", "onClick: 스캔버튼 클릭");
                // AP 스캔
                updateNowAp();  // 테스트 용도
                ap_items.clear();
                adapter.notifyDataSetChanged();
                if(wifiUtil.isWifiEnabled()) {
                    wifiUtil.scan();
                    Log.d("buttonScan", "onClick: 와이파이 유틸 슽캔 완료");

                    List<ScanResult> wifiList = wifiUtil.getScanResults();
                    Log.d("buttonScan", "onClick: 리스트생성");
                    for (ScanResult scanResult : wifiList) {
//                        Log.d("buttonScan", "onClick: 포문");
//                        Log.d("TEST",scanResult.toString());
                        if(scanResult.SSID.equals(""))
                            scanResult.SSID = "숨겨진 네트워크";   // 임시로 숨겨진 ap 단순히 처리. 수정 필요
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
        final TextView textViewSsid = (TextView) findViewById(R.id.tv_ssid);
        final ImageView imgcolor = (ImageView) findViewById(R.id.img_color);
        final ImageView imgRssi = (ImageView) findViewById(R.id.img_rssiDegree);
        ImageButton imgButtonMoreinf = (ImageButton) findViewById(R.id.imgb_moreinf) ;
        ImageButton imgButtonConnect = (ImageButton) findViewById(R.id.imgb_connect) ;
        textViewNowSsid = textViewSsid;
        imgNowcolor = imgcolor; // 신호등
        imgNowRssi = imgRssi;
        imgButtonNowMoreinf = imgButtonMoreinf;
        imgButtonNowConnect = imgButtonConnect;

        // - AP 리스트뷰 요소
        // 리사이클러뷰 리니어 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.wifiListOld);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 리스트 어뎁더 객체 지정
        adapter = new ListAdapterOld(this, ap_items, new ListAdapterOld.OnApItemClickListener() {
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
//        addItem("CJWIFI_9C1A","[WPA-PSK-CCMP+TKIP]",-50,-1);
//        addItem("IPTIME","[WEP][ESS]",-80);

        WifiReceiver receiverWifi = new WifiReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(receiverWifi, mIntentFilter);
    }

    public void addItem(String item_ssid, String capabilities, int rssi, int eap_type){
        ApItem item = new ApItem(item_ssid, capabilities, rssi, eap_type);
        Log.d("TEST add", "addItem: "+item_ssid);
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
                    Log.d("TEST","Update Ap, try_net_id [net_id:"+Integer.toString(wifiInfo.getNetworkId())+"]");
                    password_try_net_id = wifiInfo.getNetworkId();

                    String bssid = wifiInfo.getBSSID();
                    String ssid = wifiInfo.getSSID().replace("\"", "");
                    now_ap_item = new ApItem(ssid, wifiUtil.getCapabilities(bssid),
                                             wifiInfo.getRssi(), wifiUtil.ssidToEap(ssid));
                }
                else {
                    now_ap_item = null;
                }

                if(now_ap_item == null){
                    textViewNowSsid.setText("연결된 와이파이가 없습니다.");
                    imgNowRssi.setImageResource(R.drawable.wifi_x);
                    imgNowcolor.setImageResource(R.drawable.gray);
//                    imgButtonNowMoreinf.setOnClickListener(new Button.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
//                    imgButtonNowConnect.setOnClickListener(new Button.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
                }
                else {
                    textViewNowSsid.setText(now_ap_item.getSsid());
                    int sec_level = now_ap_item.getSecLevel();
                    switch (sec_level){
                        case 1:
                            imgNowcolor.setImageResource(R.drawable.red);
                            break;
                        case 2:
                            imgNowcolor.setImageResource(R.drawable.orange);
                            break;
                        case 3:
                            imgNowcolor.setImageResource(R.drawable.green);
                            break;
                        default:
                            imgNowcolor.setImageResource(R.drawable.red);
                    }
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
                            adapter.notifyDataSetChanged(); // 필요한지 확인 필요
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

    public void connection(String ssid, String capabilities){
        // Profile Check
        if(wifiUtil.getProfileId(ssid) == -1){
            if(wifiUtil.isNeedPassword(capabilities)){
                // Get Password
                askPassword(ssid, capabilities);
            }
            else {
                // No Password New Connect
                int net_id = wifiUtil.addProfile(ssid, capabilities);
                wifiUtil.connect(net_id);
            }
        }
        else {  // Already Ap Profile
            wifiUtil.connect(ssid);
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
                    int net_id = wifiUtil.addProfile(ssid, capabilities, password);
                    if(net_id != -1) {
                        Log.d("TEST", "Try Password [net_id:" + Integer.toString(net_id) + "]");
                        password_try_net_id = net_id;

                        wifiUtil.connect(net_id);
                        updateNowAp();
                        Log.d("TEST", ssid + " " + capabilities + " " + password);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "패스워드의 길이가 부족합니다.", Toast.LENGTH_SHORT);  // Capabilities 에 맞는 비번 가이드라인 제시 추가 필요
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "패스워드가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void callMoreActivity(ApItem ap_item) {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_ITEM", ap_item);
        startActivity(intent);
    }
    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            Log.d("TEST", "onReceive() in WifiReceiver");
            String action  = intent.getAction();
            if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
                Log.d("TEST", "Check SUPPLICANT_STATE_CHANGED_ACTION");
                SupplicantState supl_state=((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                switch(supl_state){
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

                int supl_error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(supl_error==WifiManager.ERROR_AUTHENTICATING){
                    Log.d("TEST", "ERROR_AUTHENTICATING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi 패스워드가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();

                    if(password_try_net_id != -1) {
                        wifiUtil.removeProfile(password_try_net_id);
                        password_try_net_id = -1;
                    }
                }

            }   // if Closed
        }   // onReceive Closed
    }   // WifiReceiver Class Closed
}
