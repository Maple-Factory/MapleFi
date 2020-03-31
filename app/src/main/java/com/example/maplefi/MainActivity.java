package com.example.maplefi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.maplefi.databinding.ActivityMainBinding;
import com.example.maplefi.ui.ApItem;
import com.example.maplefi.ui.MainViewModel;
import com.example.maplefi.util.ListAdapter;
import com.example.maplefi.util.ListAdapterOld;
import com.example.maplefi.util.MainActivityNavigator;
import com.example.maplefi.util.WifiUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private ActivityMainBinding binding;
    private MainViewModel model;
    private WifiUtil wifiUtil ;
    public ListAdapter mAdapter;
    private ArrayList<ApItem> apList = new ArrayList<>();
    private LinearLayout container;
    String item_ssid;
    Drawable ap_state;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> list = new ArrayList<>(); // Old Sample

        wifiUtil = new WifiUtil(getApplicationContext(), this);
        model = new MainViewModel(this, wifiUtil, list /*Old*/);

//        mAdapter = new ListAdapter();
//        apList = new ObservableArrayList<>(); // MVVM
//        apList.add(new ApItem("test")); // MVVM Debug

        //바인딩
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.wifilist.setAdapter(mAdapter);    // MVVM
//        binding.setApList(apList);    // MVVM
        binding.setModel(model);
        model.onCreate();



        //리사이클러뷰 리니어 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.wifiListOld);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //리사이클러뷰에 리스트 어뎁더 객체 지정
        ListAdapterOld adapter = new ListAdapterOld(list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void callMoreActivity() {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        intent.putExtra("AP_NAME",model.now_ap_item.ap_name);
        intent.putExtra("AP_SEC_SCORE","75");
        intent.putExtra("AP_SPEED","7");
        startActivity(intent);
    }


//    public void addItem(String item_ssid, Drawable ap_state){
//        ApItem item = new ApItem();
//
//        item.ap_name(item_ssid);
//        item.setAp_state(ap_state);
//
//        mlist.add(item);
////        Log.d("debug", "addItem: "+item_ssid);//debug
//    }
}
