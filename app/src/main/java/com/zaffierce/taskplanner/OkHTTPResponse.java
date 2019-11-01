//package com.zaffierce.taskplanner;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//import com.google.gson.Gson;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;
//
//public class OkHTTPResponse implements Callback {
//
//    AllTasksActivity allTasksActivityInstance;
//
//    public OkHTTPResponse(AllTasksActivity allTasksActivityInstance) {
//        this.allTasksActivityInstance = allTasksActivityInstance;
//    }
//
//    private static final String TAG = "zaffierce.HTTPFail";
//    private static final String TAG1 = "zaffierce.HTTPOk";
//    @Override
//    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//        Log.e(TAG, e.getMessage());
//    }
//
//    public List<Task> tasks;
//
//    @Override
//    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//        String responseBody = response.body().string();
//
//        Gson gson = new Gson();
//
//        Task[] taskObject = gson.fromJson(responseBody, Task[].class);
//        this.tasks = new LinkedList<>();
//        this.tasks.addAll(Arrays.asList(taskObject));
//        Log.i(TAG1, tasks.toString());
//
//        Handler handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message inputMessage) {
//                allTasksActivityInstance.renderResponseData((String)inputMessage.obj);
//            }
//        };
//    }
//}
