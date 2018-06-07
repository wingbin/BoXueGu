package com.boxuegu;

import com.boxuegu.test.GetExample;
import com.boxuegu.utils.OkhttpTool;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testOkhttp(){
        GetExample example = new GetExample();
        try {
            String ret = example.run(GetExample.MOVIE_URL);
            //System.out.print(ret);

            //GSON解析JSON字符串
            Gson gson = new Gson();
            //将网络返回的JSON字符串封装到一个JSONObject
            JsonObject jo = new JsonParser().parse(ret).getAsJsonObject();
            //评分
            System.out.println(jo.get("rating").getAsJsonObject().get("average").getAsDouble());
            //第一个主演的名字
            System.out.println(jo.get("casts").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString());
            //输出电影简介（summary的值）
            System.out.println(jo.get("summary").getAsString());
            //输出电影海报（small）的url
            System.out.println(jo.get("images").getAsJsonObject().get("small").getAsString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLocalServer(){
        GetExample example = new GetExample();
        try {
            String ret = example.run("http://192.168.45.60:8080/Boxuegu/login");

            //GSON解析JSON字符串
            Gson gson = new Gson();
            //将网络返回的JSON字符串封装到一个JSONObject
            JsonArray ja = new JsonParser().parse(ret).getAsJsonArray();

            System.out.println("共有"+ja.size()+"条数据");

            //第一章

            System.out.println("chapterid="+ja.get(0).getAsJsonObject().get("chapterId").getAsInt()+",title="+ja.get(0).getAsJsonObject().get("title").getAsString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void testLogin() throws  Exception{
        OkhttpTool tool = new OkhttpTool();

        String url = "http://192.168.45.60:8080/Boxuegu/login";

        String ret = tool.post(url,"admin","123");

        System.out.print(ret);
    }

}