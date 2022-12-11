package com.example.jattui.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

public class DownloadFileFromURLOrig {
    public static void downloadTask(Context context, String url, String extension) throws URISyntaxException, GeneralSecurityException, IOException {
        Log.i("TAG", "downloadTask: " + url);
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("File");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
        File downloadFileDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath(), System.currentTimeMillis() + ".encrypted");

        request.setDestinationUri(Uri.fromFile(downloadFileDir));
        downloadmanager.enqueue(request);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
                try {
                    File file = FileEncryptor.decryptFile(new File(downloadFileDir.getAbsolutePath()), extension);
                    Log.i("TAG", "onReceive: " + file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAG", "onReceive: decrypting file");
                }

            }
        };

        context.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
