package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.Calendar;
import java.util.Locale;

public class SingleRewardsCategory extends AppCompatActivity {
    private String Category_Id = null;
    TextView catname;
    DatabaseReference recategory, rew;
    ImageView backing;
    RecyclerView recyclerView;
    LinearLayoutManager Horizontal;
    FirebaseRecyclerOptions<RewardsModel> options;
    FirebaseRecyclerAdapter<RewardsModel, RewardsViewHolder> firebaseRecyclerAdapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_rewards_category);
        catname = (TextView) findViewById(R.id.rewardcat);
        Category_Id = getIntent().getExtras().getString("category_id");
        recategory = FirebaseDatabase.getInstance().getReference("recategory");
        rew = FirebaseDatabase.getInstance().getReference("reward");
        query = rew.orderByChild("rewards_category").equalTo(Category_Id);
        backing = (ImageView) findViewById(R.id.backing);
        recyclerView = (RecyclerView) findViewById(R.id.catrecycler);
        Horizontal = new LinearLayoutManager(this);
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);

        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        recategory.child(Category_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("recategory_name").getValue();
                catname.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showData();
    }

    private void showData(){

        options = new FirebaseRecyclerOptions.Builder<RewardsModel>().setQuery(query, RewardsModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RewardsModel, RewardsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RewardsViewHolder viewHolder, int i, @NonNull RewardsModel model) {
                final String rewards_key = getRef(i).getKey();
                viewHolder.setRewards_name(model.getRewards_name());
                viewHolder.setRewards_description(model.getRewards_description());
                viewHolder.setRewards_status(model.getRewards_status());
                viewHolder.setRewards_timestamp(model.getRewards_timestamp());
                viewHolder.setRewards_photo(getApplicationContext(), model.getRewards_photo());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), SingleRewards.class);
                        i.putExtra("rewards_id", rewards_key);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public RewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_card, parent,false);
                RewardsViewHolder rewardsViewHolder = new RewardsViewHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerView.setLayoutManager(Horizontal);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static String getDate(long time) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("MMM dd, yyyy K:mm a", cal).toString();
        return date;
    }


    public static class RewardsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public RewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setRewards_name(String name){
            TextView ProdName = (TextView) mView.findViewById(R.id.product_name);
            ProdName.setText(name);
        }

        public void setRewards_description(String desc){
            TextView DesName = (TextView) mView.findViewById(R.id.product_desc);
            DesName.setText(desc);
        }

        public void setRewards_status(String status){
            ImageView stats = (ImageView) mView.findViewById(R.id.status);
            if(status.equals("Added")){
                stats.setImageResource(R.drawable.added);
            }else if(status.equals("Updated")) {
                stats.setImageResource(R.drawable.updated);
            }
        }

        public void setRewards_timestamp(long tim){
            //String timers = String.valueOf(tim);
            TextView TimeStamp = (TextView) mView.findViewById(R.id.stamptime);
            TimeStamp.setText(getDate(tim));
        }

        public void setRewards_photo(final Context ctx, final String image){
            final ImageView rewards_photo = (ImageView) mView.findViewById(R.id.product_photo);
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
