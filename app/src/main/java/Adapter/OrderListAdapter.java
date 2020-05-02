package Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.delivery.OrderDetailActivity;
import com.example.david.delivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.OrderModel;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<OrderModel> orderModels;
    ArrayList<String> oids;
    DatabaseReference mOrder;

    public OrderListAdapter() {
        orderModels = new ArrayList<>();
        oids = new ArrayList<>();


        mOrder = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Order");
        mOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderModels.clear();
                oids.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    orderModels.add(snapshot.getValue(OrderModel.class));
                    oids.add(snapshot.getKey());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_row, viewGroup, false);
        return new RowCell3(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        ((RowCell3)viewHolder).order_date.setText(orderModels.get(i).order_date);
        ((RowCell3)viewHolder).total_price.setText(orderModels.get(i).total_price);
        ((RowCell3)viewHolder).used_mileage.setText(orderModels.get(i).used_mileage);
        ((RowCell3)viewHolder).total_pay.setText(orderModels.get(i).total_pay);
        ((RowCell3)viewHolder).payment_plan.setText(orderModels.get(i).payment_plan);

        ((RowCell3)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                intent.putExtra("oid",oids.get(i));
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    private class RowCell3 extends RecyclerView.ViewHolder {
        TextView order_date, total_price, used_mileage, total_pay, payment_plan;

        public RowCell3(View view) {
            super(view);

            order_date = view.findViewById(R.id.textView_order_date);
            total_price = view.findViewById(R.id.textView_order_total_price);
            used_mileage = view.findViewById(R.id.textView_order_used_mileage);
            total_pay = view.findViewById(R.id.textView_order_total_pay);
            payment_plan = view.findViewById(R.id.textView_order_payment_plan);
        }
    }
}
