package com.example.david.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.UserSignupModel;

public class UserInfoActivity extends AppCompatActivity {

    TextView name, email, password, mileage;
    Button modify;
    DatabaseReference mDatabase;
    String uid;
    UserSignupModel user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name = findViewById(R.id.textview_userName);
        email = findViewById(R.id.textView_userEmail);
        password = findViewById(R.id.textview_userPassword);
        mileage = findViewById(R.id.textView_userMileage);
        modify = findViewById(R.id.button_modifyInfo);
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getUid();
        System.out.println("uid = " + uid);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        mDatabase.addValueEventListener(new ValueEventListener() { //데이터베이스 읽어올때
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserSignupModel.class);

                name.setText("이름 = " + user.user_name);
                email.setText("이메일 = " +user.user_email);
                password.setText("비밀번호 = " + user.user_password);
                mileage.setText("마일리지 = " + user.user_mileage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, ModifyInfoActivity.class);
                startActivity(intent);
            }
        });
    }

}
