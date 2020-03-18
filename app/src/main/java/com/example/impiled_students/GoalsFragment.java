package com.example.impiled_students;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class GoalsFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<GoalsModel> options;
    FirebaseRecyclerAdapter<GoalsModel, GoalsHolder> firebaseRecyclerAdapter;
    LinearLayoutManager Horizontal;
    static DatabaseReference trans, rew;
    static Context ctx;
    Query query;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.goals_fragment,container,false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.goalView);
        trans = FirebaseDatabase.getInstance().getReference("transaction");
        rew = FirebaseDatabase.getInstance().getReference("reward");
        Horizontal = new LinearLayoutManager(getActivity());
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);
        auth = FirebaseAuth.getInstance();
        query = trans.orderByChild("student_id").equalTo(auth.getUid());
        ctx = getActivity();
        showData();
        return rootview;
    }

    public void showData(){
        options = new FirebaseRecyclerOptions.Builder<GoalsModel>().setQuery(query, GoalsModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GoalsModel, GoalsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoalsHolder viewHolder, int i, @NonNull GoalsModel model) {
                final String goals_key = getRef(i).getKey();
                viewHolder.setRewards_id(model.getRewards_id());
                viewHolder.setProgress(model.getProgress());
                viewHolder.setStatus(model.getStatus());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), SingleGoals.class);
                        i.putExtra("goals_key", goals_key);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public GoalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goals_card, parent,false);
                GoalsHolder rewardsViewHolder = new GoalsHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerView.setLayoutManager(Horizontal);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class GoalsHolder extends RecyclerView.ViewHolder{
        View mView;

        public GoalsHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProgress(String progress) {
            TextView prog = (TextView) mView.findViewById(R.id.progress);
            prog.setText(progress + "%");
        }

        public void setStatus(String status) {
            TextView stat = (TextView) mView.findViewById(R.id.status);
            stat.setText(status);
        }

        public void setRewards_id(final String rewards_id) {
            final TextView des = (TextView) mView.findViewById(R.id.rewards_task);
            final TextView name = (TextView) mView.findViewById(R.id.product_name);
            final ImageView ture = (ImageView) mView.findViewById(R.id.product_photo);

            rew.child(rewards_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String rewards_name = (String) dataSnapshot.child("rewards_name").getValue();
                    String rewards_task = (String) dataSnapshot.child("rewards_task").getValue();
                    final String rewards_pic = (String) dataSnapshot.child("rewards_photo").getValue();

                    des.setText(rewards_task);
                    name.setText(rewards_name);

                    Picasso.with(ctx).load(rewards_pic).networkPolicy(NetworkPolicy.OFFLINE).into(ture, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(rewards_pic).into(ture);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
