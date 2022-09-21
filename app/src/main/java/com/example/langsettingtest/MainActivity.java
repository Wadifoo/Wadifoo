package com.example.langsettingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, new FoodListFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.cameraFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new CameraFragment()).commit();
                        break;
                    case R.id.listFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new FoodListFragment()).commit();
                        break;
                    case R.id.settingFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new SettingFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
}