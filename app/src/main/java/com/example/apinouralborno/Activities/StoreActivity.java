package com.example.apinouralborno.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;


import com.example.apinouralborno.Activities.FavActivity;
import com.example.apinouralborno.Activities.LoginActivity;
import com.example.apinouralborno.Activities.OrderActivity;
import com.example.apinouralborno.Activities.OrderListActivity;
import com.example.apinouralborno.Activities.ProfileActivity;
import com.example.apinouralborno.Adapters.ProductAdapter;
import com.example.apinouralborno.Listeners.ProductListeners;
import com.example.apinouralborno.LoginFragment;
import com.example.apinouralborno.Model.product;
import com.example.apinouralborno.R;
import com.example.apinouralborno.databinding.ActivityStoreBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StoreActivity extends AppCompatActivity {

    ActivityStoreBinding binding;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.gamMaterialToolbar);
        setTitle("Store");

        firestore = FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firestore.collection("Products")
                        .whereEqualTo("Title",s).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){

                                    ArrayList<product> productList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                        product product= document.toObject(product.class);
                                        product.setDocumentID(document.getId());

                                        productList.add(product);
                                    }


                                    ProductAdapter adapter = new ProductAdapter(productList, getApplicationContext(), new ProductListeners() {
                                        @Override
                                        public void productDetails(product product) {

                                            addCart(product);
                                        }

                                        @Override
                                        public void productFav(product product, int pos) {
                                            addFav(product);
                                        }

                                    });
                                    binding.Rv.setAdapter(adapter);
                                    binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
                                } else {
                                    task.getException().getMessage();
                                }
                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });




        binding.SpFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    firestore.collection("Products").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {

                                        ArrayList<product> productList = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {


                                            product product= document.toObject(product.class);
                                            product.setDocumentID(document.getId());

                                            productList.add(product);
                                        }

                                        ProductAdapter adapter = new ProductAdapter(productList, getApplicationContext(), new ProductListeners() {
                                            @Override
                                            public void productDetails(product product) {

                                                addCart(product);


                                            }

                                            @Override
                                            public void productFav(product product, int pos) {
                                                addFav(product);
                                            }


                                        });
                                        binding.Rv.setAdapter(adapter);
                                        binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
                                    }


                                }
                            });
                } else if (i == 1) {
                    firestore.collection("Products").orderBy("Price", Query.Direction.ASCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {

                                        ArrayList<product> productList = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {


                                            product product= document.toObject(product.class);
                                            product.setDocumentID(document.getId());

                                            productList.add(product);
                                        }

                                        ProductAdapter adapter = new ProductAdapter(productList, getApplicationContext(), new ProductListeners() {
                                            @Override
                                            public void productDetails(product product) {

                                                addCart(product);
                                            }

                                            @Override
                                            public void productFav(product product, int pos) {
                                                addFav(product);
                                            }


                                        });
                                        binding.Rv.setAdapter(adapter);
                                        binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
                                    }


                                }
                            });
                } else {
                    firestore.collection("Products").orderBy("Price", Query.Direction.DESCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {


                                        ArrayList<product> productList = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {


                                           product product= document.toObject(product.class);
                                           product.setDocumentID(document.getId());

                                           productList.add(product);
                                        }


                                        ProductAdapter adapter = new ProductAdapter(productList, getApplicationContext(), new ProductListeners() {
                                            @Override
                                            public void productDetails(product product) {
                                                addCart(product);
                                            }

                                            @Override
                                            public void productFav(product product, int pos) {
                                                addFav(product);
                                            }


                                        });

                                        binding.Rv.setAdapter(adapter);
                                        binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
                                    }


                                }
                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                firestore.collection("Products").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    ArrayList<product> productList = new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                        product product= document.toObject(product.class);
                                        product.setDocumentID(document.getId());

                                        productList.add(product);
                                    }

                                    ProductAdapter adapter = new ProductAdapter(productList, getApplicationContext(), new ProductListeners() {
                                        @Override
                                        public void productDetails(product product) {
                                           addCart(product);
                                        }

                                        @Override
                                        public void productFav(product product, int pos) {
                                            addFav(product);
                                        }



                                    });
                                    binding.Rv.setAdapter(adapter);
                                    binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
                                }


                            }
                        });
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){

        switch (item.getItemId()) {

            case R.id.toolbar_menu_Logout:

                if (firebaseUser != null) {
                    auth.signOut();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));

                    return true;
                } else{
                    LoginFragment frag = new LoginFragment();
                    frag.show(getSupportFragmentManager(),"Hello");

                    return false;
                }



            case R.id.toolbar_menu_profile:

                if (firebaseUser != null) {

                startActivity(new Intent(getBaseContext(), ProfileActivity.class));

                return true;
                }else{

                    LoginFragment frag = new LoginFragment();
                    frag.show(getSupportFragmentManager(),"Hello");

                    return false;
                }

            case  R.id.toolbar_menu_OrderList:

                if (firebaseUser != null) {
                startActivity(new Intent(getBaseContext(), OrderListActivity.class));

                return true;
                } else {
                    LoginFragment frag = new LoginFragment();
                    frag.show(getSupportFragmentManager(),"Hello");

                    return false;
                }

            case  R.id.toolbar_menu_FavList:

                if (firebaseUser != null) {
                    startActivity(new Intent(getBaseContext(), FavActivity.class));

                    return true;
                } else{

                    LoginFragment frag = new LoginFragment();
                    frag.show(getSupportFragmentManager(),"Hello");

                    return false;
                }
        }

        return false;
    }

void addFav(product product){
        if (firebaseUser != null){
    String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    firestore.collection("Favorite").document(firebaseUser).collection("userFav")
            .document(product.getDocumentID())
            .set(product)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Log.d("Fav", "True");
                    }else {
                        Log.d("Fav","False");}
                }
            });
        } else {
            LoginFragment frag = new LoginFragment();
            frag.show(getSupportFragmentManager(),"Hello");
        }
}
void addCart(product product){
    if (firebaseUser != null){
        startActivity(new Intent(getBaseContext(), OrderActivity.class).putExtra("product", product));
    }  else {
        LoginFragment frag = new LoginFragment();
        frag.show(getSupportFragmentManager(),"Hello");
    }
}
}

