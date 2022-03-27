package com.example.prognozapogody;


import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData extends AsyncTask<Void,Void,Void> {

    String data = "";
    String dataTemp = "";
    String dataTempMin = "";
    String dataTempMax = "";
    String dataHumidity = "";
    String dataPressure = "";
    String imageUrl;
    String imageCode;

    @Override
    protected Void doInBackground(Void... voids) {

        String adrress = "http://api.openweathermap.org/data/2.5/weather?q=" + WeatherActivity.cityName + ",pl&APPID=749561a315b14523a8f5f1ef95e45864&units=metric";

        try {
            URL url = new URL(adrress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            httpURLConnection.disconnect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();

            while (line != null){
                data += line;
                line = bufferedReader.readLine();
            }

            JSONObject JO_ALL = new JSONObject(data);

            dataTemp = JO_ALL.getJSONObject("main").getString("temp") + " °C";
            dataTempMin = JO_ALL.getJSONObject("main").getString("temp_min") + " °C";
            dataTempMax = JO_ALL.getJSONObject("main").getString("temp_max") + " °C";
            dataHumidity = JO_ALL.getJSONObject("main").getString("humidity") + " %";
            dataPressure = JO_ALL.getJSONObject("main").getString("pressure") + " hPa";



            imageCode = JO_ALL.getJSONArray("weather").getJSONObject(0).getString("icon");
            imageUrl = "http://openweathermap.org/img/wn/" + imageCode + "@2x.png";

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

            WeatherActivity.temp.setText(this.dataTemp);
            WeatherActivity.tempMin.setText(this.dataTempMin);
            WeatherActivity.tempMax.setText(this.dataTempMax);
            WeatherActivity.humidity.setText(this.dataHumidity);
            WeatherActivity.pressure.setText(this.dataPressure);
            Picasso.get().load(imageUrl).into(WeatherActivity.image);

    }
}
