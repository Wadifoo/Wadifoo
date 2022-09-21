package com.example.langsettingtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Set;

public class SettingFragment extends PreferenceFragmentCompat {
    private final String TAG = "SettingFragment";
    SharedPreferences defaultPref;
    Context mContext;
    String lang;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.root_preference, rootKey);
        //mContext = getActivity();

        lang = displaySettings();
        setTableName(lang);

        Toast.makeText(mContext, lang, Toast.LENGTH_SHORT).show();
    }

    private String displaySettings(){
        defaultPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String language = defaultPref.getString("lang", "null");
        Toast.makeText(mContext, lang, Toast.LENGTH_SHORT).show();
        Log.d(TAG, lang);
        return language;
    }
    public void setTableName(String lang) {
        SharedPreferences pref = mContext.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();

        switch (lang) {
            case "Korean":
                editor.putString("papago", "ko");
                editor.putString("table_name", "snackFood");
                editor.commit();
                break;

            case "English": //
                // 영어
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
                editor.putString("table_name", "snackFood_zh_id");
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
