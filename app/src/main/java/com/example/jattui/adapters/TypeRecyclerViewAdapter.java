package com.example.jattui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jattui.DashBoard;
import com.example.jattui.R;
import com.example.jattui.models.Document;
import com.example.jattui.models.Super;

import java.util.List;

public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewHolder> {

    Context context;
    List<Super> listInstances;
    int type;

    public TypeRecyclerViewAdapter(Context context, List<Super> listInstances, int type) {
        this.context = context;
        this.listInstances = listInstances;
        this.type = type;
    }

    @NonNull
    @Override
    public TypeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view;
        view = li.inflate(R.layout.document_layout, parent, false);
        return new TypeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeRecyclerViewHolder holder, int position) {
        initDocuments(holder, position);
    }

    private void initDocuments(TypeRecyclerViewHolder holder, int position) {
        Document document = (Document) listInstances.get(position);
        holder.tvDocumentText.setText(document.getDocumentName());
        holder.btnDownload.setOnClickListener(view -> {
            WebView theWebPage = new WebView(context);
            theWebPage.getSettings().setJavaScriptEnabled(true);
            theWebPage.getSettings().setPluginState(WebSettings.PluginState.ON);
            Activity contextActivity = (Activity) context;
            contextActivity.setContentView(theWebPage);
            theWebPage.loadUrl(document.getDocumentUrl());
            DashBoard.isWebCurrently = true;
        });
    }


    @Override
    public int getItemCount() {
        return listInstances.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
