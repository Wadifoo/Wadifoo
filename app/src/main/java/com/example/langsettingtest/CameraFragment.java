package com.example.langsettingtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CameraFragment extends Fragment {
    private DataAdapter mDbHelper;
    private View view;
    private Context mContext;

    // 레이아웃
    private ImageView img;
    private Button btn_capture, btn_gallery, btn_send;

    // 업로드 표시
    private ProgressDialog progress;
    // 플라스크로부터 주고받는 큐.....?
    private RequestQueue queue;

    private String currentPhotoPath;

    private Bitmap bitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GET_GALLERY_IMAGE = 2;
    private int j = 0;
    private int resetCount = 0;

    static final String TAG = "카메라";
    private String imageString;
    private Bitmap rotatedBitmap;

    //private String flaskURL = "http://192.168.0.10:80/";
    private String flaskURL = "http://223.194.155.123:80/";

    private int jsonSize;
    private ArrayList<Result> results;
    private Result result;
    private SnackFood snackfood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_camera, container, false);
        mContext = getActivity();
        init(); // 레이아웃 설정

        // 사진찍기
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Count : " + resetCount);
                if(resetCount != 0){
                    for(int i = 0; i < results.size(); i++){
                        results.get(i).getTv().setVisibility(View.GONE); // 번역 결과 지우기
                        if(results.get(i).getImgBtn() != null){ // 돋보기가 있다면 지우기
                            results.get(i).getImgBtn().setVisibility(View.GONE);
                        }
                    }
                }
                resetCount+=1;
                camera_open_intent();
            }
        });

        // 갤러리로 이동
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Count : " + resetCount);
                if(resetCount != 0){
                    for(int i = 0; i < results.size(); i++){
                        results.get(i).getTv().setVisibility(View.GONE); // 번역 결과 지우기
                        if(results.get(i).getImgBtn() != null){ // 돋보기가 있다면 지우기
                            results.get(i).getImgBtn().setVisibility(View.GONE);
                        }
                    }
                }
                resetCount+=1;
                gallery_open_intent();
            }
        });

        // 번역하기
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(mContext);
                progress.setMessage("Uploading...");
                progress.show();
                sendImage(); // 서버로 이미지 보내기
            }
        });

        return view;
    }
    //xml에 정의한 view 초기화
    private void init() {
        img = view.findViewById(R.id.img);
        btn_capture = view.findViewById(R.id.btn_capture);
        btn_send = view.findViewById(R.id.btn_send);
        btn_gallery = view.findViewById(R.id.btn_gellary);
        //reset = view.findViewById(R.id.reset);
        queue = Volley.newRequestQueue(mContext);

        requestPermission(); //카메라 권한 체크 없으면 요청
    }

    //카메라, 쓰기, 읽기 권한 체크/요청
    private void requestPermission() {
        //민감한 권한 사용자에게 허용요청
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { // 저장소에 데이터를 쓰는 권한을 부여받지 않았다면~

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    //카메라 호출
    private void camera_open_intent() {
        Log.d("Camera", "카메라실행!");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, "에러발생!!");
            }

            if (photoFile != null) {
                Uri providerURI = FileProvider.getUriForFile(mContext, "com.example.langsettingtest.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    //갤러리 띄우기
    private void gallery_open_intent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GET_GALLERY_IMAGE);
    }

    // 카메라로 사진을 찍거나 갤러리에서 사진을 가져오면 Uri를 bitmap으로 변환
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Uri picturePhotoURI = Uri.fromFile(new File(currentPhotoPath));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setBitmap(picturePhotoURI);
            }
        } else if (requestCode == GET_GALLERY_IMAGE && resultCode == getActivity().RESULT_OK) {
            Uri galleryURI = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setBitmap(galleryURI);
            }
        }
    }

    //카메라 촬영 시 임시로 사진을 저장하고 사진위치에 대한 Uri 정보를 가져오는 메소드
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "사진저장>> "+storageDir.toString());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Uri에서 bitmap으로 변환
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setBitmap(Uri picturePhotoURI) {
        try {
            InputStream in = getActivity().getContentResolver().openInputStream(picturePhotoURI);
            bitmap = BitmapFactory.decodeStream(in);
            rotatedBitmap = rotateImage(picturePhotoURI, bitmap);
            img.setImageBitmap(rotatedBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이미지뷰에 회전되어 나오는 사진 올바르게 출력
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException{
        InputStream in = getActivity().getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
            matrix.postRotate(90);
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
            matrix.postRotate(180);
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
            matrix.postRotate(270);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    //이미지 플라시크로 전송
    private void sendImage() {
        //비트맵 이미지를 byte로 변환 -> base64형태로 변환
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        SharedPreferences pref = mContext.getSharedPreferences("preference", mContext.MODE_PRIVATE);
        String papago = pref.getString("papago", "language");

        byte[] imageBytes = bytes.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //base64형태로 변환된 이미지 데이터를 플라스크 서버로 전송
        StringRequest request = new StringRequest(Request.Method.POST, flaskURL+"getImage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        if(response.equals("true")){ // 받은 결과 값이 true이면
                            getResponse(); // json 받아옴
                            Toast.makeText(mContext, "Please wait 5 seconds...", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(mContext, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(mContext, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                    }
                }){
            // 서버로 보낼 것들
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString);
                params.put("lang", papago);

                return params;
            }
        };

        // 서버와의 연결 시 Timeout Error 해결하기 위해 시간 늘여주기
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                100000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    //플라스크로부터 json파일 받기
    private void getResponse(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, flaskURL+"deliverImage", null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 번역된 결과 개수
                            String size = response.getString("size"); // key 값이 "size"인 Value 가져오기
                            jsonSize = Integer.parseInt(size);

                            // 번역 결과
                            String trans = response.getString("trans"); // key 값이 "trans"인 Value 가져오기
                            String t = trans.substring(1, trans.length()-1); // [~~~]로 넘어온 Value의 [] 없애기
                            String [] trans1 = t.split("\""); // " 단위로 쪼개기(가격을 인식할 때 3,000으로 ,가 들어간 상태로 인식하기 때문에 먼저 "로 쪼개줌줌
                            int transSize = 0;

                            String [] trans2 = new String[trans1.length]; // 쪼갠 결과를 저장할 배열
                            for(int i = 0; i < trans1.length; i++){
                                if(!trans1[i].equals(",") && !trans1[i].equals("")) { // 쪼갠 결과가 ,나 공백이 아닌경우 저장
                                    trans2[transSize] = trans1[i];
                                    transSize++;
                                }
                            }

                            // X 좌표값
                            String getX = response.getString("textX"); // key 값이 "textX"인 Value 가져오기
                            String x1 = getX.substring(1, getX.length()-1); // [~~~]로 넘어온 Value의 [] 없애기
                            String [] x2 = x1.split(","); // ,단위로 결과값 쪼개기

                            // Y 좌표값
                            String getY = response.getString("textY"); // key 값이 "textY"인 Value 가져오기
                            String y1 = getY.substring(1, getY.length()-1); // [~~~]로 넘어온 Value의 [] 없애기
                            String [] y2 = y1.split(","); // ,단위로 결과값 쪼개기

                            // 결과값 저장
                            results = new ArrayList<Result>(jsonSize); // 결과값을 ArrayList로 저장

                            ConstraintLayout layout2 = view.findViewById(R.id.buttonLayout); // 결과값을 출력할 layout
                            for(j = 0; j < jsonSize; j++){
                                result = new Result(mContext); // 결과값을 저장할 객체
                                // 좌표값 저장
                                String x = x2[j].substring(1, x2[j].length()-1); // x값의 " 없애기
                                String y = y2[j].substring(1, y2[j].length()-1); // y값의 " 없애기

                                // 간혹 double 형식으로 전달되어오는데 x값은 float 형이여야 하므로 double로 변환 후 float으로 변환
                                Double dX = Double.parseDouble(x);
                                Double dY = Double.parseDouble(y);
                                float fx = dX.floatValue()+(float)60;
                                float fy = dY.floatValue()-(float)40;

                                // 결과값 객체로 저장
                                result.setTrans(trans2[j]); // 번역 결과
                                result.setX(fx); // x 좌표값
                                result.setY(fy); // y 좌표값
                                results.add(result); // ArrayList에 추가

                                // 변역결과를 출력할 textView
                                TextView tv = new TextView(mContext);
                                tv.setText(trans2[j]);
                                tv.setBackgroundColor(Color.WHITE); // textView의 배경색을 흰색으로
                                tv.setX(dX.floatValue()-(float)100); // x 좌표 설정
                                tv.setY(dY.floatValue()); // y 좌표 설정
                                result.setTv(tv);

                                layout2.addView(tv); // layout에 추가

                                if(results.get(j).searchInDB()==true) { // DB에 있는 메뉴이면 돋보기 출력
                                    String m = results.get(j).getTrans();  // 메뉴 받아오기(버튼 눌렀을 때 메뉴 정보 화면으로 넘어가기 위해 메뉴 값을 넘겨주기 위해서 먼저 값을 받아와야함)
                                    ImageButton btn = new ImageButton(mContext); // 이미지 버튼 생성
                                    btn.setImageResource(R.drawable.baseline_search_24); // 돋보기 이미지 설정
                                    btn.setBackgroundColor(Color.parseColor("#00000000")); // 이미지 버튼 투명하게
                                    btn.setX(result.getX()); // 버튼 좌표의 x값 설정
                                    btn.setY(result.getY()); // 버튼 좌표의 y값 설정

                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Toast.makeText(mContext, m, Toast.LENGTH_SHORT).show();
                                            searchFood(m); // DB에 있는지 찾기
                                        }
                                    });
                                    result.setImgBtn(btn);
                                    layout2.addView(btn); // layout에 버튼 추가
                                }
                                else{
                                    result.setImgBtn(null);
                                }
                                results.add(result);
                            }
                        } catch (Exception e) { // JSONException로 하면 Error -> 그냥 안된다고 하더라.. 왤까
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // 서버와의 연결 시 Timeout Error 해결하기 위해 시간 늘여주기
        req.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                100000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    // DB에 메뉴가 있는지 확인
    private void searchFood(String food){
        snackfood = new SnackFood();
        SharedPreferences pref = mContext.getSharedPreferences("preference", Context.MODE_PRIVATE);
        String tableName = pref.getString("table_name", "");
        mDbHelper = new DataAdapter(mContext, tableName);
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
}