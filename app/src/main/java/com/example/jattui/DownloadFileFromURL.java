package com.example.jattui;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

public class DownloadFileFromURL {
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
                        .getAbsolutePath(), System.currentTimeMillis() + "." + extension);

        request.setDestinationUri(Uri.fromFile(downloadFileDir));
        downloadmanager.enqueue(request);
//        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
//                try {
//                    File file = FileEncryptor.decryptFile(new File(downloadFileDir.getAbsolutePath()));
//                    Log.i("TAG", "onReceive: " + file.getAbsolutePath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.i("TAG", "onReceive: decrypting file");
//                }
//
//            }
//        };
//
//        context.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
