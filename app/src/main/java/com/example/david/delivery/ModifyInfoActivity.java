package com.example.david.delivery;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.UserSignupModel;

public class ModifyInfoActivity extends AppCompatActivity { // 사용자 정보 수정

    TextInputEditText email, password, name;
    DatabaseReference mUser;
    UserSignupModel user;
    Button modify;
    FirebaseUser cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        email = findViewById(R.id.textview_modify_email);
        password = findViewById(R.id.textview_modify_password);
        name = findViewById(R.id.textview_modify_name);
        modify = findViewById(R.id.button_modify);

        user = new UserSignupModel();

        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        cUser = FirebaseAuth.getInstance().getCurrentUser();

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserSignupModel.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        email.setText(user.user_email);
        password.setText(user.user_password);
        name.setText(user.user_name);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length() * password.getText().length() * name.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"전부 입력해주세요.",Toast.LENGTH_LONG).show();
                }else if(password.getText().length() < 8) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 8자리 이상으로 설정해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    cUser.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mUser.child("user_email").setValue(email.getText().toString());
                        }
                    });

                    cUser.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mUser.child("user_password").setValue(password.getText().toString());
                        }
                    });

                    mUser.child("user_name").setValue(name.getText().toString());
                    Toast.makeText(getApplicationContext(),"수정 성공",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
