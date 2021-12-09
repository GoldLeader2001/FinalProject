package com.example.jsfinalcryptoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    TextView title1, title2, title3, title4, desc1, desc2, desc3, desc4, link1, link2, link3, link4;
    ImageView img1, img2, img3, img4;
    ArrayList<String> finalIMGUrl;
    ArrayList<String> titles;
    ArrayList<String> descript;
    ArrayList<String> newsLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        BottomNavigationView bottomNavigationView =  (BottomNavigationView) findViewById(R.id.Bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.News);
        title1 = findViewById(R.id.tittle_in_card);
        img1 = findViewById(R.id.image_in_card);
        desc1 = findViewById(R.id.desc_in_card);
        link1 = findViewById(R.id.link_in_card);
        title2 = findViewById(R.id.tittle_in_card2);
        img2 = findViewById(R.id.image_in_card2);
        desc2 = findViewById(R.id.desc_in_card2);
        link2 = findViewById(R.id.link_in_card2);
        title3 = findViewById(R.id.tittle_in_card3);
        img3 = findViewById(R.id.image_in_card3);
        desc3 = findViewById(R.id.desc_in_card3);
        link3 = findViewById(R.id.link_in_card3);
        title4 = findViewById(R.id.tittle_in_card4);
        img4 = findViewById(R.id.card4_image);
        desc4 = findViewById(R.id.card4_descrip);
        link4 = findViewById(R.id.card4_link);

        finalIMGUrl =  new ArrayList<String>();
        titles= new ArrayList<String>();
        descript = new ArrayList<String>();
        newsLink = new ArrayList<String>();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.News:
                        return true;

                    case R.id.Login:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), UserProf.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return true;
            }
        });

        newsExtractor();


    }

    private void newsExtractor() {

        finalIMGUrl.clear();

        RequestQueue queueIMG = Volley.newRequestQueue(this);
        String imgUrl = "https://gnews.io/api/v4/search?q=crypto&lang=en&token=91a8834d24e0c6f2194834c7596a3447";
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, imgUrl,
                response -> {
                    Log.d("Response", response);
                    if(response!=null){
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String results = jsonObject.getString("articles");
                            JSONArray news_section = new JSONArray(results);
                            Log.e("TAG", String.valueOf(news_section.length()));
                            for(int i = 0; i <6 ; i++){
                                int rand = (int) (Math.random() * (news_section.length() - 1)) + 1;
                                JSONObject result_index = news_section.getJSONObject(rand);
                                finalIMGUrl.add(result_index.getString("image"));
                                Log.e("TAG", finalIMGUrl.get(i));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    Log.d("ERROR","error => "+error.toString());
                });
        queueIMG.add(stringRequest);


        RequestQueue queueNews =  Volley.newRequestQueue(this);
        String url = getString(R.string.APIKEY) ;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                this::responseHandler,
                error -> {
                    Log.d("ERROR","error => "+error.toString());
                });

        queueNews.add(getRequest);
    }

    private void responseHandler(String response) {
        Log.d("Response", response);
        if(response!=null){
            try{
                JSONObject jsonObject = new JSONObject(response);
                String results = jsonObject.getString("articles");
                JSONArray JSresults = new JSONArray(results);


                for(int i = 0; i <6 ; i++) {

                    JSONObject index_results = JSresults.getJSONObject(i);
                    titles.add(index_results.getString("title"));
                    descript.add(index_results.getString("description"));
                    newsLink.add(index_results.getString("url"));


                    if(i == 0){
                        title1.setText(String.valueOf(titles.get(i)));
                        desc1.setText(String.valueOf(descript.get(i)));
                        Picasso.get().load(String.valueOf(finalIMGUrl.get(i))).into(img1);
                        link1.setText(String.valueOf(newsLink.get(i)));
                    }
                    if(i == 1){
                        title2.setText(String.valueOf(titles.get(i)));
                        desc2.setText(String.valueOf(descript.get(i)));
                        Picasso.get().load(String.valueOf(finalIMGUrl.get(i))).into(img2);
                        link2.setText(String.valueOf(newsLink.get(i)));
                    }
                    if(i == 2){
                        title3.setText(String.valueOf(titles.get(i)));
                        desc3.setText(String.valueOf(descript.get(i)));
                        Picasso.get().load(String.valueOf(finalIMGUrl.get(i))).into(img3);
                        link3.setText(String.valueOf(newsLink.get(i)));
                    }
                    if(i == 4){
                        title4.setText(String.valueOf(titles.get(i)));
                        desc4.setText(String.valueOf(descript.get(i)));
                        Picasso.get().load(String.valueOf(finalIMGUrl.get(i))).into(img4);
                        link4.setText(String.valueOf(newsLink.get(i)));
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}