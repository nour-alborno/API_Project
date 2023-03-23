package com.example.apinouralborno.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apinouralborno.Model.product;
import com.example.apinouralborno.databinding.ActivityOrderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    ActivityOrderBinding binding;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        product p = (product) intent.getSerializableExtra("product");

        binding.TvPrice.setText("Price: "+String.valueOf(p.getPrice())+"$");
        binding.TvTitle.setText(p.getTitle());
        binding.TvCategory.setText("Category: "+p.getCategory());
        binding.TvDetails2.setText(p.getDetails());

        Glide.with(this).load(p.getPicture())
                .into(binding.ImgProduct);


        binding.BtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String Title = binding.TvTitle.getText().toString();
               String Price = binding.TvPrice.getText().toString();
               String Quantity = binding.EtQuantity.getText().toString();
               String Picture= p.getPicture();

                Map<String, Object> data1 = new HashMap<>();
                data1.put("Title", Title);
                data1.put("Price",Price);
                data1.put("Quantity",Quantity);
                data1.put("Picture",Picture);

                String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                firestore.collection("Order").document(firebaseUser).collection("userOrder")
                        .document(p.getDocumentID()).set(data1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(OrderActivity.this, "Ordered Successfully", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(OrderActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}