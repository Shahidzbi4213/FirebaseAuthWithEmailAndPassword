package com.gulehri.edu.pk.securitywork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.gulehri.edu.pk.securitywork.databinding.ActivitySignInBinding;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.logIn.setOnClickListener(view -> {

            String email = binding.eID.getEditText().getText().toString().trim();
            String password = binding.pswrd.getEditText().getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, task -> {

                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignIn.this, Dashbaord.class);
                                startActivity(intent);

                                binding.eID.getEditText().setText("");
                                binding.pswrd.getEditText().setText("");

                            }

                        }).addOnFailureListener(SignIn.this, e -> {
                            binding.eID.getEditText().setText("");
                            binding.pswrd.getEditText().setText("");


                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(getApplicationContext(), "Email not Found", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                if (email.isEmpty()) {
                    binding.eID.getEditText().setError("Enter Email");
                }
                if (password.isEmpty()) {
                    Objects.requireNonNull(binding.pswrd.getEditText()).setError("Enter password");
                }
            }
        });

        binding.forgotPassword.setOnClickListener(v -> {
            String email = binding.eID.getEditText().getText().toString();

            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> {
                            binding.eID.getEditText().setText("");
                            Toast.makeText(SignIn.this, "Verification Email Send", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            binding.eID.getEditText().setText("");
                            Toast.makeText(SignIn.this, "Failed to Send Verification Email", Toast.LENGTH_SHORT).show();
                        });
            } else {
                binding.eID.getEditText().setError("Email Field Can't be empty");
            }
        });
    }
}