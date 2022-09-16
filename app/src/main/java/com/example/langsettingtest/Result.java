package com.example.langsettingtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.langsettingtest.DataAdapter;
import com.example.langsettingtest.SnackFood;

import java.util.ArrayList;

public class Result {
    private static final String TAG = "Result";
    Context mContext;
    SharedPreferences pref;
    DataAdapter mDbHelper;
    String tableName;
    private ArrayList<SnackFood> snackFoodsList = new ArrayList<>();
    public ArrayList<String> foodNames = new ArrayList<String>(); // 분식 이름 리스트

    private String [] menu;
    private SnackFood snackFood;
    private String trans;
    private float x;
    private float y;
    private TextView tv;
    private ImageButton imgBtn;

    public Result(Context mContext) {
        this.mContext = mContext;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getTrans() {
        return trans;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() { return y; }

    public void setTv(TextView tv) { this.tv = tv; }

    public TextView getTv() { return tv; }

    public void setImgBtn(ImageButton imgBtn) { this.imgBtn = imgBtn; }

    public ImageButton getImgBtn() { return imgBtn; }

    public boolean searchInDB() {
        pref = mContext.getSharedPreferences("preference", mContext.MODE_PRIVATE);
        tableName = pref.getString("table_name", "");
        mDbHelper = new DataAdapter(mContext, tableName);
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        snackFoodsList = mDbHelper.getTableData();
        menu = new String[snackFoodsList.size()];

        // db 닫기
        mDbHelper.close();

        for (int i = 0; i < snackFoodsList.size(); i++) {
            String food = snackFoodsList.get(i).getFood();
            foodNames.add(food);
        }

        if (foodNames.contains(trans)) {
            return true;
        }
        else
            return false;
    }
}