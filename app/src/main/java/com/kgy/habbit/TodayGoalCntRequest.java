package com.kgy.habbit;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TodayGoalCntRequest extends StringRequest {

    private static final String URL = "http://15.164.250.22/today_goal_select.php";
    private Map<String, String> map;

    public TodayGoalCntRequest(String userId, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
