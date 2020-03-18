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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SingleWorksCategory extends AppCompatActivity {
    private String Works_Category = null;
    DatabaseReference cat, postjourn;
    TextView cat_name;
    RecyclerView recyclerviewWorks;
    ImageView back;
    FirebaseRecyclerAdapter<WritingModel, WorksFragment.WorksHolder> postfirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<WritingModel> postoptions;
    LinearLayoutManager Horizontal;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_works_category);
        Works_Category = getIntent().getExtras().getString("works_category");
        cat_name = (TextView) findViewById(R.id.catworks);
        cat = FirebaseDatabase.getInstance().getReference("journ_cate");
        postjourn = FirebaseDatabase.getInstance().getReference("journ_post");
        query = postjourn.orderByChild("journ_category").equalTo(Works_Category);
        back = (ImageView) findViewById(R.id.backers);
        recyclerviewWorks = (RecyclerView) findViewById(R.id.recyclerviewWorks);
        recyclerviewWorks.setHasFixedSize(true);
        Horizontal = new LinearLayoutManager(this);
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);

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

        cat.child(Works_Category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String work_cat = (String) dataSnapshot.child("journ_name").getValue();
                cat_name.setText(work_cat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        showPostdata();
    }

    public void showPostdata(){
        postoptions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(query, WritingModel.class).build();

        postfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WritingModel, WorksFragment.WorksHolder>(postoptions) {
            @Override
            protected void onBindViewHolder(@NonNull WorksFragment.WorksHolder viewHolder, int i, @NonNull WritingModel model) {
                final String Works_Id = getRef(i).getKey();
                viewHolder.setJourn_title(model.getJourn_title());
                viewHolder.setJourn_category(model.getJourn_category());
                viewHolder.setJourn_photo(getApplicationContext(), model.getJourn_photo());
                viewHolder.setJourn_content(model.getJourn_content());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), SingleWorks.class);
                        i.putExtra("works_key", Works_Id);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public WorksFragment.WorksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.works_card, parent,false);
                WorksFragment.WorksHolder rewardsViewHolder = new WorksFragment.WorksHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerviewWorks.setLayoutManager(Horizontal);
        postfirebaseRecyclerAdapter.startListening();
        recyclerviewWorks.setAdapter(postfirebaseRecyclerAdapter);

    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CategoryViewHolder(@NonNull View itemView) {
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
