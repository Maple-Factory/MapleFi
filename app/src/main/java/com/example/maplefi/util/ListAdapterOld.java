package com.example.maplefi.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maplefi.MainActivity;
import com.example.maplefi.R;
import com.example.maplefi.ui.ApItem;

import java.util.ArrayList;

public class ListAdapterOld extends RecyclerView.Adapter<ListAdapterOld.ViewHolder> {
    public interface OnApItemClickListener {
        void onMoreBtnClick(View v, int position);
        void onConBtnClick(View v, int position);
        void onItemClick(View v, int position);
    }
    private MainActivity mActivity;
    private ArrayList<ApItem> ap_items = null;
    public OnApItemClickListener OnApItemClickListener;

    // 아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_SSID;
        ImageView img_ap_color;
        ImageView img_ap_rssi;
        ImageButton btn_ap_info;
        ImageButton btn_ap_connect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조
            textView_SSID = itemView.findViewById(R.id.tv_ssid);
            img_ap_color = itemView.findViewById(R.id.img_color);
            img_ap_rssi = itemView.findViewById(R.id.img_rssiDegree);
            btn_ap_info = itemView.findViewById(R.id.imgb_moreinf);
            btn_ap_connect = itemView.findViewById(R.id.imgb_connect);

            // 아이템 클릭 이벤트 처리
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        // 아이템 클릭 이벤트
                        ApItem apItem = ap_items.get(pos);
                        Log.d("TEST", "onLongClick: this is listAdapter" + pos);

                        ApItem ap_item = ap_items.get(pos);
                        final int clicked_net_id = mActivity.wifiUtil.getProfileId(ap_item.getSsid());  // 와이파이가 안되는 기기면 에러 발생
                        if(clicked_net_id != -1){
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            // 삭제하기
                                            mActivity.wifiUtil.removeProfile(clicked_net_id);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setMessage("저장된 패스워드를 삭제하시겠습니까?").setPositiveButton("네", dialogClickListener)
                                    .setNegativeButton("아니오", dialogClickListener).show();
                        }
                        notifyItemChanged(pos);
                    }
                    return true;
                }
            });

        }
    }

    // 생성자에게 리스트 객체 전달 받는 파트
    public ListAdapterOld(MainActivity activity, ArrayList<ApItem> list, OnApItemClickListener onApItemClickListener/*, OnItemClickListener itemClickListener*/){
        this.mActivity = activity;
        this.ap_items = list;
        this.OnApItemClickListener = onApItemClickListener;
//        this.itemClickListener =  itemClickListener;
    }

    @NonNull
    @Override
    // 뷰홀더 객체 생성해서 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //일부 뷰를 메모리 상에 객체화 할때 layoutinflatoer 클래스 사용
        LayoutInflater inflater = (LayoutInflater)context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.wifilist_item, parent, false);
        ListAdapterOld.ViewHolder vh = new ListAdapterOld.ViewHolder(view);
        return vh;  // wifilist item 반환
    }

    @Override
    // 포지션에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApItem ap_item = ap_items.get(position);

        String text = ap_item.getSsid();
        int sec_level = ap_item.getSecLevel();
        int rssi_level = ap_item.getRssiLevel();

        holder.textView_SSID.setText(text);
        switch (sec_level){
            case 1:
                holder.img_ap_color.setImageResource(R.drawable.red);
                break;
            case 2:
                holder.img_ap_color.setImageResource(R.drawable.orange);
                break;
            case 3:
                holder.img_ap_color.setImageResource(R.drawable.green);
                break;
            default:
                holder.img_ap_color.setImageResource(R.drawable.red);
        }
        switch (rssi_level){
            case 1:
                holder.img_ap_rssi.setImageResource(R.drawable.wifi_1);
                break;
            case 2:
                holder.img_ap_rssi.setImageResource(R.drawable.wifi_2);
                break;
            case 3:
                holder.img_ap_rssi.setImageResource(R.drawable.wifi_3);
                break;
            default:
                holder.img_ap_rssi.setImageResource(R.drawable.wifi_x);
        }

        holder.btn_ap_info.setTag(position);
        holder.btn_ap_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnApItemClickListener != null) {
                    OnApItemClickListener.onMoreBtnClick(v, (int)v.getTag());
                }
            }
        });
        holder.btn_ap_connect.setTag(position);
        holder.btn_ap_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnApItemClickListener != null) {
                    OnApItemClickListener.onConBtnClick(v, (int)v.getTag());
                }
            }
        });

    }

    // 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return ap_items.size();
    }
}
