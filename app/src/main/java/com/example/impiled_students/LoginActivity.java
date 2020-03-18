package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText iemail, ipass;
    Button login;
    FirebaseAuth Auth;
    DatabaseReference UserDatabase;
    ProgressDialog mProgress;
    Animation slide_down, slide_up;
    RelativeLayout container;
    TextView textlog;
    View views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Auth = FirebaseAuth.getInstance();
        iemail = (EditText)findViewById(R.id.email);
        ipass = (EditText)findViewById(R.id.pass);
        login = (Button)findViewById(R.id.login);
        mProgress = new ProgressDialog(this);
        textlog = (TextView) findViewById(R.id.textlog);
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        views = findViewById(R.id.content);
        UserDatabase = FirebaseDatabase.getInstance().getReference().child("students");
        UserDatabase.keepSynced(true);
        container = (RelativeLayout) findViewById(R.id.container);

        container.startAnimation(slide_up);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserLogin();
            }
        });
    }

    private void checkUserLogin() {
        String email = iemail.getText().toString().trim();
        String pass = ipass.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mProgress.setMessage("Checking Login ...");
            mProgress.show();
            Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgress.dismiss();
                        checkUserExist();
                    }else {
                        mProgress.dismiss();
                        Snackbar.make(views, "Wrong Password or Email", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                }
            });

        }else {
            Snackbar.make(views, "Must Type The Email and Password", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }

    private void checkUserExist(){
        final String student_id = Auth.getCurrentUser().getUid();

        UserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(student_id)){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Snackbar.make(views, "Account Not Found", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
