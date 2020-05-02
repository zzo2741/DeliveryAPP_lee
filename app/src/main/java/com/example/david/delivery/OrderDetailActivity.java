package com.example.david.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import Adapter.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    public String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        oid = intent.getExtras().getString("oid");



        RecyclerView view = findViewById(R.id.orderdetaillist_view);

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(oid);

        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(orderDetailAdapter);
    }
}
