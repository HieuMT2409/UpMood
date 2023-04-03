package com.example.upmood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText edtUsername,edtPassword,edtConfirmPassword;
    Button btnSignup;
    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignup = findViewById(R.id.btnSignUp);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String re_password = edtConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(re_password)){
                    Toast.makeText(SignUpActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(re_password)){
                        Boolean checkUser = dbHelper.checkUsername(username); // kiem tra co user ton tai khong
                        if(checkUser == true){
                            Toast.makeText(SignUpActivity.this,"Username available", Toast.LENGTH_SHORT).show();
                        }else{
                            Boolean insert = dbHelper.insertData(username,password);
                            if(insert == true){
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("username",username);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this,"Re-entered password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
