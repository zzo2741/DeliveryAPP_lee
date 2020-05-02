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
import model.ProductModel;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    DatabaseReference mProduct, mCart;
    ArrayList<ProductModel> productModels;
    ArrayList<String> pids;

    public ProductListAdapter() { //생성자
        productModels = new ArrayList<>();
        pids = new ArrayList<>();

        mProduct = FirebaseDatabase.getInstance().getReference().child("product");
        mCart = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        mProduct.addValueEventListener(new ValueEventListener() { //데이터베이스 읽기
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //데이터베이스 변경마다 호출
                productModels.clear();
                pids.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){ // 데이터베이스 가져옴
                    productModels.add(snapshot.getValue(ProductModel.class)); //product model 클래스 구조로 받아옴
                    //System.out.println("갯수 = " + productModels.size());
                    //System.out.println("uid = " + snapshot.getKey());
                    pids.add(snapshot.getKey());// 제품 id 가져옴
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { // 리사이클러뷰에서 recycler row 를 view로 지정
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_row, viewGroup, false);
        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) { // 매 항목의
        ((RowCell)viewHolder).product_name.setText(productModels.get(i).product_name);
        ((RowCell)viewHolder).product_price.setText(productModels.get(i).product_price);
        ((RowCell)viewHolder).product_description.setText(productModels.get(i).product_description);

        ((RowCell)viewHolder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // + 버튼 클릭시
                int count = 0;
                count = Integer.parseInt(((RowCell)viewHolder).product_count.getText().toString());
                count += 1;
                ((RowCell)viewHolder).product_count.setText(String.valueOf(count));
            }
        });

        ((RowCell)viewHolder).minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // - 버튼 클릭시
                int count = 0;
                count = Integer.parseInt(((RowCell)viewHolder).product_count.getText().toString());
                if(count <= 0){
                }else{
                    count -= 1;
                }
                ((RowCell)viewHolder).product_count.setText(String.valueOf(count));
            }
        });

        ((RowCell)viewHolder).add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 담기 버튼 클릭시
                CartModel cartModel = new CartModel(
                        ((RowCell)viewHolder).product_name.getText().toString(),
                        ((RowCell)viewHolder).product_price.getText().toString(),
                        ((RowCell)viewHolder).product_count.getText().toString(),
                        String.valueOf(Integer.parseInt(
                                ((RowCell)viewHolder).product_count.getText().toString()) *
                                Integer.parseInt(
                                        ((RowCell)viewHolder).product_price.getText().toString())));
                mCart.child(pids.get(i)).setValue(cartModel);
            }
        });
    }

    @Override
    public int getItemCount() {

        return productModels.size();

    }

    private static class RowCell extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_description, product_count;
        Button plus, minus, add;

        public RowCell(View view) {
            super(view);

            product_name = view.findViewById(R.id.textview_productName);
            product_price = view.findViewById(R.id.textview_productPrice);
            product_description = view.findViewById(R.id.textview_productDescription);

            product_count = view.findViewById(R.id.textview_productCount);

            plus = view.findViewById(R.id.button_plus);
            minus = view.findViewById(R.id.button_minus);
            add = view.findViewById(R.id.button_addToCart);
        }
    }
}
