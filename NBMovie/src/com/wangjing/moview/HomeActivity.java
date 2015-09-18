package com.wangjing.moview;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import com.wangjing.moview.fragment.MovieNomalFragment;
import com.wangjing.moview.util.DirectUtil;
import com.wangjing.moview.util.URLTool;

import java.util.Locale;


public class HomeActivity extends Activity {
    private long exitTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseLanguage();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.movie);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (width > height) {
            DirectUtil.isHorizontal = true;
            setContentView(R.layout.land_main);
        } else {
            DirectUtil.isHorizontal = false;
            setContentView(R.layout.main);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, new MovieNomalFragment(), "tag");
            transaction.commit();
        }
    }

    private void chooseLanguage() {
        String language = Locale.getDefault().getLanguage();
        if ("en".equals(language)) {
            URLTool.language = "&language=en";
        } else {
            URLTool.language = "&language=zh";
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar, menu);
        return true;
    }


    /**
     * 退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) { //System.currentTimeMillis()无论何时调用，肯定大于2000
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        FragmentManager manager = getFragmentManager();
        MovieNomalFragment fragment = (MovieNomalFragment) manager.findFragmentByTag("tag");
        switch (itemId) {
            case R.id.release_date:
                fragment.RequestMovieData(URLTool.sort_by_release_date);
                break;
            case R.id.vote_average:
                fragment.RequestMovieData(URLTool.sort_by_vote_average);
                break;
            case R.id.vote_count:
                fragment.RequestMovieData(URLTool.sort_by_vote_count);
                break;
            case R.id.original_title:
                fragment.RequestMovieData(URLTool.sort_by_original_title);
                break;
            case R.id.revenue:
                fragment.RequestMovieData(URLTool.sort_by_revenue);
                break;
            case R.id.popularity:
                fragment.RequestMovieData(URLTool.sort_by_popularity);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}
