package com.wangjing.moview.util;

import com.wangjing.moview.domain.MovieDetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/9/5at22:26.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class ParseMovieDetailJson {

    public MovieDetail parseMovieDetailJson(String json) {
        List<String> genresList = null;
        MovieDetail movieDetail = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONArray genres = object.getJSONArray("genres");
            if (genres != null) {
                genresList = new ArrayList<>();
                for (int i = 0; i < genres.length(); i++) {
                    JSONObject genre = genres.getJSONObject(i);
                    String name = genre.getString("name");
                    genresList.add(name);
                }
            }

            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String vote_average = object.getString("vote_average");
            String runtime = object.getString("runtime");
            String title = object.getString("title");

            movieDetail = new MovieDetail(genresList, title, overview, runtime, release_date, vote_average);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetail;
    }
}
