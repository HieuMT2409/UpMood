package com.example.upmood.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.upmood.Activity.ForgotPasswordActivity;
import com.example.upmood.Activity.MainActivity;
import com.example.upmood.R;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;


public class LoginFragment extends Fragment {

    private FirebaseAuth auth;

    private EditText edtUsername,edtPassword,edtEmailuser;
    private Button btnForgot,btnSignIn,btnSendEmail,btnFacebook,btnGoogle;
    private static final String ARG_PARAM1 = "Sign In";
    private static final String ARG_PARAM2 = "Sign Up";
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();

        //xử lý facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        // anh xa view
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtEmailuser = view.findViewById(R.id.edtEmailuser);
        btnForgot = view.findViewById(R.id.btnForgot);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSendEmail = view.findViewById(R.id.btnSendEmail);
        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        progressDialog = new ProgressDialog(getContext());



        btnSignIn.setOnClickListener(view1 -> {
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            //xu ly dang nhap
            if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(!password.isEmpty()){
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                progressDialog.dismiss();
                                //neu dang nhap thanh cong thi chuyen sang trang home
                                Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().onBackPressed();

                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            });
                }else{
                    //sai mat khau
                    edtPassword.setError("Mật khẩu không chính xác");
                }
            }else if(email.isEmpty()){
                edtUsername.setError("Email không được bỏ trống");
            }else{
                edtUsername.setError("Email không chính xác");
            }
        });

        //xu ly quen mat khau
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}