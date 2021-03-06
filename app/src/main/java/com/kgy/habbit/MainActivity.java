package com.kgy.habbit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private long backBtnTime;
    TextView todayCnt;
    TextView goalCnt;
    TextView backCnt;
    TextView logout;
    String lsUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 개수
        todayCnt = findViewById(R.id.todayCnt);
        // 목표 개수
        goalCnt = findViewById(R.id.goalCnt);
        // 플러스 버튼
        Button plusBtn = findViewById(R.id.plusBtn);
        // 되돌리기
        backCnt = findViewById(R.id.backCnt);
        // 로그아웃
        logout = findViewById(R.id.logout);

        // 로그인 한 ID를 가지고 오는 부분
        SessionManage sessionManage = new SessionManage();
        lsUserId = sessionManage.getAttribute(MainActivity.this, "userId");

        // 오늘 카운트 가지고 오는 부분
        getCount();

        // 목표 카운트 가지고 오는 부분
        getGoalCount();

        // 로그아웃 클릭
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 되돌리기 클릭
        backCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Habbit", "response : "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                getCount();
                            } else {
                                Toast.makeText(getApplicationContext(), "0개 이하로는 되돌릴 수 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                BackTodayCntRequest backTodayCntRequest = new BackTodayCntRequest(lsUserId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(backTodayCntRequest);
            }
        });

        // 플러스 버튼 클릭
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                getCount();
                            } else {
                                Toast.makeText(getApplicationContext(), "업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                UpdateTodayCntRequest updateTodayCntRequest = new UpdateTodayCntRequest(lsUserId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(updateTodayCntRequest);
            }
        });
    }

    public void getCount(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String goalCnt = jsonObject.getString("goalCnt");
                    todayCnt.setText(goalCnt+"개");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TodayCntRequest todayCntRequest = new TodayCntRequest(lsUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(todayCntRequest);
    }

    public void getGoalCount(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Cnt = jsonObject.getString("goalCnt");
                    goalCnt.setText(Cnt+"개");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TodayGoalCntRequest todayGoalCntRequest = new TodayGoalCntRequest(lsUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(todayGoalCntRequest);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if ( 2000 >= gapTime && gapTime >= 0 ) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(MainActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(MainActivity.this, AdActivity.class);
        startActivity(intent);
        super.onDestroy();
    }
}
