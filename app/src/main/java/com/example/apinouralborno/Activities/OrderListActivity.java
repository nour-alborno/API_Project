package com.example.apinouralborno.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.apinouralborno.Adapters.OrderAdapter;
import com.example.apinouralborno.Listeners.OrderListeners;
import com.example.apinouralborno.Model.Order;
import com.example.apinouralborno.databinding.ActivityOrderListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    ActivityOrderListBinding binding;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore.collection("Order").document(firebaseUser).collection("userOrder")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Order> orderList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Order order = document.toObject(Order.class);
                                order.setDocumentId(document.getId());

                                orderList.add(order);
                            }

                            OrderAdapter adapter = new OrderAdapter(orderList, getBaseContext(), new OrderListeners() {
                                @Override
                                public void delete(Order order) {
                                    firestore.collection("Order").document(firebaseUser)
                                            .collection("userOrder")
                                            .document(order.getDocumentId())
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){
                                                        Toast.makeText(OrderListActivity.this, "Ordered is cancelled", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(OrderListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                }
                            });
                            binding.Rv.setAdapter(adapter);
                            binding.Rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


                        } else {
                            task.getException().getMessage();
                            Toast.makeText(OrderListActivity.this, "You haven't ordered yet.", Toast.LENGTH_SHORT).show();
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