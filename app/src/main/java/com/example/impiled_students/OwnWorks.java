package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class OwnWorks extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<WritingModel, OwnViewHolder> postfirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<WritingModel> postoptions;
    LinearLayoutManager Horizontal;
    DatabaseReference postjourn;
    FirebaseAuth auth;
    ImageView back;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_works);
        recyclerView = (RecyclerView) findViewById(R.id.ownworks);
        recyclerView.setHasFixedSize(true);
        Horizontal = new LinearLayoutManager(this);
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);
        postjourn = FirebaseDatabase.getInstance().getReference("journ_post");
        auth = FirebaseAuth.getInstance();
        query = postjourn.orderByChild("journ_author").equalTo(auth.getUid());
        back = (ImageView) findViewById(R.id.backers2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        showPostdata();
    }

    public void showPostdata(){
        postoptions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(query, WritingModel.class).build();

        postfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WritingModel, OwnViewHolder>(postoptions) {
            @Override
            protected void onBindViewHolder(@NonNull OwnViewHolder viewHolder, int i, @NonNull WritingModel model) {
                final String Works_Id = getRef(i).getKey();
                viewHolder.setJourn_title(model.getJourn_title());
                viewHolder.setJourn_category(model.getJourn_category());
                viewHolder.setJourn_photo(getApplicationContext(), model.getJourn_photo());
                viewHolder.setJourn_content(model.getJourn_content());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), SingleOwnWorks.class);
                        i.putExtra("works_key", Works_Id);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public OwnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.works_cardv2, parent,false);
                OwnViewHolder rewardsViewHolder = new OwnViewHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerView.setLayoutManager(Horizontal);
        postfirebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(postfirebaseRecyclerAdapter);

    }

    public static class OwnViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public OwnViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setJourn_title(String title){
            TextView Title = (TextView) mView.findViewById(R.id.title);
            Title.setText(title);
        }

        public void setJourn_content(String content){
            TextView Content = (TextView) mView.findViewById(R.id.content);
            Content.setText(content);
        }

        public void setJourn_category(String cat){

        }

        public void setJourn_photo(final Context ctx, final String image){
            final ImageView rewards_photo = (ImageView) mView.findViewById(R.id.photo);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(rewards_photo, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(rewards_photo);
                }
            });
        }
    }
}
