package com.QuickChatApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.QuickChatApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
  TextView txt_signUp,signin_btn;
  EditText login_Email,login_Password;
  FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

        txt_signUp=findViewById(R.id.text_signup);
        signin_btn=findViewById(R.id.signin_btn);
        login_Password=findViewById(R.id.login_Password);
        login_Email=findViewById(R.id.login_Email);
        closeKeyBoard();


        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= login_Email.getText().toString();

                String password= login_Password.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Entre Valid Data",Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    login_Email.setError("EnValid EMAIL");
                    Toast.makeText(LoginActivity.this,"EnValid EMAIL",Toast.LENGTH_SHORT).show();
                } else if(password.length()<6){
                    login_Password.setError("EnValid password");
                    Toast.makeText(LoginActivity.this,"Please Enter Valid Password",Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this,"Error IN login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }
    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}