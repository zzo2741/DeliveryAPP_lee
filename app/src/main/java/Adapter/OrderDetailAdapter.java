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

import model.CartModel;


public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    DatabaseReference mList;
    ArrayList<CartModel> cartModels;

    public OrderDetailAdapter(String oid) {

        System.out.println(oid);

        cartModels = new ArrayList<>();
        mList = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Order").child(oid).child("list");

        mList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartModels.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    cartModels.add(snapshot.getValue(CartModel.class));
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orderdetail_list_row, viewGroup, false);
        return new RowCell4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((RowCell4)viewHolder).name.setText(cartModels.get(i).product_name);
        ((RowCell4)viewHolder).price.setText(cartModels.get(i).total_price);
        ((RowCell4)viewHolder).count.setText(cartModels.get(i).product_count);
        ((RowCell4)viewHolder).total_price.setText(cartModels.get(i).total_price);
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    private class RowCell4 extends RecyclerView.ViewHolder {
        TextView name, price, count, total_price;

        public RowCell4(View view) {
            super(view);

            name = view.findViewById(R.id.textView_detail_name);
            price = view.findViewById(R.id.textView_detail_price);
            count = view.findViewById(R.id.textView_detail_count);
            total_price = view.findViewById(R.id.textView_detail_total_price);
        }
    }
}
