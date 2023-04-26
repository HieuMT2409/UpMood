package com.example.upmood;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginFragment extends Fragment {

    private FirebaseAuth auth;

    private EditText edtUsername,edtPassword;
    private Button btnForgot,btnSignIn,btnFacebook,btnGoogle;
    private static final String ARG_PARAM1 = "Sign In";
    private static final String ARG_PARAM2 = "Sign Up";
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;


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

        printHashKey(getContext());

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();

        //xử lý facebook
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(getContext());

        // anh xa view
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnForgot = view.findViewById(R.id.btnForgot);
        btnSignIn = view.findViewById(R.id.btnSignIn);
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
                                startActivity(new Intent(getContext(),MainActivity.class));
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

        return view;
    }


}