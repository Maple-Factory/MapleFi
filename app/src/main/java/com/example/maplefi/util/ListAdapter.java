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
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
//    private ArrayList<ApItem> tssid = null;
    private ArrayList<ApItem> mdata = null;

    public interface OnItemClickListener{
        //        public void onItemClick(View view, int position, boolean isUser);
        void onItemClick(View v, int position);
    }

    private OnItemClickListener onItemClickListener;

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_SSID;
        ImageView ap_state;
        ImageButton btn_ap_connect;
        ImageButton btn_ap_info;
//        WifilistItemBinding binding;//mvvm

        public ViewHolder(View itemView) {
//            super(binding.getRoot());//mvvm
//            this.binding = binding;//mvvm
            super(itemView);

//            //뷰 객체에 대한 참조
            textView_SSID = itemView.findViewById(R.id.tv_ssid);
            ap_state = itemView.findViewById(R.id.img_rssiDegree);
            btn_ap_connect = itemView.findViewById(R.id.imgb_connect);
            btn_ap_info = itemView.findViewById(R.id.imgb_moreinf);

        }
        public ImageButton getMoreinf(){
            return btn_ap_info;
        }
        public ImageButton getAp_connect(){
            return this.btn_ap_connect;
        }

//        void bind(ApItem apitem){//mvvm
////            binding.setVariable(com.example.maplefi.BR.listitem, apitem);   // MVVM
//        }
    }

    //생성자에게 리스트 객체 전달 받는 파트
//    public ListAdapter(){//mvvm
//        this.mdata = new ArrayList<>();
//        mdata.add(new ApItem("test")); // Debug
//    }

    //생성자에게 리스트 객체 전달 받는 파트
    ListAdapter(ArrayList<ApItem> list/*, ListAdapterOld.OnItemClickListener onItemClickListener*/){
        mdata = list;
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }

    @NonNull
    @Override
    //뷰홀더 객체 생성해서 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //일부 뷰를 메모리 상에 객체화 할때 layoutinflatoer 클래스 사용
        LayoutInflater inflater = (LayoutInflater)context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.wifilist_item, parent, false);
        ListAdapter.ViewHolder vh = new ListAdapter.ViewHolder(view);
//        WifilistItemBinding binding = WifilistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);//mvvm
//        return new ViewHolder(binding);//wifilist item 반환 mvvm
        return vh;//wifilist item 반환
    }

    @Override
    //포지션에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        apList ssid = apList.get(position);
//        holder.textView_SSID.setText(ssid);
        ApItem apitem = mdata.get(position);
//        holder.bind(apitem);//mvvm

        holder.textView_SSID.setText(apitem.getSsid());
//        holder.ap_state.setImageDrawable(apitem.getImg_ap_rssi());

        holder.getAp_connect().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
//                Log.d("debug", "onClick: Apconnect");
            }
        });

        holder.getMoreinf().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
//                Log.d("debug", "onClick: getmoreinf");
            }
        });
    }

    void setApItem(List<ApItem> apitem){
        if(apitem == null){
            return ;
        }
        notifyDataSetChanged();
    }

    //전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return mdata.size();
    }

}
