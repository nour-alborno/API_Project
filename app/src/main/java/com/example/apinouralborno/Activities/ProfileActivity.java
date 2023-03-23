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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apinouralborno.databinding.ActivityProfileBinding;
import com.example.apinouralborno.Model.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    FirebaseUser user;

    boolean accessing_result;
    Uri profile_img_uri;
    Uri imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.LayoutPass.setVisibility(View.GONE);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();



//Setting User Data
        firestore.collection("UserData").document(firebaseUser).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                         userData userData   = task.getResult().toObject(userData.class);

                         binding.Etname.setText(userData.getName());
                         binding.regSpCountry.setSelection(userData.getCountry());


                         imgUrl = Uri.parse(userData.getPicture());


                         Glide.with(getBaseContext()).load(userData.getPicture())
                                    .into(binding.ImgUserimage);

                        }else {
                            startActivity(new Intent(getBaseContext(), AddUserDataActivity.class));
                            finish();
                        }
                    }
                });


        // Resetting password section
             binding.TvResetPassword.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     binding.LayoutPass.setVisibility(View.VISIBLE);

                 }
             });

             binding.BtnChangePass.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),binding.EtOldpassword.getText().toString());
                     user.reauthenticate(authCredential)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

if (task.isSuccessful()){
    user.updatePassword(binding.EtNewpassword.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.reload();
                        Toast.makeText(ProfileActivity.this, "Password has been updated", Toast.LENGTH_SHORT).show();
                    } else {
                        task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
} else{
    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
}

                                 }
                             });

                 }
             });



                                  //Updating pic//
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


        binding.BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.Etname.getText().toString();
                int country = binding.regSpCountry.getSelectedItemPosition();
                String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();

                Log.d("Test",name + country + email + imgUrl.toString());

                if (!name.isEmpty() && country != 0 && !email.isEmpty() && imgUrl !=null) {
                    userData userData = new userData(name, email, country, imgUrl.toString());
                    firestore.collection("UserData").document(firebaseUser).set(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ProfileActivity.this, "Erorrr", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    void uploadImage(){

        //Setting the progress
        ProgressDialog progressDialog= new ProgressDialog(ProfileActivity.this);
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
                                Log.d("bb",imgUrl.toString());
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