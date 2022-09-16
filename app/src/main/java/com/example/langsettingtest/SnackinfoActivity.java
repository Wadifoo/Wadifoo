package com.example.langsettingtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SnackinfoActivity extends AppCompatActivity {
    protected static final String TAG = "SnackinfoActivity";
    TextView foodName, ingredient, flavor, spicy;
    ImageView imageView;

    String foo, ingr, fla;
    byte[] img;
    int spi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackinfo);

        foodName = findViewById(R.id.foodName);
        ingredient = findViewById(R.id.ingredient);
        flavor = findViewById(R.id.flavor);
        spicy = findViewById(R.id.spicy);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();

        foo = intent.getExtras().getString("foodName");
        ingr = intent.getExtras().getString("ingredient");
        fla = intent.getExtras().getString("flavor");
        spi = intent.getExtras().getInt("spicy");
        img = intent.getExtras().getByteArray("image");



        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
        imageView.setImageBitmap(bitmap);
        foodName.setText(foo);
        ingredient.setText(ingr);
        flavor.setText(fla);
        spicy.setText(Integer.toString(spi));


    }
}