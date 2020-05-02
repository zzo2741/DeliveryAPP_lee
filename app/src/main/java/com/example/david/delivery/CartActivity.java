package com.example.david.delivery;

import android.app.Activity;
import android.content.DialogInterface;
import android.service.autofill.Dataset;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.CartListAdapter;
import model.CartModel;
import model.OrderModel;

public class CartActivity extends AppCompatActivity {

    public static TextView textView_total_price, available_mileage;
    public static Button order;
    EditText use_mileage;
    DatabaseReference mCurrentOrder, mCurrentMileage, mOrderList, mOrder;
    ArrayList<CartModel> cartModels;
    ArrayList<String> pids;
    OrderModel orderModel;
    int total_price, mileage, user_mileage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //total_price = 0;
        cartModels = new ArrayList<>();
        pids = new ArrayList<>();

        mCurrentMileage = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("user_mileage");
        mCurrentOrder = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");
        mOrderList = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Order");

        available_mileage = findViewById(R.id.textView_available_mileage);
        use_mileage = findViewById(R.id.editText_use_mileage);
        use_mileage.setText("0");


        mCurrentMileage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_mileage = Integer.parseInt(dataSnapshot.getValue(String.class));
                available_mileage.setText(String.valueOf(user_mileage));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCurrentOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartModels.clear();
                pids.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    cartModels.add(snapshot.getValue(CartModel.class));
                    pids.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        textView_total_price = findViewById(R.id.textview_totalPrice);
        order = findViewById(R.id.button_order);

        RecyclerView view = findViewById(R.id.cartList_view);

        CartListAdapter cartListAdapter = new CartListAdapter();

        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(cartListAdapter);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePaymentPlan();
            }
        });
    }

    private void ChoosePaymentPlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("결제방식");
        builder.setMessage("결제방식을 선택해주세요");
        builder.setPositiveButton("현금결제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        total_price = Integer.parseInt(textView_total_price.getText().toString());
                        mileage = Integer.parseInt(use_mileage.getText().toString());

                        if(total_price == 0){
                            Toast.makeText(getApplicationContext(),"장바구니에 상품이 없습니다.",Toast.LENGTH_LONG).show();
                        }else if(user_mileage < mileage){
                            Toast.makeText(getApplicationContext(),"보유하신 마일리지의 양보다 많습니다.",Toast.LENGTH_LONG).show();
                        }else if(mileage > total_price){
                            Toast.makeText(getApplicationContext(),"마일리지는 총 금액을 넘을 수 없습니다.",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"현금결제 선택하셨습니다.",Toast.LENGTH_LONG).show();
                            orderModel = new OrderModel(getTime(), String.valueOf(total_price), String.valueOf(mileage), String.valueOf(total_price - mileage), "현금결제");
                            mOrder = mOrderList.push();
                            mOrder.setValue(orderModel);
                            mCurrentMileage.setValue(String.valueOf(user_mileage - mileage + Integer.parseInt(orderModel.total_pay) * 0.1)); // 총 결제 금액의 10퍼센트를 마일리지로 적립

                            for(int i=0; i < cartModels.size(); i++){
                                mOrder.child("list").child(pids.get(i)).setValue(cartModels.get(i));
                            }

                            mCurrentOrder.removeValue();
                            CartActivity.this.finish();
                        }
                    }
                });
        builder.setNegativeButton("카드결제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        total_price = Integer.parseInt(textView_total_price.getText().toString());
                        mileage = Integer.parseInt(use_mileage.getText().toString());

                        if(total_price == 0){
                            Toast.makeText(getApplicationContext(),"장바구니에 상품이 없습니다.",Toast.LENGTH_LONG).show();
                        }else if(user_mileage < mileage){
                            Toast.makeText(getApplicationContext(),"보유하신 마일리지의 양보다 많습니다.",Toast.LENGTH_LONG).show();
                        }else if(mileage > total_price){
                            Toast.makeText(getApplicationContext(),"마일리지는 총 금액을 넘을 수 없습니다.",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"카드결제 선택하셨습니다.",Toast.LENGTH_LONG).show();

                            orderModel = new OrderModel(getTime(), String.valueOf(total_price), String.valueOf(mileage), String.valueOf(total_price - mileage), "카드결제");
                            mOrder = mOrderList.push();
                            mOrder.setValue(orderModel);
                            mCurrentMileage.setValue(String.valueOf(user_mileage - mileage + Integer.parseInt(orderModel.total_pay) * 0.1)); // 총 결제 금액의 10퍼센트를 마일리지로 적립

                            for(int i=0; i < cartModels.size(); i++){
                                mOrder.child("list").child(pids.get(i)).setValue(cartModels.get(i));
                            }

                            mCurrentOrder.removeValue();
                            CartActivity.this.finish();
                        }
                    }
                });
        builder.show();
    }
    private String getTime(){ // 주문날짜 생성
        long mNow;
        Date mDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return sdf.format(mDate);
    }
}
