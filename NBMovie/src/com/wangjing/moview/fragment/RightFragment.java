package com.wangjing.moview.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.wangjing.moview.R;
import com.wangjing.moview.domain.MovieDetail;
import com.wangjing.moview.util.HttpUtil;
import com.wangjing.moview.util.ParseMovieDetailJson;
import com.wangjing.moview.util.URLTool;

import java.util.List;

/**
 * Created by wangjing on 2015/9/4at12:41.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class RightFragment extends Fragment implements View.OnClickListener {

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


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case SUCCESS:
                    MovieDetail movieDetail = (MovieDetail) msg.obj;
                    List<String> genres = movieDetail.getGenres();
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),
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
                    break;
                case FAILED:
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        image = (ImageView) view.findViewById(R.id.image);
        year = (TextView) view.findViewById(R.id.year);
        time = (TextView) view.findViewById(R.id.time);
        rate = (TextView) view.findViewById(R.id.rate);
        content = (TextView) view.findViewById(R.id.content);
        titles = (TextView) view.findViewById(R.id.titles);
        genreslist = (ListView) view.findViewById(R.id.genreslist);
        collection = (TextView) view.findViewById(R.id.collection);
        collection.setOnClickListener(this);
        line = view.findViewById(R.id.line);
        return view;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "has favorite", Toast.LENGTH_SHORT).show();
    }

    /**
     * 刷新Fragment
     *
     * @param id
     * @param path
     */
    public void refreshFragment(String id, String path) {
        Picasso.with(getActivity()).load(URLTool.IMAGEURL + path).into(image);
        final String detailPath = URLTool.DETAIL_MOVIE_INFOI + id + "?api_key=" + URLTool.APTKEY + URLTool.language;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String json = HttpUtil.getMovieJson(detailPath);
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


}
