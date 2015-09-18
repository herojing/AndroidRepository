package com.wangjing.moview.util;

import com.wangjing.moview.domain.MovieList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/9/4at22:02.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class ParseMovieListJson {

    public List<MovieList> getMovieList(String json) {
        List<MovieList> list = null;

        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray("results");
            list = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject tempObj = results.getJSONObject(i);
                String poster_path = tempObj.getString("poster_path");
                int id = tempObj.getInt("id");
                MovieList movie = new MovieList(id, poster_path);
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
