package com.example.upmood.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.upmood.Activity.LoginActivity;
import com.example.upmood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText edtEmailSignUp,edtPasswordSignUp,edtConfirmPasswordSignUp,edtPhone,edtUserSignUp;
    private Button btnSignup;
    private ProgressDialog progressDialog;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //khai bao bien, anh xa view
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        auth = FirebaseAuth.getInstance();

        btnSignup = view.findViewById(R.id.btnSignUp);
        edtEmailSignUp = view.findViewById(R.id.edtEmailSignUp);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtPasswordSignUp = view.findViewById(R.id.edtPasswordSignUp);
        edtConfirmPasswordSignUp = view.findViewById(R.id.edtConfirmPasswordSignUp);
        edtUserSignUp = view.findViewById(R.id.edtUserSignUp);
        progressDialog = new ProgressDialog(getContext());

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        progressDialog.show();
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getContext(), LoginActivity.class));
                                }else {
                                    Toast.makeText(getContext(), "Đăng ký thất bại !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Mật khẩu nhập lại không chính xác !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}