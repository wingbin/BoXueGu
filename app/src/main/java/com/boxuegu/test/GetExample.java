package com.boxuegu.test;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetExample {

     public  static String MOVIE_URL = "https://api.douban.com/v2/movie/subject/24773958";

    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        String retString = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = client.newCall(request).execute();
           retString = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return  retString;
    }

    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println(response);
    }
}