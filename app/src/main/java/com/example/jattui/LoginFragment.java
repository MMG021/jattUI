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

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment implements FingerprintDialogCallback{
    EditText email;
    EditText password;
    Button btn;
    LottieAnimationView finger;


    String strEmail;
    String strPassword;
    SharedPreferences.Editor editor;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_login, container, false);
        editor = getContext().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        finger = itemView.findViewById(R.id.finger_print);


        email = itemView.findViewById(R.id.et_email);
        password = itemView.findViewById(R.id.et_password);

        btn = itemView.findViewById(R.id.btn_login);
        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FingerprintDialog.initialize(this)
                        .title("User Fingerprint")
                        .message("Place your finger on sensor")
                        .enterAnimation(DialogAnimation.Enter.RIGHT)
                        .exitAnimation(DialogAnimation.Exit.RIGHT)
                        .circleScanningColor(R.color.colorAccent)
                        .callback(this)
                        .show();

            }
        });





        btn.setOnClickListener(view -> {

            strEmail = email.getText().toString();
            strPassword = password.getText().toString();



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


            FirebaseAuth.getInstance().signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Your logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(),DashBoard.class));
                            getActivity().finish();
                            editor.putString("p", strPassword);
                            editor.apply();
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });


        return itemView;





    }

}
