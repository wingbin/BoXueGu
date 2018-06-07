package com.boxuegu.utils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/6.
 */

/**
 * 简单工具类
 */
public class OkhttpTool {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    /**
     * 发送POST请求 ，JSON参数
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try   {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 发送POST请求，登录
     * @param url
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public String login(String url, String username,String password) throws IOException {
        //构建请求体
        //添加参数
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 发送GET请求
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
