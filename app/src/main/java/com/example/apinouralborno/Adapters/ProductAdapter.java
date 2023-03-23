package com.example.apinouralborno.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apinouralborno.Listeners.ProductListeners;
import com.example.apinouralborno.R;
import com.example.apinouralborno.databinding.CustomItemStoreBinding;
import com.example.apinouralborno.Model.product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.PVH> {

    List<product> productList;
    Context context;
    ProductListeners listeners;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public ProductAdapter(List<product> productList, Context context, ProductListeners listeners) {
        this.productList = productList;
        this.context = context;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public PVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomItemStoreBinding binding = CustomItemStoreBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return  new PVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PVH holder, int position) {

        product p = productList.get(position);

        holder.title.setText(p.getTitle());
        holder.price.setText(String.valueOf(p.getPrice())+"$");


        Glide.with(context).load(p.getPicture())
                .into(holder.img);

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeners.productDetails(p);
            }
        });



        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tt","onClickAdapter");


                     if (firebaseUser != null) {
                         holder.fav.setImageResource(R.drawable.ic_full_heart);

                     }
                listeners.productFav(p,0);


            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class PVH extends RecyclerView.ViewHolder{

        TextView title , price;
        ImageView img,cart, fav;
        public PVH(@NonNull CustomItemStoreBinding binding) {
            super(binding.getRoot());

            title = binding.TvTitle;
            price = binding.TvPrice;
            img = binding.ImgProduct;
            cart = binding.ImgAddToCart;
            fav = binding.ImgFav;
        }
    }
}
