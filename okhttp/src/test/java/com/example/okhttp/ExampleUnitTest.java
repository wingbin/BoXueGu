package com.example.okhttp;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testOk(){
        GetExample example = new GetExample();
        String response = null;
        try {
            response = example.run(GetExample.MOVIE_API_URL);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(response);
    }

    @Test
    public void testPost(){
        PostExample example = new PostExample();
        String json = example.bowlingJson("Jesse", "Jake");
        String response = null;
        try {
            response = example.post("http://www.roundsapp.com/post", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }
}