package com.example.jattui;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button btn;
    LottieAnimationView finger;


    String strEmail;
    String strPassword;
    SharedPreferences.Editor editor;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static String getStrSharedPrefs(Context context, String key) {
        return context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).getString(key, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_login, container, false);

        initViews(itemView);

        initPasswordCheck();

        initBiometrics();

        initClickListeners();

        startActivity(new Intent(getContext(), DashBoard.class));

        return itemView;


    }

    private void initClickListeners() {
        finger.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));


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
                            startActivity(new Intent(getContext(), DashBoard.class));
                            getActivity().finish();
                            editor.putString("e", strEmail);
                            editor.putString("p", strPassword);
                            editor.apply();
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initViews(View itemView) {
        editor = getContext().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        finger = itemView.findViewById(R.id.finger_print);
        email = itemView.findViewById(R.id.et_email);
        password = itemView.findViewById(R.id.et_password);
        btn = itemView.findViewById(R.id.btn_login);

    }

    private void initPasswordCheck() {
        checkPrefs("e");
        checkPrefs("p");
    }

    private void checkPrefs(String s) {
        String databasePassword = getStrSharedPrefs(getContext(), s);
        if (databasePassword.isEmpty())
            finger.setVisibility(View.GONE);
        else
            finger.setVisibility(View.VISIBLE);
    }

    private void initBiometrics() {
        executor = ContextCompat.getMainExecutor(getContext());

        biometricPrompt = new BiometricPrompt((FragmentActivity) getContext(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext().getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(getStrSharedPrefs(getContext(), "e"), getStrSharedPrefs(getContext(), "p"))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getContext(), DashBoard.class));
                                getActivity().finish();
                            }
                        });

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext().getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }

}
