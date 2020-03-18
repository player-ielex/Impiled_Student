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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class WorksFragment extends Fragment {
    RecyclerView recyclerViewCat, recyclerViewPost;
    FloatingActionButton addworks;
    static DatabaseReference catjourn, postjourn;
    FirebaseRecyclerAdapter<WoksCategoryModel, CategoryHolder> catfirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<WoksCategoryModel> catoptions;

    FirebaseRecyclerAdapter<WritingModel, WorksHolder> postfirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<WritingModel> postoptions;

    LinearLayoutManager Horizontal,Horizontal2;
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.works_fragment, container, false);
        recyclerViewCat = (RecyclerView) rootView.findViewById(R.id.category);
        recyclerViewPost = (RecyclerView) rootView.findViewById(R.id.post);

        recyclerViewCat.setHasFixedSize(true);
        recyclerViewPost.setHasFixedSize(true);

        catjourn = FirebaseDatabase.getInstance().getReference("journ_cate");
        postjourn = FirebaseDatabase.getInstance().getReference("journ_post");

        postjourn.keepSynced(true);
        catjourn.keepSynced(true);

        Horizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);

        Horizontal2 = new LinearLayoutManager(getActivity());
        Horizontal2.setReverseLayout(true);
        Horizontal2.setStackFromEnd(true);

        addworks = (FloatingActionButton) rootView.findViewById(R.id.add);
        addworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddWorks.class);
                startActivity(i);
            }
        });


        showCatdata();
        showPostdata();
        return rootView;
    }


    public void showCatdata(){
        catoptions = new FirebaseRecyclerOptions.Builder<WoksCategoryModel>().setQuery(catjourn, WoksCategoryModel.class).build();
        catfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WoksCategoryModel, CategoryHolder>(catoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryHolder viewHolder, int i, @NonNull WoksCategoryModel model) {
                final String works_category = getRef(i).getKey();
                viewHolder.setJourn_name(model.getJourn_name());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), SingleWorksCategory.class);
                        i.putExtra("works_category", works_category);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewardcat_card, parent,false);
                CategoryHolder rewardsViewHolder = new CategoryHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerViewCat.setLayoutManager(Horizontal);
        catfirebaseRecyclerAdapter.startListening();
        recyclerViewCat.setAdapter(catfirebaseRecyclerAdapter);
    }

    public void showPostdata(){
        postoptions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(postjourn, WritingModel.class).build();

        postfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WritingModel, WorksHolder>(postoptions) {
            @Override
            protected void onBindViewHolder(@NonNull WorksHolder viewHolder, int i, @NonNull WritingModel model) {
                final String Works_Id = getRef(i).getKey();
                viewHolder.setJourn_title(model.getJourn_title());
                viewHolder.setJourn_category(model.getJourn_category());
                viewHolder.setJourn_photo(getActivity(), model.getJourn_photo());
                viewHolder.setJourn_content(model.getJourn_content());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), SingleWorks.class);
                        i.putExtra("works_key", Works_Id);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public WorksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.works_card, parent,false);
                WorksHolder rewardsViewHolder = new WorksHolder(itemView);
                return rewardsViewHolder;
            }
        };
        recyclerViewPost.setLayoutManager(Horizontal2);
        postfirebaseRecyclerAdapter.startListening();
        recyclerViewPost.setAdapter(postfirebaseRecyclerAdapter);

    }

    public static class CategoryHolder extends RecyclerView.ViewHolder{
        View mView;

        public CategoryHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setJourn_name(String names) {
            TextView cat = (TextView) mView.findViewById(R.id.catname);
            cat.setText(names);
        }

    }

    public static class WorksHolder extends RecyclerView.ViewHolder{
        View mView;

        public WorksHolder(@NonNull View itemView) {
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
            catjourn.child(cat).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String category_name = (String) dataSnapshot.child("journ_name").getValue();
                    TextView Categ = (TextView) mView.findViewById(R.id.categ);
                    Categ.setText(category_name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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
