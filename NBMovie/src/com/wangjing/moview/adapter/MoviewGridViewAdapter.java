package com.wangjing.moview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wangjing.moview.R;
import com.wangjing.moview.domain.MovieList;
import com.wangjing.moview.util.DirectUtil;
import com.wangjing.moview.util.URLTool;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MoviewGridViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener;
    private Context context;
    private List<MovieList> list = null;
    private ViewHolder holder;
    private StringBuilder imagePath = new StringBuilder();

    public MoviewGridViewAdapter(Context context, List<MovieList> list) {
        this.context = context;
        this.list = list;

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.mm)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.pic)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.pic)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                            // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(0))    // 设置成圆角图片
                .build();                                    // 创建配置过得DisplayImageOption对象
        animateFirstListener = new AnimateFirstDisplayListener();
    }

    public void addData(List<MovieList> tempList) {
        list.addAll(tempList);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieList movie = list.get(position);
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            if (DirectUtil.isHorizontal == true) {
                view = LayoutInflater.from(context).inflate(R.layout.item_land_gridview, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.item_gridview, parent, false);
            }

        }

        holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.movieImage);
            view.setTag(holder);
        }
        imagePath.append(URLTool.IMAGEURL).append(movie.getPoster_path());
        //Picasso.with(context).load(imagePath.toString()).into(holder.image);
        imageLoader.displayImage(imagePath.toString(), holder.image, options, animateFirstListener);
        imagePath.delete(0, imagePath.length());
        return view;
    }

    /**
     * 条目点击事件处理
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class ViewHolder {
        ImageView image = null;
    }

    /**
     * 图片加载第一次显示监听器
     *
     * @author wangjing
     */
    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
