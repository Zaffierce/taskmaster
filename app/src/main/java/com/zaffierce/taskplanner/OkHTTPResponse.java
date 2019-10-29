package com.zaffierce.taskplanner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OkHTTPResponse implements Callback {

    AllActivity allActivityInstance;

    public OkHTTPResponse(AllActivity allActivityInstance) {
        this.allActivityInstance = allActivityInstance;
    }

    private static final String TAG = "zaffierce.HTTPFail";
    private static final String TAG1 = "zaffierce.HTTPOk";
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, e.getMessage());
    }

    public List<Task> tasks;

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        String responseBody = response.body().string();

        Gson gson = new Gson();

        Task[] taskObject = gson.fromJson(responseBody, Task[].class);
        for(int i = 0; i < taskObject.length; i++) {
            System.out.println(taskObject);
        }

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                allActivityInstance.renderResponseData((String)inputMessage.obj);
            }
        };
//
//        Message completeMessage = handler.obtainMessage(0, responseBody);
//        completeMessage.sendToTarget();
//        Log.i(TAG1, String.valueOf(completeMessage));



//        System.out.println(responseBody);
    }
}
