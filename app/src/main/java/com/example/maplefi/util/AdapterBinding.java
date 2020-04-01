package com.example.maplefi.util;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maplefi.ui.ApItem;

public class AdapterBinding {
    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<ApItem> apItemlist) {
        ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setApItem(apItemlist);
        }
    }
}
