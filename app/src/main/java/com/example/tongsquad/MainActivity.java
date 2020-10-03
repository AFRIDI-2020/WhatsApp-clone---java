package com.example.tongsquad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private EditText membersNameET, ageET, universityET, departmentET;
    private DatabaseReference databaseReference,memberRef;
    private Button beMemberBtn;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }



    private void init() {
        membersNameET = findViewById(R.id.membersNameET);
        ageET = findViewById(R.id.ageET);
        universityET = findViewById(R.id.universityET);
        departmentET = findViewById(R.id.departmentET);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        memberRef = databaseReference.child("members");
        beMemberBtn = findViewById(R.id.beMemberBtn);
    }

    private void setMemberInfoToDatabase(String memberName, String age, String university, String department) {

        String postKey = memberRef.push().getKey();

        memberRef.child(postKey).setValue(new Member(memberName,age, university,department))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,MemberListActivity.class));
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void beMember(View view) {
        openDialog();
        String memberName = membersNameET.getText().toString();
        String age = ageET.getText().toString();
        String university = universityET.getText().toString();
        String department = departmentET.getText().toString();
        setMemberInfoToDatabase(memberName,age,university,department);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(null);
        builder.setMessage("please wait...");
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

    }


    public void showList(View view) {
        startActivity(new Intent(MainActivity.this,MemberListActivity.class));
    }
}
