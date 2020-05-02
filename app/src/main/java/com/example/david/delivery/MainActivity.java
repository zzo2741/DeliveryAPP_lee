package com.example.david.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    Button userInfo, foodList, cart, addMenu, currentOrder, signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userInfo = findViewById(R.id.button_userInfo);
        foodList = findViewById(R.id.button_foodList);
        cart = findViewById(R.id.button_cart);
        addMenu = findViewById(R.id.button_addMenu);
        currentOrder = findViewById(R.id.button_currentOrder);
        signOut = findViewById(R.id.button_signout);

        userInfo.setOnClickListener(this);
        foodList.setOnClickListener(this);
        cart.setOnClickListener(this);
        addMenu.setOnClickListener(this);
        currentOrder.setOnClickListener(this);
        signOut.setOnClickListener(this);

    }

    @Override
    public void onStart() { // 시작 시 호출
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); //현재 유저의 정보 가져옴
        if(currentUser == null){ //로그인된 유저 없음
            Intent intent = new Intent(MainActivity.this, LoginActivity.class); //로그인 액티비티로 이동
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) { //버튼클릭 이벤트 처리
        Intent intent;
        switch(view.getId()){
            case R.id.button_userInfo :
                intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_foodList :
                intent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(intent);
                break;
            case R.id.button_cart :
                intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.button_addMenu :
                intent = new Intent(MainActivity.this, ProductRegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.button_currentOrder :
                intent = new Intent(MainActivity.this, OrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.button_signout :
                mAuth.signOut();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}