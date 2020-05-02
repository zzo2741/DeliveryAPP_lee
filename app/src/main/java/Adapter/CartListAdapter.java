package Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.david.delivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.CartModel;

import static com.example.david.delivery.CartActivity.textView_total_price;


public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<CartModel> cartModels;
    DatabaseReference mCart;
    ArrayList<String> pids;

    public CartListAdapter() {
        cartModels = new ArrayList<>();
        pids = new ArrayList<>();

        System.out.println("생성자");

        mCart = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        mCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total_price = 0;
                cartModels.clear();
                pids.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    cartModels.add(snapshot.getValue(CartModel.class));
                    pids.add(snapshot.getKey());
                    total_price += Integer.parseInt(snapshot.child("total_price").getValue().toString());
                }
                textView_total_price.setText(String.valueOf(total_price));
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_list_row, viewGroup, false);
        return new RowCell2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        ((RowCell2)viewHolder).cart_name.setText(cartModels.get(i).product_name);
        ((RowCell2)viewHolder).cart_price.setText(cartModels.get(i).product_price);
        ((RowCell2)viewHolder).cart_count.setText(cartModels.get(i).product_count);
        ((RowCell2)viewHolder).cart_totalPrice.setText(cartModels.get(i).total_price);

        ((RowCell2)viewHolder).cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCart.child(pids.get(i)).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    private  class RowCell2 extends RecyclerView.ViewHolder {
        TextView cart_name, cart_price, cart_count, cart_totalPrice;
        Button cancle;

        public RowCell2(View view) {
            super(view);

            cart_name = itemView.findViewById(R.id.textView_cart_productName);
            cart_price = itemView.findViewById(R.id.textview_cart_price);
            cart_count = itemView.findViewById(R.id.textView_cart_count);
            cart_totalPrice = itemView.findViewById(R.id.textview_cart_totalPrice);
            cancle = itemView.findViewById(R.id.button_cancle);
        }
    }
}
