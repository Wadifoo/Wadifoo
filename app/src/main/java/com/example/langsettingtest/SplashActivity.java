package com.example.langsettingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingStart();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
    private void loadingStart(){
        handler.postDelayed(new Runnable(){
            public void run(){
                firstUserCheck();
            }
        },1000);
    }

    public void firstUserCheck(){
        SharedPreferences sharedPreferences = getSharedPreferences("istFirst", Activity.MODE_PRIVATE );
        boolean first = sharedPreferences.getBoolean("istFirst", false);
        if(first == false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();

            // 앱 최초 실행 시 하고싶은 작업
            Intent intent = new Intent(getApplicationContext(), LanguageSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // 다음화면으로 넘어감
            finish();
        }
        else{
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
}