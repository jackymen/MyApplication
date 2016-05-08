package com.tianzhi.newsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tianzhi.newsapp.R;
import com.tianzhi.newsapp.util.ImageAsynLoader;
import com.tianzhi.newsapp.util.ImageUtil;

/**
 * 带有图片加载器的适配器
 * 
 * @author xxf
 * 
 */
public class ImageLoadAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
	private Picasso loader;
	private Bitmap defaultBitmap;

	public Bitmap getDefaultBitmap() {
		return defaultBitmap;
	}

	public Picasso getLoader() {
		return loader;
	}

	// 当前是否正在滑动
	private boolean isFling;

	public void setFling(boolean isFling) {
		this.isFling = isFling;
	}

	public ImageLoadAdapter(Context context) {
		loader =Picasso.with(context);

//		defaultBitmap = ImageUtil.readBitmap(context,
//				R.drawable.common_default_picbox);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadImage(String url, ImageView imageView, Bitmap defaultBitmap) {

		loader.load(url).error(R.drawable.common_default_picbox).centerInside().resize(100,100).into(imageView);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
			loader.resumeTag(this);
		} else {
			loader.pauseTag(this);
		}
	}
}
