package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Date;

public class SingleRewards extends AppCompatActivity {
    private String Rewards_Id = null;
    ImageView imageView, imagecat, back;
    TextView name, des;
    RelativeLayout relativeLayout;
    Button showre;
    static DatabaseReference mDatabase, transDatabase;
    FirebaseAuth auth;
    SwipeButton enableButton;
    Dialog popup;
    ImageView close;
    TextView retask;
    Button addgoal;
    ProgressDialog progressDialog;
    Query queryone, querytwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_rewards);
        Rewards_Id = getIntent().getExtras().getString("rewards_id");
        imageView = (ImageView) findViewById(R.id.re_pic);
        imagecat = (ImageView) findViewById(R.id.status);
        showre = (Button) findViewById(R.id.show);
        back = (ImageView) findViewById(R.id.backing);
        name = (TextView) findViewById(R.id.re_name);
        des = (TextView) findViewById(R.id.re_des);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("reward");
        transDatabase = FirebaseDatabase.getInstance().getReference("transaction");
        transDatabase.keepSynced(true);
        auth = FirebaseAuth.getInstance();
        String user = auth.getUid();
        queryone = transDatabase.orderByChild("student_id").equalTo(user);
        querytwo = transDatabase.orderByChild("rewards_id").equalTo(Rewards_Id);

        progressDialog = new ProgressDialog(this);


        showre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRewards();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        popup = new Dialog(this);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        mDatabase.child(Rewards_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String rewards_name = (String) dataSnapshot.child("rewards_name").getValue();
                String rewards_desc = (String) dataSnapshot.child("rewards_description").getValue();
                String rewards_pic = (String) dataSnapshot.child("rewards_photo").getValue();
                String rewards_stat = (String) dataSnapshot.child("rewards_status").getValue();
                //String rewards_id = (String) dataSnapshot.child("rewards_id").getValue();

                name.setText(rewards_name);
                des.setText(rewards_desc);

                Picasso.with(SingleRewards.this).load(rewards_pic).into(imageView);

                if(rewards_stat.equals("Added")){
                    imagecat.setImageResource(R.drawable.added);
                }else if(rewards_stat.equals("Updated")) {
                    imagecat.setImageResource(R.drawable.updated);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Code here in the single rewards

    }

    public void ShowRewards(){
        popup.setContentView(R.layout.popup_rewards);
        close = (ImageView) popup.findViewById(R.id.close);
        retask = (TextView) popup.findViewById(R.id.task_des);
        addgoal = (Button) popup.findViewById(R.id.addgoal);

        querytwo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   queryone.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                           if(dataSnapshot1.exists()){
                               addgoal.setVisibility(View.GONE);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        addgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding to Goals Section");
                progressDialog.show();
                /*
                    Data needed to Insert is Student uid, rewards uid, progress = 0 and status = pending;
                */

                //Toast.makeText(SingleRewards.this, Rewards_Id, Toast.LENGTH_SHORT).show();

                DatabaseReference newGoals = transDatabase.child(Rewards_Id + auth.getUid());
                newGoals.child("progress").setValue("0");
                newGoals.child("rewards_id").setValue(Rewards_Id);
                newGoals.child("student_id").setValue(auth.getUid());
                newGoals.child("status").setValue("pending");
                progressDialog.dismiss();
                popup.dismiss();

                /*
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();*/
            }
        });


        mDatabase.child(Rewards_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rewards_task = (String) dataSnapshot.child("rewards_task").getValue();
                retask.setText(rewards_task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

            }
        });
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();
    }
}
