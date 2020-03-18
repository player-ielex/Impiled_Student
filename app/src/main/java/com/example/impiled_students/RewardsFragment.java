package com.example.impiled_students;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.util.Calendar;
import java.util.Locale;

public class RewardsFragment extends Fragment {
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase, UserDatabase, CatDatabase, transDatabase;
    RecyclerView recyclerView, recylcerCat;
    Query query;
    FirebaseRecyclerAdapter<RewardsModel, RewardsViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<RewardCategoryModel, CategoryRewardsViewHolder> catfirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<RewardsModel> options;
    FirebaseRecyclerOptions<RewardCategoryModel> catoptions;
    LinearLayoutManager Horizontal,Horizontal2;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.rewards_fragment,container,false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview);
        recylcerCat = (RecyclerView) rootview.findViewById(R.id.recyclercat);
        mDatabase = FirebaseDatabase.getInstance().getReference("reward");
        CatDatabase = FirebaseDatabase.getInstance().getReference("recategory");
        mDatabase.keepSynced(true);
        query = mDatabase.orderByKey(); //Edit this code when you put the time itself
        Horizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal2 = new LinearLayoutManager(getActivity());
        Horizontal.setReverseLayout(true);
        Horizontal2.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);
        Horizontal2.setStackFromEnd(true);
        UserDatabase = FirebaseDatabase.getInstance().getReference().child("students");
        UserDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recylcerCat.setHasFixedSize(true);


        showData();
        cateData();
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }

        if(catfirebaseRecyclerAdapter != null){
            catfirebaseRecyclerAdapter.startListening();
        }
    }

    private static String getDate(long time) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("MMM dd, yyyy K:mm a", cal).toString();
        return date;
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
                viewHolder.setRewards_photo(getActivity(), model.getRewards_photo());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), SingleRewards.class);
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
        recyclerView.setLayoutManager(Horizontal2);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void cateData(){
        catoptions = new FirebaseRecyclerOptions.Builder<RewardCategoryModel>().setQuery(CatDatabase, RewardCategoryModel.class).build();

        catfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RewardCategoryModel, CategoryRewardsViewHolder>(catoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryRewardsViewHolder viewHolder, int i, @NonNull RewardCategoryModel model) {
                final String category_key = getRef(i).getKey();
                viewHolder.setRecategory_name(model.getRecategory_name());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleRewardsCategory.class);
                        a.putExtra("category_id", category_key);
                        startActivity(a);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryRewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewardcat_card, parent,false);
                CategoryRewardsViewHolder rewardsViewHolder = new CategoryRewardsViewHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recylcerCat.setLayoutManager(Horizontal);
        catfirebaseRecyclerAdapter.startListening();
        recylcerCat.setAdapter(catfirebaseRecyclerAdapter);
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

    public static class CategoryRewardsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CategoryRewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setRecategory_name(String name){
            TextView ProdName = (TextView) mView.findViewById(R.id.catname);
            ProdName.setText(name);
        }

    }
}
