package com.example.david.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import Adapter.ProductListAdapter;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView view = findViewById(R.id.productList_view);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        ProductListAdapter productListAdapter = new ProductListAdapter();
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(productListAdapter); // 어댑터 등록
    }
}
