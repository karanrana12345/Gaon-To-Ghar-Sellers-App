package com.gg.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView alreadyhaveAccount;
    private EditText fullname;
    private EditText email;
    private EditText address;
    private EditText phonenumber;
    private EditText password;
    private EditText confirmpassword;
    private ImageButton closeBtn;
    private Button signUpBtn;
    private ProgressDialog pd;
    private FirebaseFirestore firebaseFirestore;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        alreadyhaveAccount = findViewById(R.id.logintxt);
        fullname = findViewById(R.id.signup_fullname);
        email = findViewById(R.id.signup_email);
        address = findViewById(R.id.signup_address);
        phonenumber = findViewById(R.id.signup_phonenumber);
        password = findViewById(R.id.signup_password);
        confirmpassword = findViewById(R.id.signup_confirmpassword);

        signUpBtn = findViewById(R.id.signup_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        alreadyhaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(alreadyhaveAccount,"emailTransition");
                pairs[1] = new Pair<View,String>(alreadyhaveAccount,"passwordTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);

                startActivity(intent,options.toBundle());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPassword();
            }
        });

    }

    private void checkEmailandPassword()
    {
        String femail = email.getText().toString();
        String fpassword = password.getText().toString();
        String fconfirmpassword = confirmpassword.getText().toString();
        String ffullname = fullname.getText().toString();
        String fphonenumber = phonenumber.getText().toString();
        String faddress = address.getText().toString();

        if (TextUtils.isEmpty(femail)) {
            email.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(faddress))
        {
            address.setError("Address is required");
            return;
        }

        if (TextUtils.isEmpty(fpassword)) {
            password.setError("Password is required");
            return;
        }

        if (password.length() < 8) {
            password.setError("Password must be of 8 characters");
            return;
        }

        if (TextUtils.isEmpty(ffullname)) {
            fullname.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(fphonenumber)) {
            phonenumber.setError("Phonenumber is required");
            return;
        }

        if (TextUtils.isEmpty(fconfirmpassword))
        {
            confirmpassword.setError("Password is required");
            return;
        }

        if (femail.matches(emailpattern))
        {
            if (fpassword.equals(fconfirmpassword))
            {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setTitle("Please wait");
                pd.setMessage("Creating your account...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Map<String,Object> userdata = new HashMap<>();
                            userdata.put("Fullname",ffullname);
                            userdata.put("Email",femail);
                            userdata.put("Phonenumber",fphonenumber);
                            userdata.put("Address",faddress);
                            userdata.put("Profile","");

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Toast.makeText(RegisterActivity.this,"You are registered !",Toast.LENGTH_SHORT).show();
                                        mainIntent();
                                    }
                                    else
                                    {
                                        pd.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            pd.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
            else
            {
                confirmpassword.setError("Password doesn't match !");
            }
        }
        else
        {
            email.setError("Invalid Email !");
        }
    }
    private void mainIntent()
    {
        Intent mainintent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainintent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null)
        {
            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}