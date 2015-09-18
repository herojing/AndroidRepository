package com.wangjing.moview.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wangjing on 2015/9/4at20:52.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class HttpUtil {

    public static String getMovieJson(String path) {
        HttpGet get = new HttpGet(path);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream stream = response.getEntity().getContent();
                return loadStreamForMovieJsonString(stream);
            }
        } catch (IOException e) {
            return "获取失败";
        }
        return null;
    }

    private static String loadStreamForMovieJsonString(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len = 0;
        byte arr[] = new byte[1024];

        while ((len = bis.read(arr)) != -1) {
            bos.write(arr, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }
}
