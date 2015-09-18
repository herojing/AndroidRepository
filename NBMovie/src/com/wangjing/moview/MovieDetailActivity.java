package com.wangjing.moview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.wangjing.moview.domain.MovieDetail;
import com.wangjing.moview.util.HttpUtil;
import com.wangjing.moview.util.ParseMovieDetailJson;
import com.wangjing.moview.util.URLTool;

import java.util.List;


/**
 * Created by wangjing on 2015/9/4at22:17.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class MovieDetailActivity extends Activity implements View.OnClickListener {
    public final int SUCCESS = 1;
    public final int FAILED = 2;
    protected ImageView image;
    private TextView year;
    private TextView time;
    private TextView rate;
    private TextView content;
    private TextView titles;
    private TextView collection;
    private View line;
    private ListView genreslist;
    private ProgressBar circle_progressbar;
    private StringBuilder pathBuider;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case SUCCESS:
                    MovieDetail movieDetail = (MovieDetail) msg.obj;
                    List<String> genres = movieDetail.getGenres();
                    //  ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects)
                    ArrayAdapter adapter = new ArrayAdapter(MovieDetailActivity.this,
                            R.layout.item_genes_list,
                            R.id.genre,
                            genres);
                    genreslist.setAdapter(adapter);

                    year.setText(movieDetail.getRelease_date());
                    rate.setText(movieDetail.getVote_average() + "/10");
                    content.setText("     " + movieDetail.getOverview());
                    time.setText(movieDetail.getRuntime() + "min");
                    titles.setText(movieDetail.getTitle());
                    collection.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    circle_progressbar.setVisibility(View.INVISIBLE);
                    break;
                case FAILED:
                    Toast.makeText(MovieDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);
        initUI();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String path = intent.getStringExtra("path");
        Picasso.with(this).load(URLTool.IMAGEURL + path).into(image);

        pathBuider = new StringBuilder();
        pathBuider.append(URLTool.DETAIL_MOVIE_INFOI).append(id).append("?api_key=").append(URLTool.APTKEY).append(URLTool.language);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String json = HttpUtil.getMovieJson(pathBuider.toString());
                ParseMovieDetailJson pmdj = new ParseMovieDetailJson();
                MovieDetail movieDetail = pmdj.parseMovieDetailJson(json);
                Message msg = Message.obtain();
                if (movieDetail != null) {
                    msg.obj = movieDetail;
                    msg.what = SUCCESS;
                } else {
                    msg.what = FAILED;
                }
                mHandler.sendMessage(msg);

                Looper.loop();
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        pathBuider.delete(0, pathBuider.length());
        super.onDestroy();
    }

    private void initUI() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        image = (ImageView) findViewById(R.id.image);
        year = (TextView) findViewById(R.id.year);
        time = (TextView) findViewById(R.id.time);
        rate = (TextView) findViewById(R.id.rate);
        content = (TextView) findViewById(R.id.content);
        titles = (TextView) findViewById(R.id.titles);
        genreslist = (ListView) findViewById(R.id.genreslist);
        collection = (TextView) findViewById(R.id.collection);
        line = findViewById(R.id.line);
        circle_progressbar = (ProgressBar) findViewById(R.id.circle_progressbar);
        collection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}