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

public class SignInActivity extends AppCompatActivity {

    EditText edtUsername,edtPassword;
    Button btnForgot,btnSignIn,btnFacebook,btnGoogle;
    DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        dbHelper = new DBHelper(this);

        //anh xa view log in
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnForgot = findViewById(R.id.btnForgot);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                        edtPassword.setText(username);
//                    Toast.makeText(SignInActivity.this,"Please field username or password",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkUserPass = dbHelper.checkUserPass(username,password);
                    if(checkUserPass == true){
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignInActivity.this,"Please enter valid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

