package com.example.david.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import Adapter.CartListAdapter;
import Adapter.OrderListAdapter;

public class OrderListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        RecyclerView view = findViewById(R.id.OrderList_view);

        OrderListAdapter orderListAdapter = new OrderListAdapter();

        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(orderListAdapter);
    }
}
