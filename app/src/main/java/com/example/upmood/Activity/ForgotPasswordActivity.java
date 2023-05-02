package com.example.upmood.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.upmood.Fragment.LoginFragment;
import com.example.upmood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmailuser;
    private Button btnSendEmail;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot_password);

        //anh xa view
        edtEmailuser = findViewById(R.id.edtEmailuser);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        progressDialog = new ProgressDialog(this);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = edtEmailuser.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginFragment.class);
                                startActivity(intent);
                            }
                        });
            }
        });

    }
}
