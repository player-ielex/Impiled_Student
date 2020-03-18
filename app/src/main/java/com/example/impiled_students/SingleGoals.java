package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleGoals extends AppCompatActivity {
    private String Goals_Id = null;
    TextView name, des, stat, loc;
    DatabaseReference rData, tData;
    ImageView back;
    Button yes, no, remove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_goals);
        Goals_Id = getIntent().getExtras().getString("goals_key");
        rData = FirebaseDatabase.getInstance().getReference("reward");
        tData = FirebaseDatabase.getInstance().getReference("transaction");
        name = findViewById(R.id.name_re);
        des = findViewById(R.id.task_re);
        yes = findViewById(R.id.yess);
        no = findViewById(R.id.notyet);
        stat = findViewById(R.id.pends);
        back = findViewById(R.id.backers2);
        loc = findViewById(R.id.loc);
        remove = findViewById(R.id.remove);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tData.child(Goals_Id).removeValue();
                finish();
            }
        });


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

        tData.child(Goals_Id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                String rewards_name = (String) dataSnapshot.child("rewards_id").getValue();
                String progress = (String) dataSnapshot.child("progress").getValue();
                String status = (String) dataSnapshot.child("status").getValue();

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference upd = tData.child(dataSnapshot.getKey());
                        upd.child("progress").setValue("50");
                        finish();
                    }
                });

                if(progress.equals("50")){
                    stat.setText("Waiting for Admin Response");
                    yes.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);
                }

                if(progress.equals("100") && status.equals("Accepted")){
                    stat.setText("Congratuations");
                    loc.setText("You can now able to get the reward at your Proware");
                    loc.setVisibility(View.VISIBLE);
                    yes.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);
                }

                if(progress.equals("100") && status.equals("Rejected")){
                    stat.setText("I'm Very Sorry");
                    loc.setText("The Task you did is invalid");
                    loc.setVisibility(View.VISIBLE);
                    yes.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);
                }

                rData.child(rewards_name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        String rewards_name = (String) dataSnapshot1.child("rewards_name").getValue();
                        String rewards_task = (String) dataSnapshot1.child("rewards_task").getValue();

                        name.setText(rewards_name);
                        des.setText(rewards_task);

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



    }
}
