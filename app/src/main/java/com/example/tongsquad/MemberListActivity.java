package com.example.tongsquad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MemberListActivity extends AppCompatActivity {

    private RecyclerView memberListRV;
    private FirebaseRecyclerAdapter<Member,ViewHolder> allMembersAdapter;
    private DatabaseReference memberRef;
    private EditText searchItemET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        init();
        fetchAllData("");
        searchItemET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString()!=null){
                    fetchAllData(s.toString().toUpperCase());
                }
                else
                {
                    fetchAllData("");
                }
            }
        });

    }

    private void init() {
        memberListRV = findViewById(R.id.memberListRV);
        memberListRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MemberListActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        memberListRV.setLayoutManager(layoutManager);
        memberRef = FirebaseDatabase.getInstance().getReference().child("members");
        searchItemET = findViewById(R.id.searchItemET);
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView memberNameTV, ageTV, universityTV, departmentTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberNameTV = itemView.findViewById(R.id.memberNameTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            universityTV = itemView.findViewById(R.id.universityTV);
            departmentTV = itemView.findViewById(R.id.departmentTV);
        }
    }

    private void fetchAllData(String data) {
        Query query = memberRef.orderByChild("department").startAt(data).endAt(data + "\uf8ff");

        FirebaseRecyclerOptions<Member> options
                = new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();


        allMembersAdapter = new FirebaseRecyclerAdapter<Member, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {

                holder.departmentTV.setText(model.getDepartment());
                holder.universityTV.setText(model.getUniversity());
                holder.memberNameTV.setText(model.getMemberName());
                holder.ageTV.setText(model.getAge());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_member_demo_on_memeberlist,parent,false);

                return new ViewHolder(view);
            }
        };


        allMembersAdapter.startListening();
        memberListRV.setAdapter(allMembersAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        allMembersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        allMembersAdapter.stopListening();
    }
}
