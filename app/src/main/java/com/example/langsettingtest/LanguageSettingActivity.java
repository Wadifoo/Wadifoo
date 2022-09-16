package com.example.langsettingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class LanguageSettingActivity extends AppCompatActivity {
    String[] items; // = {"Korean", "English", "Japanese", "Chinese", "Vietnamese", "Indonesian", "Thai", "German", "Russian", "Spanish", "Italian", "French"};
    TextView textView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);

        Spinner spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.textView);
        items = getResources().getStringArray(R.array.array_lang);

        ArrayAdapter<String> list = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );

        //미리 정의된 레이아웃 사용
        list.setDropDownViewResource(android.R.layout.simple_spinner_item);

        //스피너객체에 list넣어주기
        spinner.setAdapter(list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //선택되면
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                textView.setText(items[position]);
                SharedPreferences pref = getSharedPreferences("preference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("lang", spinner.getSelectedItem().toString());
                editor.commit();

                SharedPreferences get_pref = getSharedPreferences("preference", Context.MODE_PRIVATE);
                String s = get_pref.getString("lang", "language");

                SetTableName(s);

                //Log.v("test", s);

            }

            //아무것도 선택되지 않을 때
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("선택하기");
            }
        });


        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void SetTableName(String lang) {
        SharedPreferences pref = getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        switch (lang) {
            case "Korean":
                editor.putString("papago", "ko");
                editor.putString("table_name", "snackFood");
                editor.commit();
                break;

            case "English": // 영어
                editor.putString("papago", "en");
                editor.putString("table_name", "snackFood_en");
                editor.commit();
                break;

            case "Japanese": // 일본어
                editor.putString("papago", "ja");
                editor.putString("table_name", "snackFood_ja");
                editor.commit();
                break;

            case "Chinese": // 중국어
                editor.putString("papago", "ch-CN");
                editor.putString("table_name", "snackFood_zh_CN");
                editor.commit();
                break;

            case "Vietnamese": // 베트남어
                editor.putString("papago", "vi");
                editor.putString("table_name", "snackFood_vi");
                editor.commit();
                break;

            case "Indonesian": // 인도네시아
                editor.putString("papago", "id");
                editor.putString("table_name", "snackFood_id");
                editor.commit();
                break;

            case "Thai": // 태국어
                editor.putString("papago", "th");
                editor.putString("table_name", "snackFood_th");
                editor.commit();
                break;

            case "German": // 독일어
                editor.putString("papago", "de");
                editor.putString("table_name", "snackFood_de");
                editor.commit();
                break;

            case "Russian": // 러시아어
                editor.putString("papago", "ru");
                editor.putString("table_name", "snackFood_ru");
                editor.commit();
                break;

            case "Spanish": // 스페인어
                editor.putString("papago", "es");
                editor.putString("table_name", "snackFood_es");
                editor.commit();
                break;

            case "Italian": // 이탈리아어
                editor.putString("papago", "it");
                editor.putString("table_name", "snackFood_it");
                editor.commit();
                break;

            case "French": // 프랑스어
                editor.putString("papago", "fr");
                editor.putString("table_name", "snackFood_fr");
                editor.commit();
                break;
        }
    }
}