package com.kgy.habbit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText userId;
    EditText userPw1;
    EditText userPw2;
    EditText userGoal;
    private String dupeChkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userId = findViewById(R.id.userId);
        userPw1 = findViewById(R.id.userPw1);
        userPw2 = findViewById(R.id.userPw2);
        userGoal = findViewById(R.id.userGoal);
        Button idDupeChkBtn = findViewById(R.id.idDupeChkBtn);
        Button registerBtn = findViewById(R.id.registerBtn);

        // 중복확인 버튼 클릭
        idDupeChkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String lsUserId = userId.getText().toString();

                if (lsUserId != null || lsUserId.length() > 0) { // 아이디를 입력한 경우
                    // 아이디 유효성 검사
                    if (!Pattern.matches("^[a-zA-Z0-9]{6,12}$", lsUserId)) {
                        Toast.makeText(getApplicationContext(), "아이디는 6~12자로, 영문 및 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else { // 아이디를 입력하지 않은 경우
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 중복확인 성공
                                Toast.makeText(getApplicationContext(), "사용하실 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                dupeChkFlag = lsUserId;
                            } else { // 중복확인 실패
                                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                IdDupeChkRequest idDupeChkRequest = new IdDupeChkRequest(lsUserId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(idDupeChkRequest);
            }
        });

        // 회원가입 버튼 클릭
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lsUserId = userId.getText().toString();
                String lsUserPw1 = userPw1.getText().toString();
                String lsUserPw2 = userPw2.getText().toString();
                String lsGoalCnt = userGoal.getText().toString();

                if (lsUserId != null || lsUserId.length() > 0) { // 아이디를 입력한 경우
                    // 아이디 유효성 검사
                    if (!Pattern.matches("^[a-zA-Z0-9]{6,12}$", lsUserId)) {
                        Toast.makeText(getApplicationContext(), "아이디는 6~12자로, 영문 및 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else { // 아이디를 입력하지 않은 경우
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lsUserPw1 != null || lsUserPw1.length() > 0) { // 비밀번호를 입력한 경우
                    // 비밀번호 유효성 검사
                    if (!Pattern.matches("^[A-Za-z0-9]{6,12}$", lsUserPw1)) {
                        Toast.makeText(getApplicationContext(), "비밀번호는 6~12자로, 영문 및 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else { // 비밀번호를 입력하지 않은 경우
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lsUserPw2 != null || lsUserPw2.length() > 0) { // 비밀번호 재입력을 한 경우

                } else { // 비밀번호 재입력을 하지 않은 경우
                    Toast.makeText(getApplicationContext(), "비밀번호 재입력을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!lsUserPw1.equals(lsUserPw2)) { // 두개의 비밀번호가 다를 경우
                    Toast.makeText(getApplicationContext(), "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!dupeChkFlag.equals(lsUserId)) {
                    Toast.makeText(getApplicationContext(), "아이디 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lsGoalCnt != null || lsGoalCnt.length() > 0 ){
                    if (!Pattern.matches("^[0-9]$", lsGoalCnt)) {
                        Toast.makeText(getApplicationContext(), "숫자로만 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "목표 개수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원가입 성공
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else { // 회원가입 실패
                                Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(lsUserId, lsUserPw1, lsGoalCnt, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

    }
}
