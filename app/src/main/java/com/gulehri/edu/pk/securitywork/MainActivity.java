package com.gulehri.edu.pk.securitywork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.gulehri.edu.pk.securitywork.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signUp.setOnClickListener(v -> {

            String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString();
            String pass = Objects.requireNonNull(binding.password.getEditText()).getText().toString();

            if (email.isEmpty()) {
                binding.email.getEditText().setError("Field Can't be Empty");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.getEditText().setError("Invalid Email");
            }
            if (pass.isEmpty()) {
                binding.password.getEditText().setError("Field Can't be Empty");
            }
            if (pass.length() < 6) {
                binding.password.getEditText().setError("pass must be greater then 6 character");
            }

            if ((!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                    (!pass.isEmpty() && pass.length() >= 6)) {
                binding.prBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                binding.email.getEditText().setText("");
                                binding.password.getEditText().setText("");
                                binding.prBar.setVisibility(View.GONE);
                                binding.email.getEditText().requestFocus();
                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                binding.email.getEditText().setText("");
                                binding.password.getEditText().setText("");
                                binding.prBar.setVisibility(View.GONE);
                                binding.email.setFocusable(true);

                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), "Email is registered with another account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }


        });

        binding.noted.setOnClickListener(v1 -> startActivity(new Intent(getApplicationContext(), SignIn.class)));
    }


}