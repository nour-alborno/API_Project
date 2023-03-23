package com.example.apinouralborno.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apinouralborno.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivity extends AppCompatActivity {


    ActivityRegisterBinding binding;
    Uri profile_img_uri;
    String profile_img;
    private boolean accessing_result;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;

    String firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();




        binding.regBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = binding.EtEmail.getText().toString();
                String pass = binding.EtPassword.getText().toString().trim();
                String repass= binding.EtRepass.getText().toString().trim();



                if (!(pass.equals(repass))){
                    binding.EtPassword.setError("Passwords are not matching");
                } else if (pass.isEmpty() ){
                    binding.EtPassword.setError("Enter your password ");
                } else if (email.isEmpty()){
                    binding.EtEmail.setError("Enter your email");
                } else {
                    auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(getBaseContext(), AddUserDataActivity.class).putExtra("email",email));
                                        finish();

                                    } else {

                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }






    };});



    }









}