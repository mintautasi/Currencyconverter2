package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mCurrencies;
    private RequestQueue mQueue;
    private android.widget.ArrayAdapter<String> adapter;
    public List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrencies = findViewById(R.id.currencies);
        Button button = findViewById(R.id.button);
        adapter = new CoinsAdapter(this);

        mCurrencies.setAdapter(adapter);

        mQueue = Volley.newRequestQueue(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();

            }
        });


    }


    private void jsonParse() {
        String url = "https://api.exchangeratesapi.io/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    adapter.clear();
                    dataList = parser(response);
                    adapter.addAll(dataList);
                    adapter.notifyDataSetChanged();

                }, error -> error.printStackTrace());
        mQueue.add(request);
    }




    public List<String> parser(JSONObject response) {
        List<String> currencies = new ArrayList<>();
        JSONObject exchangeRates = response.optJSONObject("rates");
        Iterator<String> keys = exchangeRates.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = exchangeRates.optString(key);
            currencies.add(key + ": " + value);
        }
        return new ArrayList<>(currencies);
    }
}