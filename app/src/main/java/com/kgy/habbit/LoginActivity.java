package com.kgy.habbit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private long backBtnTime;
    EditText userId;
    EditText userPw;
    String loginId, loginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = findViewById(R.id.userId);
        userPw = findViewById(R.id.userPw);

        Button registerBtn = findViewById(R.id.registerBtn); // 회원가입 버튼
        Button loginBtn = findViewById(R.id.loginBtn); // 로그인 버튼

        // 자동 로그인 확인 부분
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId",null);
        if(loginId != null) {
            Intent intent = new Intent(LoginActivity.this, AdActivity.class);
            startActivity(intent);
            finish();
        }

        // 회원가입 버튼 클릭
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



        // 로그인 버튼 클릭
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lsUserId = userId.getText().toString();
                String lsUserPw = userPw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인 성공

                                String id = jsonObject.getString("userId");

                                Toast.makeText(getApplicationContext(), "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", id);
                                autoLogin.commit();

                                SessionManage sessionManage = new SessionManage();
                                sessionManage.setAttribute(LoginActivity.this, "userId", id);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else { // 로그인 실패
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(lsUserId, lsUserPw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if ( 2000 >= gapTime && gapTime >= 0 ) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(LoginActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
