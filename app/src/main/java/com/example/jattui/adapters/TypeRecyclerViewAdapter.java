package com.example.jattui.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jattui.DashBoard;
import com.example.jattui.R;
import com.example.jattui.models.Document;
import com.example.jattui.models.Super;
import com.example.jattui.utils.DownloadFileFromURLOrig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
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
        holder.btnMenu.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context, holder.btnMenu);

            popup.getMenu().add("View");
            popup.getMenu().add("Download");
            popup.getMenu().add("Share");
            popup.getMenu().add("Delete");


            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle() == "Download") {
                    DashBoard.isWebCurrently = true;
                    try {
                        DownloadFileFromURLOrig.downloadTask(context, document.getDocumentUrl(), document.getExtension());
                        ProgressDialog pd = new ProgressDialog(context);
                        pd.setMessage("loading");
                        pd.show();
                        new Handler().postDelayed(() -> {
                            pd.dismiss();
                            Toast.makeText(context, "Downloading file, check notifications", Toast.LENGTH_SHORT).show();
                        }, 2000);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (item.getTitle() == "Share") {
//                    /*Create an ACTION_SEND Intent*/
//                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                    /*This will be the actual content you wish you share.*/
//                    String shareBody = document.getDocumentUrl();
//                    /*The type of the content is text, obviously.*/
//                    intent.setType("text/plain");
//                    /*Applying information Subject and Body.*/
//                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "File");
//                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                    /*Fire!*/
//                    context.startActivity(Intent.createChooser(intent, "Whatsapp"));

                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(document.getAbsolutePath());

                    Uri imageUri = FileProvider.getUriForFile(
                            context,
                            "com.example.jattui.provider", //(use your app signature + ".provider" )
                            fileWithinMyDir);

                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("*/*");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, imageUri);
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                        context.startActivity(Intent.createChooser(intentShareFile, "Share File"));

                    } else {
                        Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show();
                    }


                }
                if (item.getTitle() == "Delete")
                    FirebaseDatabase.getInstance().getReference().child("Documents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(document.getId()).removeValue();
                if (item.getTitle() == "View") {
                    File fileWithinMyDir = new File(document.getAbsolutePath());

                    Uri uri = FileProvider.getUriForFile(
                            context,
                            "com.example.jattui.provider", //(use your app signature + ".provider" )
                            fileWithinMyDir);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uri, "image/*");
                    context.startActivity(intent);
                }


                return true;
            });

            popup.show(); //showing popup menu
        }); //closing the setOnClickListener method

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
