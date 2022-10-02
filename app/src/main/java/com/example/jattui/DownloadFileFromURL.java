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
    public static File downloadTask(Context context, String url, String extension) throws URISyntaxException, GeneralSecurityException, IOException {
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

        return downloadFileDir;


    }
}
