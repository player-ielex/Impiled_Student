package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BubbleNavigationLinearView bubbleNavigation;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        bubbleNavigation = findViewById(R.id.bottom_navigation_view_linear);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new HomeFragment()).commit();
        }

        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                Fragment fragment = null;

                switch (view.getId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.rewards:
                        fragment = new RewardsFragment();
                        break;
                    case R.id.writings:
                        fragment = new WorksFragment();
                        break;
                    case R.id.buy:
                        fragment = new GoalsFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragment,fragment).commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Auth.addAuthStateListener(mAuthListener);
    }
}
