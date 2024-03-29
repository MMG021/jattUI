package com.example.jattui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jattui.adapters.TypeRecyclerViewAdapter;
import com.example.jattui.models.Document;
import com.example.jattui.models.Super;
import com.example.jattui.utils.FileEncryptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashBoard extends AppCompatActivity {

    public static boolean isWebCurrently = false;
    public static Document selectedDocument;

    String TAG = "tag";
    List<Super> listInstances;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    RecyclerView recyclerView;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        initRv();
        initFirebaseData();
    }

    private void initFirebaseData() {
        ProgressDialog pd = new ProgressDialog(DashBoard.this);
        pd.setMessage("loading");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("Documents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pd.dismiss();
                        listInstances.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Document document = child.getValue(Document.class);
                            Log.i(TAG, "onDataChange: ");
                            if (document != null)
                                listInstances.add(document);
                        }
                        typeRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initRv() {
        recyclerView = findViewById(R.id.rv);
        listInstances = new ArrayList<>();
        typeRecyclerViewAdapter = new TypeRecyclerViewAdapter(this, listInstances, 1);
        recyclerView.setAdapter(typeRecyclerViewAdapter);
    }

    public void logout(View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, view);

        popup.getMenu().add("Logout");


        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle() == "Logout") {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

            return true;
        });

        popup.show(); //showing popup menu

//
    }

    public void upload(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 11);
    }

    public void scanDocument(View view) {
        startActivity(new Intent(this, CameraActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 22) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            String id2 = String.valueOf(System.currentTimeMillis());
            final StorageReference mStoreRef = FirebaseStorage.getInstance().getReference().child("Documents")
                    .child(id2);

            try {
                ProgressDialog pd = new ProgressDialog(DashBoard.this);
                pd.setMessage("loading");
                pd.show();

//                TODO: ENCRYPT FILE HERE

                File encryptedFile = FileEncryptor.encryptFile(finalFile);
                Log.i(TAG, "onActivityResult: ENCRYPTING FILE - " + encryptedFile.getAbsolutePath());
                mStoreRef.putFile(Uri.fromFile(encryptedFile)).continueWithTask(task -> mStoreRef.getDownloadUrl()).addOnSuccessListener(uri -> {
                    pd.dismiss();
                    String fileUrl = uri + "";
                    String id = String.valueOf(System.currentTimeMillis());
                    SimpleDateFormat s = new SimpleDateFormat("dd_MM_hh_mm_ss");
                    String ext = Utils.getExtension(finalFile);
                    String name = s.format(new Date()) + ext;
                    Document document = new Document(id, name, fileUrl, ext, finalFile.getAbsolutePath());
                    FirebaseDatabase.getInstance().getReference().child("Documents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(id).setValue(document);

                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "onActivityResult: " + e.getMessage());
            }


        }
        if (resultCode == Activity.RESULT_OK && requestCode == 11) {
            Uri selectedImage = data.getData();
            final StorageReference mStoreRef = FirebaseStorage.getInstance().getReference().child("Documents")
                    .child(selectedImage.getLastPathSegment());
            File file = Utils.getFile(this, selectedImage);
            try {
                ProgressDialog pd = new ProgressDialog(DashBoard.this);
                pd.setMessage("loading");
                pd.show();
                File encryptedFile = FileEncryptor.encryptFile(file);
                Log.i(TAG, "onActivityResult: ENCRYPTING FILE - " + encryptedFile);
                mStoreRef.putFile(Uri.fromFile(encryptedFile)).continueWithTask(task -> mStoreRef.getDownloadUrl()).addOnSuccessListener(uri -> {
                    String fileUrl = uri + "";
                    pd.dismiss();
                    String id = String.valueOf(System.currentTimeMillis());
                    SimpleDateFormat s = new SimpleDateFormat("dd_MM_hh_mm_ss");
                    String ext = Utils.getExtension(file);
                    String name = s.format(new Date()) + ext;
                    Document document = new Document(id, name, fileUrl, ext, file.getAbsolutePath());
                    FirebaseDatabase.getInstance().getReference().child("Documents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(id).setValue(document);
                    Log.i(TAG, "onActivityResult: " + fileUrl);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "onActivityResult: " + e.getMessage());
            }

        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        if (isWebCurrently) {
            isWebCurrently = false;
            startActivity(new Intent(this, DashBoard.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}
