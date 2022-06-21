package com.example.jattui;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText email;
    EditText name;
    EditText password;
    EditText repassword;
    Button btn;

    String strEmail;
    String strName;
    String strPassword;
    String strRepassword;

    SharedPreferences.Editor editor;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editor = getContext().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();

        View itemView = inflater.inflate(R.layout.fragment_register, container, false);

        email = itemView.findViewById(R.id.et_email);
        name = itemView.findViewById(R.id.et_name);
        password = itemView.findViewById(R.id.et_password);
        repassword = itemView.findViewById(R.id.et_repassword);
        btn = itemView.findViewById(R.id.btn_register);

        btn.setOnClickListener(view -> {

            strEmail = email.getText().toString().trim();
            strName = name.getText().toString().trim();
            strPassword = password.getText().toString().trim();
            strRepassword = repassword.getText().toString().trim();

            if (strName.isEmpty()) {
                name.setError("Empty");
                Toast.makeText(getContext(), "Type Your Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strEmail.isEmpty()) {
                email.setError("Empty");
                Toast.makeText(getContext(), "Type Your Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (strPassword.isEmpty()) {
                password.setError("Empty");
                Toast.makeText(getContext(), "Type Your Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!strPassword.equals(strRepassword)) {
                Toast.makeText(getContext(), "Your Password is Not same", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Your now Register", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), DashBoard.class));
                            editor.putString("p", strPassword);
                            editor.putString("e", strEmail);
                            editor.apply();

                        } else
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    });

        });


        return itemView;
    }

}
