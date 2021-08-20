package com.gulehri.edu.pk.securitywork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gulehri.edu.pk.securitywork.databinding.ActivityDashbaordBinding;

public class Dashbaord extends AppCompatActivity {

    private ActivityDashbaordBinding binding;
    private String email, id;
    private FirebaseAuth.AuthStateListener  authStateListener ;
    private FirebaseAuth maAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashbaordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        maAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> updateUI();

        binding.signOut.setOnClickListener(view -> {
            maAuth.signOut();

            startActivity(new Intent(this, SignIn.class));
        });

      binding.delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

             if( maAuth.getCurrentUser() != null){

                 maAuth.getCurrentUser().delete();
                 updateUI();
                 startActivity(new Intent(getApplicationContext(), SignIn.class));

             }
          }
      });
    }

    private void updateUI() {
        FirebaseUser user = maAuth.getCurrentUser();

       if (user != null){
           id = user.getUid();
           email = user.getEmail();

           binding.userID.setText(id);
           binding.emailAdress.setText(email);
       }else{
           binding.userID.setText("Logged Out");
           binding.emailAdress.setText("Failed");
       }


    }

    @Override
    protected void onResume() {
        super.onResume();
        maAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (authStateListener != null){
            if(maAuth !=null){

                maAuth.removeAuthStateListener(authStateListener);
            }
        }
    }
}