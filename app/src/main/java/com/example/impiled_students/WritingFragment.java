package com.example.impiled_students;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class WritingFragment extends Fragment {
    RecyclerView essayView, featureView, poemView, shortView, jokesView;
    DatabaseReference writings, features, poems, essays, shorts, jokes;
    FirebaseRecyclerAdapter<WritingModel, WriteHolder> featureRecyclerAdapter, poemRecyclerAdapter, essayRecyclerAdapter, shortRecyclerAdapter, jokeRecyclerAdapter;
    FirebaseRecyclerOptions<WritingModel> feoptions, poemtions, essaytions, shortions, joketions;
    LinearLayoutManager Horizontal, Horizontal2, Horizontal3, Horizontal4, Horizontal5;
    Query feature, poem, essay, story, joke;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.writing_fragment,container,false);
        writings = FirebaseDatabase.getInstance().getReference("journalism").child("journ_post");


        //Initializing RecyclerView
        essayView = (RecyclerView) rootView.findViewById(R.id.EssayView);
        featureView = (RecyclerView) rootView.findViewById(R.id.FeatureView);
        poemView = (RecyclerView) rootView.findViewById(R.id.PoemView);
        shortView = (RecyclerView) rootView.findViewById(R.id.ShortView);
        jokesView = (RecyclerView) rootView.findViewById(R.id.JokeView);

        essayView.setHasFixedSize(true);
        featureView.setHasFixedSize(true);
        poemView.setHasFixedSize(true);
        shortView.setHasFixedSize(true);
        jokesView.setHasFixedSize(true);

        //Initializing Database Reference
        features = writings.child("Feature");
        poems = writings.child("Poem");
        essays = writings.child("Essay");
        shorts = writings.child("ShortStory");
        jokes = writings.child("Joke");

        //Initializing Queries
        feature = features.orderByChild("journ_created").limitToLast(10);
        poem = poems.orderByChild("journ_created").limitToLast(10);
        essay = essays.orderByChild("journ_created").limitToLast(10);
        story = shorts.orderByChild("journ_created").limitToLast(10);
        joke = jokes.orderByChild("journ_created").limitToLast(10);

        //LinearLayoutManager
        Horizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal.setReverseLayout(true);
        Horizontal.setStackFromEnd(true);
        Horizontal2 =  new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal2.setReverseLayout(true);
        Horizontal2.setStackFromEnd(true);
        Horizontal3 =  new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal3.setReverseLayout(true);
        Horizontal3.setStackFromEnd(true);
        Horizontal4 =  new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal4.setReverseLayout(true);
        Horizontal4.setStackFromEnd(true);
        Horizontal5 =  new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Horizontal5.setReverseLayout(true);
        Horizontal5.setStackFromEnd(true);


        //Calling Function
        showEssaydata();
        showFeaturesdata();
        showPoemdata();
        showStorydata();
        showJokesdata();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(essayRecyclerAdapter != null){
            essayRecyclerAdapter.startListening();
        }

        if(featureRecyclerAdapter != null){
            featureRecyclerAdapter.startListening();
        }

        if(poemRecyclerAdapter != null){
            poemRecyclerAdapter.startListening();
        }

        if(shortRecyclerAdapter != null){
            shortRecyclerAdapter.startListening();
        }

        if(jokeRecyclerAdapter != null){
            jokeRecyclerAdapter.startListening();
        }
    }

    public void showEssaydata(){
        essaytions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(essay, WritingModel.class).build();
        essayRecyclerAdapter =  new FirebaseRecyclerAdapter<WritingModel, WriteHolder>(essaytions) {
            @Override
            protected void onBindViewHolder(@NonNull WriteHolder viewHolder, int i, @NonNull WritingModel model) {
                viewHolder.setDetails(getActivity(), model.journ_title, model.journ_content, model.journ_photo);
                final String works_key = getRef(i).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleWorks.class);
                        a.putExtra("works_key", works_key);
                        startActivity(a);
                    }
                });

            }

            @NonNull
            @Override
            public WriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_card, parent, false);
                WriteHolder writeHolder = new WriteHolder(itemView);
                return writeHolder;
            }
        };
        essayView.setLayoutManager(Horizontal);
        essayRecyclerAdapter.startListening();
        essayView.setAdapter(essayRecyclerAdapter);
    }

    public void showFeaturesdata(){
        feoptions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(feature, WritingModel.class).build();
        featureRecyclerAdapter =  new FirebaseRecyclerAdapter<WritingModel, WriteHolder>(feoptions) {
            @Override
            protected void onBindViewHolder(@NonNull WriteHolder viewHolder, int i, @NonNull WritingModel model) {
                viewHolder.setDetails(getActivity(), model.journ_title, model.journ_content, model.journ_photo);
                final String works_key = getRef(i).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleWorks.class);
                        a.putExtra("works_key", works_key);
                        startActivity(a);
                    }
                });
            }

            @NonNull
            @Override
            public WriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_card, parent, false);
                WriteHolder writeHolder = new WriteHolder(itemView);
                return writeHolder;
            }
        };
        featureView.setLayoutManager(Horizontal2);

        featureRecyclerAdapter.startListening();
        featureView.setAdapter(featureRecyclerAdapter);
    }

    public void showPoemdata(){
        poemtions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(poem, WritingModel.class).build();
        poemRecyclerAdapter =  new FirebaseRecyclerAdapter<WritingModel, WriteHolder>(poemtions) {
            @Override
            protected void onBindViewHolder(@NonNull WriteHolder viewHolder, int i, @NonNull WritingModel model) {
                viewHolder.setDetails(getActivity(), model.journ_title, model.journ_content, model.journ_photo);
                final String works_key = getRef(i).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleWorks.class);
                        a.putExtra("works_key", works_key);
                        startActivity(a);
                    }
                });
            }

            @NonNull
            @Override
            public WriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_card, parent, false);
                WriteHolder writeHolder = new WriteHolder(itemView);
                return writeHolder;
            }
        };
        poemView.setLayoutManager(Horizontal3);
        poemRecyclerAdapter.startListening();
        poemView.setAdapter(poemRecyclerAdapter);
    }

    public void showStorydata(){
        shortions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(story, WritingModel.class).build();
        shortRecyclerAdapter =  new FirebaseRecyclerAdapter<WritingModel, WriteHolder>(shortions) {
            @Override
            protected void onBindViewHolder(@NonNull WriteHolder viewHolder, int i, @NonNull WritingModel model) {
                viewHolder.setDetails(getActivity(), model.journ_title, model.journ_content, model.journ_photo);
                final String works_key = getRef(i).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleWorks.class);
                        a.putExtra("works_key", works_key);
                        startActivity(a);
                    }
                });
            }

            @NonNull
            @Override
            public WriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_card, parent, false);
                WriteHolder writeHolder = new WriteHolder(itemView);
                return writeHolder;
            }
        };
        shortView.setLayoutManager(Horizontal4);
        shortRecyclerAdapter.startListening();
        shortView.setAdapter(shortRecyclerAdapter);
    }

    public void showJokesdata(){
        joketions = new FirebaseRecyclerOptions.Builder<WritingModel>().setQuery(joke, WritingModel.class).build();
        jokeRecyclerAdapter =  new FirebaseRecyclerAdapter<WritingModel, WriteHolder>(joketions) {
            @Override
            protected void onBindViewHolder(@NonNull WriteHolder viewHolder, int i, @NonNull WritingModel model) {
                viewHolder.setDetails(getActivity(), model.journ_title, model.journ_content, model.journ_photo);
                final String works_key = getRef(i).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getActivity(), SingleWorks.class);
                        a.putExtra("works_key", works_key);
                        startActivity(a);
                    }
                });
            }

            @NonNull
            @Override
            public WriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_card, parent, false);
                WriteHolder writeHolder = new WriteHolder(itemView);
                return writeHolder;
            }
        };
        jokesView.setLayoutManager(Horizontal5);
        jokeRecyclerAdapter.startListening();
        jokesView.setAdapter(jokeRecyclerAdapter);
    }

    public static class WriteHolder extends RecyclerView.ViewHolder{
        View mView;

        public WriteHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setDetails(final Context ctx, String journ_title, String journ_content, final String journ_photo){
            TextView title = (TextView) mView.findViewById(R.id.title);
            TextView content = (TextView) mView.findViewById(R.id.content);
            final ImageView what = (ImageView) mView.findViewById(R.id.photo);

            title.setText(journ_title);
            content.setText(journ_content);
            Picasso.with(ctx).load(journ_photo).networkPolicy(NetworkPolicy.OFFLINE).into(what, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(journ_photo).into(what);
                }
            });
        }

    }
}
