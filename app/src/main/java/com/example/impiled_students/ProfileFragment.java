package com.example.impiled_students;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    TextView name, email, grade;
    CircleImageView image;
    DatabaseReference User_data, sect, grad;
    CardView own, versionz, termz, logout;
    FirebaseAuth.AuthStateListener authStateListener;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.profile_fragment,container,false);
        auth = FirebaseAuth.getInstance();
        name = (TextView) rootview.findViewById(R.id.name);
        grade = (TextView) rootview.findViewById(R.id.grade);
        own = (CardView) rootview.findViewById(R.id.workz);
        versionz = (CardView) rootview.findViewById(R.id.versionz);
        termz = (CardView) rootview.findViewById(R.id.terms);
        logout = (CardView) rootview.findViewById(R.id.logout);

        setupListener();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        termz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsandAgree.class));
            }
        });

        own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OwnWorks.class));
            }
        });

        versionz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Version.class));
            }
        });

        //email = (TextView) rootview.findViewById(R.id.email);
        image = (CircleImageView) rootview.findViewById(R.id.picture);
        String user_id = auth.getUid();

        User_data = FirebaseDatabase.getInstance().getReference().child("students");
        sect = FirebaseDatabase.getInstance().getReference().child("sections");
        grad = FirebaseDatabase.getInstance().getReference().child("grade_level");
        grad.keepSynced(true);
        sect.keepSynced(true);
        User_data.keepSynced(true);


        User_data.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String first = (String) dataSnapshot.child("student_fname").getValue();
                String last = (String) dataSnapshot.child("student_lname").getValue();
                String section = (String) dataSnapshot.child("student_section").getValue();
                String em = (String) dataSnapshot.child("student_email").getValue();
                final String img = (String) dataSnapshot.child("student_photo").getValue();

                name.setText(first + " " + last);

                //email.setText(em);

                sect.child(section).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String section_name = (String) dataSnapshot.child("section_name").getValue();
                        String grade_id = (String) dataSnapshot.child("grade_id").getValue();

                        grad.child(grade_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String grade_name = (String) dataSnapshot.child("grade_level").getValue();
                                grade.setText(grade_name + " " + section_name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Picasso.with(getActivity()).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity()).load(img).into(image);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootview;
    }

    public void setupListener(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                }else {
                    Toast.makeText(getActivity(), "Logout Succes", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    public void onStop(){
        super.onStop();
        if(authStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }
}
