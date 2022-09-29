package com.example.jattui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jattui.R;

public class TypeRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tvDocumentText;
    ImageView btnDownload;
    ImageView btnShare;

    public TypeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDocumentText = itemView.findViewById(R.id.document_text);
        btnDownload = itemView.findViewById(R.id.btn_download);
        btnShare = itemView.findViewById(R.id.btn_share);
    }

}


