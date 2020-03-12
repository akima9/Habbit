package com.kgy.habbit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = findViewById(R.id.userId);
        userPw = findViewById(R.id.userPw);

        Button registerBtn = findViewById(R.id.registerBtn); // 회원가입 버튼
        Button loginBtn = findViewById(R.id.loginBtn); // 로그인 버튼

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

//                CheckTypeTask task  = new CheckTypeTask();
//                task.execute();

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

//    private class CheckTypeTask extends AsyncTask<Void, Void, Void> {
//
//        ProgressDialog asyncDialog = new ProgressDialog(LoginActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            asyncDialog.setMessage("잠시만 기다려주세요.");
//            asyncDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            try {
//                for (int i = 0; i < 5; i++) {
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            asyncDialog.dismiss();
//            super.onPostExecute(aVoid);
//        }
//    }

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
