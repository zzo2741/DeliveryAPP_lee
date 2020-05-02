package com.example.david.delivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.UserSignupModel;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText email, password, name;
    Button register;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.edittext_signup_email);
        password = findViewById(R.id.edittext_signup_password);
        name = findViewById(R.id.edittext_signup_name);
        register = findViewById(R.id.button_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 가입하기 버튼 클릭시
                if(email.getText().length() == 0 || password.getText().length() == 0 || name.getText().length() == 0){ // 빈칸체크
                    Toast.makeText(getApplicationContext(), "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{ // 값이 입력되면 회원가입 진행
                    UserSignupModel userSignupModel = new UserSignupModel(email.getText().toString(), password.getText().toString(), name.getText().toString(), "30000");
                    signUp(userSignupModel);
                }
            }
        });
    }

    public void signUp(final UserSignupModel userSignupModel){

        final ProgressDialog asyncDialog = new ProgressDialog(
                SignUpActivity.this);

        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("wait..");
        asyncDialog.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(userSignupModel.user_email, userSignupModel.user_password) //계정생성
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 가입 성공시
                            mDatabase.child("users").child(task.getResult().getUser().getUid()).setValue(userSignupModel); //디비에 구조체 틀대로 저장
                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity LA = (LoginActivity) LoginActivity.Login_activity; //로그인 액티비티 종료
                            LA.finish();
                            finish();

                        } else { //가입 실패시
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }
}
