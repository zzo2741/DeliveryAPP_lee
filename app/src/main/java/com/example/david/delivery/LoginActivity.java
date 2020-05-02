package com.example.david.delivery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.UserSignupModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button signin_button, signup_button;
    TextInputEditText user_email, user_password;
    String email, password;

    public static Activity Login_activity;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mdatabase;
    FirebaseUser userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_activity = LoginActivity.this;

        mAuth = FirebaseAuth.getInstance(); //사용자 인증 객체
        mdatabase = firebaseDatabase.getInstance().getReference(); //데이터베이스 주소

        signin_button = findViewById(R.id.button_signin); // 로그인 버튼
        signup_button = findViewById(R.id.button_signup); // 회원가입 버튼
        user_email = findViewById(R.id.edittext_email); // 유저 아이디 입력란
        user_password = findViewById(R.id.edittext_password); // 유저 비밀번호 입력란

        signin_button.setOnClickListener(this);
        signup_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_signin : // 로그인 버튼 클릭 시
                if(user_email.getText().length() == 0 || user_password.getText().length() == 0){ //값을 입력했는지 검사
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                email = user_email.getText().toString();
                password = user_password.getText().toString();

                signIn(email, password);
                break;

            case R.id.button_signup : // 회원가입 버튼 클릭 시

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                break;

        }
    }

    public void signIn(String email, String password){
        final ProgressDialog asyncDialog = new ProgressDialog(
                LoginActivity.this); //프로그래스 진행바

        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("wait..");
        asyncDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //로그인 성공시
                            asyncDialog.dismiss(); // 프로그래스바 사라짐
                            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(final String email, final String password){
        final ProgressDialog asyncDialog = new ProgressDialog(
                LoginActivity.this);

        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("wait..");
        asyncDialog.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    public void signOut(){
        mAuth.signOut();    }

}
