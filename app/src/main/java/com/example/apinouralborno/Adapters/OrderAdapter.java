package com.example.apinouralborno.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apinouralborno.Model.Order;
import com.example.apinouralborno.Listeners.OrderListeners;
import com.example.apinouralborno.databinding.CustiomItemSingleProductBinding;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OVH> {
    List<Order> orderList;
    Context context;
    OrderListeners listeners;



    public OrderAdapter(List<Order> orderList, Context context, OrderListeners listeners) {
        this.orderList = orderList;
        this.context = context;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public OVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustiomItemSingleProductBinding binding = CustiomItemSingleProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OVH holder, int position) {
            Order o = orderList.get(position);

            holder.quantity.setText("Quantity: "+o.getQuantity());
            holder.price.setText(o.getPrice());
            holder.title.setText(o.getTitle());

        Glide.with(context).load(o.getPicture()).into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeners.delete(o);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OVH extends RecyclerView.ViewHolder{
       ImageView img, delete;
       TextView title , price , quantity;
        public OVH(@NonNull CustiomItemSingleProductBinding binding) {
            super(binding.getRoot());

         img = binding.ImgProduct;
         title = binding.TvTitle;
         price = binding.TvPrice;
         quantity = binding.TvQuantity;
         delete = binding.ImgCancel;
        }
    }
}
