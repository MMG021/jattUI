package com.example.jattui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RenameFileActivity extends AppCompatActivity {

    EditText etRename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_file);
        etRename = findViewById(R.id.et_rename);
        etRename.setText(DashBoard.selectedDocument.getDocumentName());
    }

    public void submitButton(View view) {
        String id = DashBoard.selectedDocument.getId();
        DashBoard.selectedDocument.setDocumentName(etRename.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Documents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(id)
                .setValue(DashBoard.selectedDocument).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            finish();
                    }
                });
    }
}