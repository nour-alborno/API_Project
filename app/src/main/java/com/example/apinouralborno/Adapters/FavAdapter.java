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
import com.example.apinouralborno.databinding.CustomItemFavBinding;
import com.example.apinouralborno.Model.product;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FVH> {

    ArrayList<product> productArrayList;
    Context context;
    ProductListeners listeners;



    public FavAdapter(ArrayList<product> productArrayList, Context context, ProductListeners listeners) {
        this.productArrayList = productArrayList;
        this.context = context;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public FVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomItemFavBinding binding=CustomItemFavBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FVH holder, int position) {

       product product = productArrayList.get(position);

        holder.title.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice())+"$");


        Glide.with(context).load(product.getPicture())
                .into(holder.img);

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeners.productDetails(product);
            }
        });



        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tt","onClickAdapter");



                holder.fav.setImageResource(R.drawable.ic_empty_heart);


                listeners.productFav(product, holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class FVH extends RecyclerView.ViewHolder{
        TextView title , price;
        ImageView img,cart, fav;
        public FVH(@NonNull CustomItemFavBinding binding) {
            super(binding.getRoot());


            title = binding.TvTitle;
            price = binding.TvPrice;
            img = binding.ImgProduct;
            cart = binding.ImgAddToCart;
            fav = binding.ImgFav;
        }
    }
}
