package com.kgy.habbit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView todayCnt;
    String lsUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayCnt = findViewById(R.id.todayCnt);
        Button plusBtn = findViewById(R.id.plusBtn);

        // 로그인 한 ID를 가지고 오는 부분
        SessionManage sessionManage = new SessionManage();
        lsUserId = sessionManage.getAttribute(MainActivity.this, "userId");

        // 오늘 카운트 가지고 오는 부분
        getCount();
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String goalCnt = jsonObject.getString("goalCnt");
//                    todayCnt.setText(goalCnt+"개");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        TodayCntRequest todayCntRequest = new TodayCntRequest(lsUserId, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        queue.add(todayCntRequest);

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

}
