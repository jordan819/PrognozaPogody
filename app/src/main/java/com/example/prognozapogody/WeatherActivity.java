package com.example.prognozapogody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeatherActivity extends AppCompatActivity {

    public static String cityName;
    TextView text1;
    TextView time;
    public static TextView temp;
    public static TextView tempMin;
    public static TextView tempMax;
    public static TextView humidity;
    public static TextView pressure;
    public static ImageView image;
    TextView connectionTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        connectionTextView = findViewById(R.id.connectionTextView);

        if (!isNetworkAvailable()){
            connectionTextView.setVisibility(View.VISIBLE);
        }else {
            connectionTextView.setVisibility(View.INVISIBLE);

            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (isNetworkAvailable()) {
                        connectionTextView.setVisibility(View.INVISIBLE);
                        FetchData process = new FetchData();
                        process.execute();
                        weatherRefresh();
                    } else {
                        connectionTextView.setVisibility(View.VISIBLE);
                    }
                    pullToRefresh.setRefreshing(false);
                }
            });


            time = findViewById(R.id.textView7);

            temp = findViewById(R.id.tempTV);
            tempMin = findViewById(R.id.tempMinTV);
            tempMax = findViewById(R.id.tempMaxTV);
            humidity = findViewById(R.id.humidityTV);
            pressure = findViewById(R.id.pressureTV);

            image = findViewById(R.id.imageView2);

            text1 = findViewById(R.id.nameTV);
            Intent intent = getIntent();
            cityName = intent.getStringExtra("KEY_YOUR_TEXT");
            text1.setText(cityName);
            saveData(cityName);

            weatherUpdate();
            timeUpdate();

        }
    }


    private void weatherUpdate() {
        if (isNetworkAvailable()) {
            FetchData process = new FetchData();
            process.execute();
            weatherRefresh();
        }
    }

    private void timeUpdate(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String actualTime = simpleDateFormat.format(calendar.getTime());
        time.setText(actualTime);

        timeRefresh();
    }

    private void timeRefresh() {

        int milliseconds = 1000;
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeUpdate();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    private void weatherRefresh() {

        int milliseconds = 300000;
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                weatherUpdate();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    private void saveData(String text){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DATE_KEY", text);
        editor.apply();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkConnection() {
        if (!isNetworkAvailable()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}