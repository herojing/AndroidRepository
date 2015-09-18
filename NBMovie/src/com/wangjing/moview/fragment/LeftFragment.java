package com.wangjing.moview.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.wangjing.moview.R;
import com.wangjing.moview.adapter.MoviewGridViewAdapter;
import com.wangjing.moview.domain.MovieList;
import com.wangjing.moview.util.HttpUtil;
import com.wangjing.moview.util.ParseMovieListJson;
import com.wangjing.moview.util.URLTool;

import java.util.List;

/**
 * Created by wangjing on 2015/9/5at8:20.
 * <p/>
 * Email:wjontheway@163.com
 * <p/>
 * NBMovie
 */
public class LeftFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private final int SUCCESS = 1;
    private final int FAILED = 2;
    private final int SUCCESS_REFRESH = 3;
    private final int FAILED_REFRESH = 4;
    private GridView gridView;
    private Activity mActivity;
    private ProgressBar circle_progressbar;
    private List<MovieList> movieList = null;
    private int pageIndex = 1;
    private boolean isBottom = false;
    private MoviewGridViewAdapter adapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case SUCCESS:
                    movieList = (List<MovieList>) msg.obj;
                    adapter = new MoviewGridViewAdapter(mActivity, movieList);
                    gridView.setAdapter(adapter);
                    MovieList movie = movieList.get(0);
                    circle_progressbar.setVisibility(View.INVISIBLE);
                    RightFragment rigthFragment = (RightFragment) getFragmentManager().findFragmentById(R.id.rightfragment);
                    rigthFragment.refreshFragment(String.valueOf(movie.getId()), movie.getPoster_path());
                    break;
                case FAILED:
                    Toast.makeText(mActivity, "加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS_REFRESH:
                    adapter.addData((List<MovieList>) msg.obj);
                    adapter.notifyDataSetChanged();
                    circle_progressbar.setVisibility(View.INVISIBLE);
                    break;
                case FAILED_REFRESH:
                    Toast.makeText(mActivity, "刷新失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onAttach(Activity activity) {
        mActivity = getActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String json = HttpUtil.getMovieJson(URLTool.MOVIEW_URL);
                ParseMovieListJson parseMovieJson = new ParseMovieListJson();
                movieList = parseMovieJson.getMovieList(json);
                Message msg = Message.obtain();
                if (movieList != null) {
                    msg.obj = movieList;
                    msg.what = SUCCESS;
                } else {
                    msg.what = FAILED;
                }
                mHandler.sendMessage(msg);
                Looper.loop();
            }
        }).start();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_left, container, false);
        circle_progressbar = (ProgressBar) view.findViewById(R.id.circle_progressbar);
        gridView = (GridView) view.findViewById(R.id.mygridview);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieList movie = movieList.get(position);
        RightFragment rigthFragment = (RightFragment) getFragmentManager().findFragmentById(R.id.rightfragment);
        rigthFragment.refreshFragment(String.valueOf(movie.getId()), movie.getPoster_path());
    }


    /**
     * 分页加载数据
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (isBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            circle_progressbar.setVisibility(View.VISIBLE);
            pageIndex++;
            final String path = URLTool.MOVIEW_URL + "&page=" + pageIndex;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    String json = HttpUtil.getMovieJson(path);
                    ParseMovieListJson parseMovieJson = new ParseMovieListJson();
                    List<MovieList> movieTempList = parseMovieJson.getMovieList(json);
                    Message msg = Message.obtain();
                    if (movieTempList != null && movieTempList.size() > 0) {
                        msg.obj = movieTempList;
                        msg.what = SUCCESS_REFRESH;
                    } else {
                        msg.what = FAILED_REFRESH;
                    }
                    mHandler.sendMessage(msg);
                    Looper.loop();
                }
            }).start();
        }
    }

    /**
     * @param view
     * @param firstVisibleItem 第一个可以见到的条目
     * @param visibleItemCount 一个屏幕的条目
     * @param totalItemCount   总的条目
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isBottom = (firstVisibleItem + visibleItemCount) == totalItemCount;
    }
}
