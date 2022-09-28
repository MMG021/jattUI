package com.example.jattui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jattui.DashBoard;
import com.example.jattui.DownloadFileFromURL;
import com.example.jattui.R;
import com.example.jattui.models.Document;
import com.example.jattui.models.Super;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
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
//            WebView theWebPage = new WebView(context);
//            theWebPage.getSettings().setJavaScriptEnabled(true);
//            theWebPage.getSettings().setPluginState(WebSettings.PluginState.ON);
//            Activity contextActivity = (Activity) context;
//            contextActivity.setContentView(theWebPage);
//            theWebPage.loadUrl(document.getDocumentUrl());
            DashBoard.isWebCurrently = true;

            try {
                DownloadFileFromURL.downloadTask(context, document.getDocumentUrl(), document.getExtension());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
