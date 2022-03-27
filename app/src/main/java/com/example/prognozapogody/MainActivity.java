package com.example.prognozapogody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    EditText inputCity;
    Button button;
    public static ConstraintLayout constraintLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.internetConnectionLayout);

        if (!isNetworkAvailable())
            constraintLayout.setVisibility(View.VISIBLE);

        else
            constraintLayout.setVisibility(View.INVISIBLE);

        inputCity = findViewById(R.id.inputCity);
        button = findViewById(R.id.button);
        loadData();

    }

    public void goToWeather(View view){

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + WeatherActivity.cityName + ",pl&APPID=749561a315b14523a8f5f1ef95e45864&units=metric";


            if (isNetworkAvailable()) {
                constraintLayout.setVisibility(View.INVISIBLE);

                String yourText = inputCity.getText().toString();

                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("KEY_YOUR_TEXT", yourText);
                startActivity(intent);
            } else
                MainActivity.constraintLayout.setVisibility(View.VISIBLE);

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String date = sharedPreferences.getString("DATE_KEY", "Nazwa miasta");
        inputCity.setText(date);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}