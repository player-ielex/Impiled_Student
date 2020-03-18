package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddWorks extends AppCompatActivity {
    Spinner category_works;
    DatabaseReference catref, postref;
    ImageView works_images;
    ImageView back;
    private static final int GALLERY_REQUEST = 1;
    EditText title, contents;
    Uri imageUri;
    WoksCategoryModel works;
    Button post;
    FirebaseAuth auth;
    private StorageReference mstorage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_works);
        category_works = (Spinner) findViewById(R.id.ca);
        catref = FirebaseDatabase.getInstance().getReference("journ_cate");
        postref = FirebaseDatabase.getInstance().getReference("journ_post");
        works_images = (ImageView) findViewById(R.id.works_images);
        back = (ImageView) findViewById(R.id.kers);
        title = (EditText) findViewById(R.id.title_works);
        post = (Button) findViewById(R.id.post);
        auth = FirebaseAuth.getInstance();
        contents = (EditText) findViewById(R.id.content_works);
        mstorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        works_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Posting to Works Section");
                progressDialog.show();
                //Code Here when Inserting a Data in the database
                final String journ_title = title.getText().toString().trim();
                final String  journ_content = contents.getText().toString().trim();

                if(!TextUtils.isEmpty(journ_title) && !TextUtils.isEmpty(journ_content) && imageUri != null ){
                    StorageReference filepath = mstorage.child("JournPost").child(imageUri.getLastPathSegment());

                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    DatabaseReference newPost = postref.push();
                                    newPost.child("journ_title").setValue(journ_title);
                                    newPost.child("journ_category").setValue(works.getJourn_id());
                                    newPost.child("journ_content").setValue(journ_content);
                                    newPost.child("journ_photo").setValue(String.valueOf(task.getResult()));
                                    newPost.child("journ_author").setValue(auth.getUid());
                                    newPost.child("journ_created").setValue(ServerValue.TIMESTAMP);
                                    finish();
                                }
                            });
                        }
                    });

                }
            }
        });


        catref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<WoksCategoryModel> journalcat = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() ){
                    String journ_id = dataSnapshot1.child("journ_id").getValue(String.class);
                    String journ_cat_name = dataSnapshot1.child("journ_name").getValue(String.class);
                    journalcat.add(new WoksCategoryModel(journ_id, journ_cat_name));
                }
                ArrayAdapter<WoksCategoryModel> arrayAdapter = new ArrayAdapter<WoksCategoryModel>(AddWorks.this, R.layout.spinner_item, journalcat);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                category_works.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        category_works.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               works = (WoksCategoryModel) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(!(requestCode == Activity.RESULT_OK)){
            try{
                imageUri = data.getData();
                final InputStream imagestream = getContentResolver().openInputStream(imageUri);
                final Bitmap selecetItem = BitmapFactory.decodeStream(imagestream);
                works_images.setImageBitmap(selecetItem);

            }catch (Exception e){

            }
        }else {

        }
    }
}
