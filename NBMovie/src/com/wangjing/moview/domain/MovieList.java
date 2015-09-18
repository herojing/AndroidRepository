package com.wangjing.moview.domain;

import java.io.Serializable;

/**
 * Created by wangjing on 2015/9/4at20:53.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class MovieList implements Serializable {


    private String poster_path;

    private long id;

    public MovieList(long id, String poster_path) {
        this.id = id;
        this.poster_path = poster_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
