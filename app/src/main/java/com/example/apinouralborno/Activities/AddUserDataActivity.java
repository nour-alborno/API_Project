package com.example.apinouralborno.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apinouralborno.databinding.ActivityAddUserDataBinding;
import com.example.apinouralborno.Model.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddUserDataActivity extends AppCompatActivity {

    ActivityAddUserDataBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage;

    boolean accessing_result;
    Uri profile_img_uri;
    Uri imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
       String email = intent.getStringExtra("email");


// Getting access permission for storage
        ActivityResultLauncher<String> accessing_storage = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {

                        accessing_result = result;
                    }
                });

        // Getting the pic from the gallery
        ActivityResultLauncher<String> pic_gal = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        binding.ImgUserimage.setImageURI(result);
                        profile_img_uri = result;

                        if (profile_img_uri != null){
                            uploadImage();
                        }
                    }
                });


        binding.ImgUserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessing_storage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (accessing_result) {
                    pic_gal.launch("image/*");


                }


            }
        });



                // Getting the current user ID
             String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Saving pic in storage and user information in fireStore
        binding.BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.EtFullname.getText().toString();
                int country = binding.regSpCountry.getSelectedItemPosition();

                if (!name.isEmpty() && !(country == 0) && !email.isEmpty() && profile_img_uri != null){



                    userData userData = new userData(name,email,country,imgUrl.toString());

                    firestore.collection("UserData").
                            document(firebaseUser).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();

                            } else {
                                Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                } else {
                    Toast.makeText(AddUserDataActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void uploadImage(){

        //Setting the progress
        ProgressDialog progressDialog= new ProgressDialog(AddUserDataActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageReference = firebaseStorage.getReference("Images/" + firebaseUser +"/"+profile_img_uri.getLastPathSegment());
        StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(profile_img_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Image Uploaded!!", Toast.LENGTH_SHORT).show();



                         storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                             @Override
                             public void onComplete(@NonNull Task<Uri> task) {

                                 imgUrl = task.getResult();
                             }
                         });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        progressDialog.setProgress((int) progress);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}