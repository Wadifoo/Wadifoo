package com.example.langsettingtest;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodListFragment extends Fragment {
    protected static final String TAG = "FoodListFragment";
    Context mContext;
    RecyclerView recyclerView;
    public ArrayList<SnackFood> snackFoodsList ;  // 분식 리스트
    public ArrayList<String> foodNames = new ArrayList<String>(); // 분식 이름 리스트
    public ArrayList<SnackFood> keywordList = new ArrayList<SnackFood>(); // 검색 키워드에 따른 메뉴 리스트
    public SnackFood snackfood;  // 분식 메뉴 객체
    DataAdapter mDbHelper;  // 데이터베이스 어댑터
    SearchView searchView;   // 검색 뷰

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        mContext = getActivity();  // 현재 프레그먼트를 포함하는 액티비티 context

        initLoadDB();
        MyAdapter adapter = new MyAdapter(snackFoodsList, mContext);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(foodNames.contains(s)){
                    searchFood(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchKeyword(s);
                return true;
            }
        });


        return view;
    }

    private void initLoadDB() {
        SharedPreferences pref = mContext.getSharedPreferences("preference", Context.MODE_PRIVATE);
        String tableName = pref.getString("table_name", "");
        println(tableName);
        mDbHelper = new DataAdapter(mContext, tableName);
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        snackFoodsList = mDbHelper.getTableData();

        // db 닫기
        mDbHelper.close();

        for(int i = 0; i < snackFoodsList.size(); i++){
            String food = snackFoodsList.get(i).getFood();
            foodNames.add(food);
        }
    }

    private void searchFood(String food){
        snackfood = new SnackFood();

        mDbHelper.createDatabase();
        mDbHelper.open();

        try{
            snackfood = mDbHelper.findByFood(food);
        }catch(Exception e){
            Log.e(TAG, "findByFood Error");
            e.printStackTrace();
        }

        mDbHelper.close();

        if(snackfood != null){

            Intent SnackinfoActivity = new Intent(mContext, SnackinfoActivity.class);
            SnackinfoActivity.putExtra("foodName", snackfood.getFood());
            SnackinfoActivity.putExtra("ingredient", snackfood.getIngredient());
            SnackinfoActivity.putExtra("flavor", snackfood.getFlavor());
            SnackinfoActivity.putExtra("spicy", snackfood.getSpicy());
            SnackinfoActivity.putExtra("image", snackfood.getImage());

            startActivity(SnackinfoActivity);
        }

    }

    private void searchKeyword(String keyword){

        mDbHelper.createDatabase();
        mDbHelper.open();
        keywordList = mDbHelper.searchKeyword(keyword);
        if(keywordList != null){
            recyclerView.setAdapter(new MyAdapter(keywordList, mContext));
        }
        mDbHelper.close();
    }
}
