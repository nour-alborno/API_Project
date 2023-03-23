package com.example.apinouralborno.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apinouralborno.Adapters.FavAdapter;
import com.example.apinouralborno.Listeners.ProductListeners;
import com.example.apinouralborno.Model.product;

import com.example.apinouralborno.databinding.ActivityFavBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    ActivityFavBinding binding;
    FirebaseFirestore firestore;
    int currentPosition;
    ArrayList<product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        firestore.collection("Favorite").document(firebaseUser)
                .collection("userFav")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                             productList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                product product= document.toObject(product.class);
                                product.setDocumentID(document.getId());

                                productList.add(product);
                            }

                            FavAdapter adapter = new FavAdapter(productList, getBaseContext(), new ProductListeners() {
                                @Override
                                public void productDetails(product product) {
                                    startActivity(new Intent(getBaseContext(), OrderActivity.class).putExtra("product", product));
                                }

                                @Override
                                public void productFav(product product, int pos) {
                                    firestore.collection("Favorite").document(firebaseUser)
                                            .collection("userFav").document(product.getDocumentID()).delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        Toast.makeText(FavActivity.this, "Unliked", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(FavActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    currentPosition=pos;
                                                }
                                            });
                                }




                            });
                            binding.Rv.setAdapter(adapter);
                            binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL,false));
                            adapter.notifyItemRemoved(currentPosition);
                            adapter.notifyItemRangeChanged(currentPosition,productList.size());
                        }


                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}