package com.example.maplefi.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
//        void onItemClick(View v, int position);
    }
    private MainActivity mActivity;
    private ArrayList<ApItem> apItems = null;
    public OnApItemClickListener OnApItemClickListener;

    // 아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ssidTextView;
        ImageView secColorImgView;
        ImageView rssiImgView;
        ImageButton moreinfoImgButton;
        ImageButton connectImgButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조
            ssidTextView = itemView.findViewById(R.id.tv_ssid);
            secColorImgView = itemView.findViewById(R.id.img_color);
            rssiImgView = itemView.findViewById(R.id.img_rssiDegree);
            moreinfoImgButton = itemView.findViewById(R.id.imgb_moreinf);
            connectImgButton = itemView.findViewById(R.id.imgb_connect);

            // 아이템 클릭 이벤트 처리
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        // 아이템 클릭 이벤트
                        ApItem apItem = apItems.get(pos);
                        Log.d("TEST", "onLongClick: this is listAdapter" + pos);

                        final int clickedNetId = mActivity.wifiUtil.getProfileId(apItem.getSsid()); // 와이파이가 안되는 기기면 에러 발생
                        if(clickedNetId != -1){
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            // 삭제하기
                                            mActivity.wifiUtil.removeProfile(clickedNetId);
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
        this.apItems = list;
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
        ApItem apItem = apItems.get(position);

        String text = apItem.getSsid();
        int secLevel = apItem.getSecLevel();
        int rssiLevel = apItem.getRssiLevel();

        holder.ssidTextView.setText(text);
        switch (secLevel){
            case 1:
                holder.secColorImgView.setImageResource(R.drawable.red);
                break;
            case 2:
                holder.secColorImgView.setImageResource(R.drawable.orange);
                break;
            case 3:
                holder.secColorImgView.setImageResource(R.drawable.green);
                break;
            default:
                holder.secColorImgView.setImageResource(R.drawable.red);
        }
        switch (rssiLevel){
            case 1:
                holder.rssiImgView.setImageResource(R.drawable.wifi_1);
                break;
            case 2:
                holder.rssiImgView.setImageResource(R.drawable.wifi_2);
                break;
            case 3:
                holder.rssiImgView.setImageResource(R.drawable.wifi_3);
                break;
            default:
                holder.rssiImgView.setImageResource(R.drawable.wifi_x);
        }

        holder.moreinfoImgButton.setTag(position);
        holder.moreinfoImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnApItemClickListener != null) {
                    OnApItemClickListener.onMoreBtnClick(v, (int)v.getTag());
                }
            }
        });
        holder.connectImgButton.setTag(position);
        holder.connectImgButton.setOnClickListener(new View.OnClickListener() {
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
        return apItems.size();
    }
}
