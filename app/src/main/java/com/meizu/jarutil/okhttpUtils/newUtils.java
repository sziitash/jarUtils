package com.meizu.jarutil.okhttpUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Created by libinhui on 2016/6/13.
 */
public class newUtils {
    static JSONObject postUtil(OkHttpClient client,
                               String url,
                               HashMap hm) throws IOException, JSONException {

        FormBody.Builder builder = new FormBody.Builder();
        Set<String> setList = hm.keySet();
        //解析hashmap,构造post请求体
        for (String keystr : setList) {
            builder.add(keystr, String.valueOf(hm.get(keystr)));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String res = response.body().string();
            JSONObject responesObj = new JSONObject(res);
            return responesObj;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    //线程返回值Callable应用
    private static class runThread implements Callable<JSONObject> {
        private OkHttpClient cl;
        private String url;
        private HashMap hm;

        private runThread(OkHttpClient cl, String url, HashMap hm) {
            this.cl = cl;
            this.url = url;
            this.hm = hm;
        }
        @Override
        public JSONObject call() throws Exception {
            JSONObject res = postUtil(cl,url,hm);
            return res;
        }
    }

    public static JSONObject getResponObj(OkHttpClient cl, String url, HashMap hm) throws ExecutionException, InterruptedException {
        //创建线程
        runThread rtc = new runThread(cl, url, hm);
        //把线程放入到线程池
        ExecutorService exec = Executors.newSingleThreadExecutor();
        //使用Future处理线程和获取结果
        Future<JSONObject> task = exec.submit(rtc);
        JSONObject result = task.get();
        //关闭线程池
        exec.shutdown();
        return result;
    }
}