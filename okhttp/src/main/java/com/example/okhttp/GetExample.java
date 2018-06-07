package com.example.okhttp;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetExample {

    public static final String MOVIE_API_URL = "https://api.douban.com/v2/movie/subject/24773958";

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        String ret = "";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try  {
            Response response = client.newCall(request).execute();
           ret = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run(MOVIE_API_URL);
        System.out.println(response);
    }
}