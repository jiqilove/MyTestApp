package com.example.mytestapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

       String aa=" 18965432101 林佳莉 发送失败 \n18965432102 林嘉豪 发送失败 \n18965432105 刘雅诗 发送失败";

        String result = aa.replaceAll("\\d+", "");
        System.out.println(result);

    }
}