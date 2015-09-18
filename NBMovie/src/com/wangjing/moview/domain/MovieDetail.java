package com.wangjing.moview.domain;

import java.util.List;

/**
 * Created by wangjing on 2015/9/4at22:27.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class MovieDetail {
    private List<String> genres;
    private String title;
    private String overview;
    private String runtime;
    private String release_date;
    private String vote_average;


    public MovieDetail(List<String> genres, String title, String overview, String runtime, String release_date, String vote_average) {
        this.genres = genres;
        this.title = title;
        this.overview = overview;
        this.runtime = runtime;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
}
