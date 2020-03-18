package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleBulletin extends AppCompatActivity {
    private String Bulletin_Id = null;
    TextView date,when,where,title,what;
    ImageView pic, back;
    DatabaseReference bulletin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bulletin);
        back = (ImageView) findViewById(R.id.back);

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

        Bulletin_Id = getIntent().getExtras().getString("bulletin_id");
        bulletin = FirebaseDatabase.getInstance().getReference("announcement");

        date = findViewById(R.id.date);
        when = findViewById(R.id.when);
        where = findViewById(R.id.where);
        title = findViewById(R.id.name);
        what = findViewById(R.id.what);
        pic = findViewById(R.id.img);

        bulletin.child(Bulletin_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String announ_date = (String) dataSnapshot.child("announ_date").getValue();
                String announ_name = (String) dataSnapshot.child("announ_name").getValue();
                String announ_what = (String) dataSnapshot.child("announ_what").getValue();
                String announ_when = (String) dataSnapshot.child("announ_when").getValue();
                String announ_where = (String) dataSnapshot.child("announ_where").getValue();
                String announ_photo = (String) dataSnapshot.child("announ_photo").getValue();

                //String rewards_id = (String) dataSnapshot.child("rewards_id").getValue();

                date.setText(announ_date);
                when.setText(announ_when);
                where.setText(announ_where);
                title.setText(announ_name);
                what.setText(announ_what);


                Picasso.with(SingleBulletin.this).load(announ_photo).into(pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
