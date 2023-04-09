package com.example.upmood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText edtEmailSignUp,edtPasswordSignUp,edtConfirmPasswordSignUp,edtPhone;
    private Button btnSignup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

        //khai bao firebase
        auth = FirebaseAuth.getInstance();

        //anh xa view tu fragment_sign_up
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtPhone = findViewById(R.id.edtPhone);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtConfirmPasswordSignUp = findViewById(R.id.edtConfirmPasswordSignUp);
        btnSignup = findViewById(R.id.btnSignUp);

        //xu ly nut Sign Up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dangki","DAng ki");
                String email = edtEmailSignUp.getText().toString().trim();
                String password = edtPasswordSignUp.getText().toString().trim();
                String re_password = edtConfirmPasswordSignUp.getText().toString().trim();

                //xu ly khi o email trong
                if(email.isEmpty()){
                    edtEmailSignUp.setError("Email không được để trống");
                }

                //xu ly khi o password trong
                if(password.isEmpty()){
                    edtPasswordSignUp.setError("Password không được để trống");
                }else {
                    if(password.equals(re_password)){
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                                }else {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(SignUpActivity.this, "Mật khẩu nhập lại không chính xác !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
