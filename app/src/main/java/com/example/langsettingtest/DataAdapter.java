package com.example.langsettingtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DataAdapter {
    protected static final String TAG = "DataAdapter";
    String[] projection = {
            "_id",
            "food",
            "ingredient",
            "flavor",
            "spicy",
            "image"
    };

    ArrayList<SnackFood> snackFoodsList;
    String selection = "food = ?";

    // TODO : TABLE 이름을 명시해야함
    protected String TABLE_NAME;

    SharedPreferences pref;
    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    ArrayList<String> foodNames;

    private static DataAdapter instance;

    private DataAdapter(Context context, String tableName)
    {
        this.mContext = context;
        this.TABLE_NAME = tableName;
        mDbHelper = new DataBaseHelper(mContext);

    }

    public static DataAdapter getInstance(Context context, String tableName) {
        if(instance == null) {
            instance = new DataAdapter(context, tableName);
        }
        return instance;
    }

    public DataAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public SnackFood findByFood(String food){
        ArrayList<SnackFood> snackFoodList = new ArrayList<SnackFood>();
        String[] selectionArgs = { food };
        SnackFood snackfood = null;
        snackfood = new SnackFood();
        try{
            String sql = "SELECT * FROM " + TABLE_NAME;

            Cursor mCur = mDb.query(
                    TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (mCur!=null)
            {

                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성


                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    snackfood.setId(mCur.getString(0));
                    snackfood.setFood(mCur.getString(1));
                    snackfood.setIngredient(mCur.getString(2));
                    snackfood.setFlavor(mCur.getString(3));
                    snackfood.setSpicy(mCur.getInt(4));
                    snackfood.setImage(mCur.getBlob(5));
                    //System.out.println(mCur.getBlob(5));

                }

            }
            else{
                return null;
            }
        }
        catch(SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return snackfood;
    }

    public ArrayList getTableData()
    {
        try
        {
            // Table 이름 -> antpool_bitcoin 불러오기
            String sql ="SELECT * FROM " + TABLE_NAME;

            snackFoodsList = null;
            // 모델 넣을 리스트 생성
            snackFoodsList = new ArrayList<SnackFood>();

            // TODO : 모델 선언
            SnackFood snackfood = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    snackfood = new SnackFood();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    snackfood.setId(mCur.getString(0));
                    snackfood.setFood(mCur.getString(1));
                    snackfood.setIngredient(mCur.getString(2));
                    snackfood.setFlavor(mCur.getString(3));
                    snackfood.setSpicy(mCur.getInt(4));
                    snackfood.setImage(mCur.getBlob(5));
                    //System.out.println(mCur.getBlob(5));

                    // 리스트에 넣기
                    snackFoodsList.add(snackfood);
                }

            }
            return snackFoodsList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public ArrayList searchKeyword(String keyword){
        ArrayList<SnackFood> keywordSnackFood = new ArrayList<SnackFood>();

        SnackFood snackfood = null;
        try{
            String sql = "SELECT * from " + TABLE_NAME + " WHERE food LIKE ?";

            Cursor mCur = mDb.rawQuery(sql, new String[]{"%"+keyword+"%"});
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    snackfood = new SnackFood();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    snackfood.setId(mCur.getString(0));
                    snackfood.setFood(mCur.getString(1));
                    snackfood.setIngredient(mCur.getString(2));
                    snackfood.setFlavor(mCur.getString(3));
                    snackfood.setSpicy(mCur.getInt(4));
                    snackfood.setImage(mCur.getBlob(5));
                    //System.out.println(mCur.getBlob(5));

                    // 리스트에 넣기
                    keywordSnackFood.add(snackfood);
                }

            }
            return keywordSnackFood;

        }catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }

    }
}