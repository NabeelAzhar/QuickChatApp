package com.QuickChatApp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.QuickChatApp.R;
import com.QuickChatApp.clases.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
TextView text_signip,btn_SignUp;
CircleImageView profile_image;
EditText regs_Name,regs_Email,regs_Password,regs_ConPassword;
FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage(" Please wait......");
        progressDialog.setCancelable(false);


        setContentView(R.layout.activity_registration);
        text_signip=findViewById(R.id.text_signip);
        profile_image=findViewById(R.id. profile_image);
        regs_Name=findViewById(R.id. regs_Name);
        regs_Email=findViewById(R.id.regs_Email);
       regs_Password=findViewById(R.id.regs_Password);
        regs_ConPassword=findViewById(R.id.regs_ConPassword);
        btn_SignUp=findViewById(R.id.btn_SignUp);
        closeKeyBoard();
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=regs_Name.getText().toString();
                String email=regs_Email.getText().toString();
                String password=regs_Password.getText().toString();
                String cPassword=regs_ConPassword.getText().toString();
                String status=" Hey There I'M USING";
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(email)||
                        TextUtils.isEmpty(password)||TextUtils.isEmpty(cPassword)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"please entre valid data",Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    regs_Email.setError("plese entre valid email");
                    Toast.makeText(RegistrationActivity.this,"plese entre valid email",Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cPassword)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"password does not match",Toast.LENGTH_SHORT).show();
                } else if( password.length()<6){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"entre 6 charter password",Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            try {
                                if (task.isSuccessful()) {
                                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                    StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
                                    if (imageUri != null) {
                                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            imageURI = uri.toString();
                                                            User users = new User(auth.getUid(), name, email, imageURI,status);
                                                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressDialog.dismiss();
                                                                        startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                                    } else {
                                                                        Toast.makeText(RegistrationActivity.this, "ERROR IN CREATE USER", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });


                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        String status=" Hey There I'M USING";
                                        imageURI = "https://firebasestorage.googleapis.com/v0/b/quickchatapp-8b9d7.appspot.com/o/profile-icon-png." +
                                                "png?alt=media&token=3e6da8b2-d46b-4ab4-b46c-5c0f80f647ed";
                                        User users = new User(auth.getUid(), name, email, imageURI,status);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "ERROR IN CREATE USER", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    progressDialog.dismiss();

                                    Toast.makeText(RegistrationActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", task.getException().toString());
                                }
                            }catch (Exception ex){
                                Log.e("TAG", ex.getMessage().toString());
                            }
                        }
                    });

                }

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        text_signip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode==10){
             if (data!= null){
                  imageUri= data.getData();
                 profile_image.setImageURI(imageUri);
             }
         }

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