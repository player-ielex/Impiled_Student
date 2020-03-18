package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UpdateOwnWorks extends AppCompatActivity {
    private String Works_Id = null;
    Spinner category_works2;
    DatabaseReference catref2, postref2;
    ImageView backz2;
    EditText title2, contents2;
    WoksCategoryModel works2;
    Button update2;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_own_works);
        Works_Id = getIntent().getExtras().getString("update_id");
        category_works2 = (Spinner) findViewById(R.id.ca);
        catref2 = FirebaseDatabase.getInstance().getReference("journ_cate");
        postref2 = FirebaseDatabase.getInstance().getReference("journ_post");
        backz2 = (ImageView) findViewById(R.id.kers);
        title2 = (EditText) findViewById(R.id.title_works);
        update2 = (Button) findViewById(R.id.post);
        contents2 = (EditText) findViewById(R.id.content_works);
        progressDialog = new ProgressDialog(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        backz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        postref2.child(Works_Id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rewards_title= (String) dataSnapshot.child("journ_title").getValue();
                String rewards_cont = (String) dataSnapshot.child("journ_content").getValue();
                contents2.setText(rewards_cont,TextView.BufferType.EDITABLE );
                title2.setText(rewards_title, TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        catref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<WoksCategoryModel> journalcat = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() ){
                    String journ_id = dataSnapshot1.child("journ_id").getValue(String.class);
                    String journ_cat_name = dataSnapshot1.child("journ_name").getValue(String.class);
                    journalcat.add(new WoksCategoryModel(journ_id, journ_cat_name));
                }
                ArrayAdapter<WoksCategoryModel> arrayAdapter = new ArrayAdapter<WoksCategoryModel>(UpdateOwnWorks.this, R.layout.spinner_item, journalcat);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                category_works2.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        category_works2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                works2 = (WoksCategoryModel) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating your Post");
                progressDialog.show();

                DatabaseReference up = postref2.child(Works_Id);
                up.child("journ_title").setValue(title2.getText().toString());
                up.child("journ_content").setValue(contents2.getText().toString());
                up.child("journ_category").setValue(works2.getJourn_id());
                finish();
            }
        });


    }
}
