package com.example.maplefi.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maplefi.R;
import com.example.maplefi.ui.ApItem;

import java.util.ArrayList;

public class ListAdapterOld extends RecyclerView.Adapter<ListAdapterOld.ViewHolder> {
    private ArrayList<ApItem> tssid = null;

    public interface OnItemClickListener{
        //        public void onItemClick(View view, int position, boolean isUser);
        void onItemClick(View v, int position);
    }

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_SSID;
        ImageView ap_state;
        ImageButton btn_ap_connect;
        ImageButton btn_ap_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            textView_SSID = itemView.findViewById(R.id.tv_ssid);
            ap_state = itemView.findViewById(R.id.img_rssiDegree);
            btn_ap_connect = itemView.findViewById(R.id.imgb_connect);
            btn_ap_info = itemView.findViewById(R.id.imgb_moreinf);

        }
    }
    //TODO: 리스트 어텝더 작성 ㅡ

    //생성자에게 리스트 객체 전달 받는 파트
    public ListAdapterOld(ArrayList<ApItem> list, OnItemClickListener onItemClickListener){
        tssid = list;
    }

    @NonNull
    @Override
    //뷰홀더 객체 생성해서 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //일부 뷰를 메모리 상에 객체화 할때 layoutinflatoer 클래스 사용
        LayoutInflater inflater = (LayoutInflater)context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.wifilist_item, parent, false);
        ListAdapterOld.ViewHolder vh = new ListAdapterOld.ViewHolder(view);
        return vh;  // wifilist item 반환
    }

    @Override
    //포지션에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = tssid.get(position).getItem_ssid();
        holder.textView_SSID.setText(text);
    }

    //전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return tssid.size();
    }

}
