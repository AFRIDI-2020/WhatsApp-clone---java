package com.example.tongsquad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongsquad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private TextView needNewAccount, forgetPasswordLink;
    private EditText logInEmail, logInPassword;
    private Button logInBtn, phoneLogInBtn;
    private FirebaseAuth firebaseAuth;
    private AlertDialog logInDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();
        makeAlertDialog();

        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
        
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    }

    private void makeAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setTitle("Log in");
        builder.setMessage("Please wait for logging in.");
        builder.setCancelable(false);
        logInDialog = builder.create();
    }

    private void AllowUserToLogin() {
        String email = logInEmail.getText().toString();
        String password = logInPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email is empty!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password is empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            logInDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                logInDialog.dismiss();
                                sendUserToWhatsAppActivity();
                            }
                            else {
                                logInDialog.dismiss();
                                Toast.makeText(LogInActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToWhatsAppActivity() {
        Intent intent = new Intent(LogInActivity.this,WhatsAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void goToRegisterActivity() {
        startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
    }

    private void init() {
        needNewAccount = findViewById(R.id.need_new_account);
        logInBtn = findViewById(R.id.login_button);
        logInEmail = findViewById(R.id.login_email);
        logInPassword = findViewById(R.id.login_password);
        phoneLogInBtn = findViewById(R.id.phone_login_button);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            goToWhatsAppActivity();
        }
    }

    private void goToWhatsAppActivity() {
        startActivity(new Intent(LogInActivity.this, WhatsAppActivity.class));
    }
}
