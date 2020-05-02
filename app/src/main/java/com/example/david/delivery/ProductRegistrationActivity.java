package com.example.david.delivery;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.ProductModel;

public class ProductRegistrationActivity extends AppCompatActivity {

    TextInputEditText productName, productPrice, productDescription;
    Button productRegistration;
    ProductModel productModel;
    DatabaseReference mProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);

        mProductRef = FirebaseDatabase.getInstance().getReference();
        productName = findViewById(R.id.edittext_productName);
        productPrice = findViewById(R.id.edittext_productPrice);
        productDescription = findViewById(R.id.edittext_productDescription);

        productRegistration = findViewById(R.id.button_productRegistration);

        productRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productName.getText().length() == 0 || productPrice.getText().length() == 0 || productDescription.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    productModel = new ProductModel(productName.getText().toString(), productPrice.getText().toString(), productDescription.getText().toString());
                    mProductRef.child("product").push().setValue(productModel); // push = id 생성
                    Toast.makeText(getApplicationContext(), "상품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
