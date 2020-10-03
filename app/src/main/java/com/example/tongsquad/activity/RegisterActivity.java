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

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword;
    private Button registerBtn;
    private TextView alreadyHaveAccount;
    private FirebaseAuth firebaseAuth;
    private AlertDialog registerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        makeAlertDialog();

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLogInActivity();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });


    }

    private void makeAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Create Account");
        builder.setMessage("Please, wait while we are creating your account.");
        builder.setCancelable(false);
        registerDialog = builder.create();
    }

    private void createNewAccount() {
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password is empty!", Toast.LENGTH_SHORT).show();
        } else {
            registerDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendUserToWhatsAppActivity();
                                registerDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                            } else {
                                registerDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToWhatsAppActivity() {
        Intent intent = new Intent(RegisterActivity.this,WhatsAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToLogInActivity() {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    private void init() {
        registerBtn = findViewById(R.id.register_button);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        alreadyHaveAccount = findViewById(R.id.already_have_account);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
