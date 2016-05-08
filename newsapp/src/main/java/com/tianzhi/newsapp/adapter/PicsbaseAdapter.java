package com.tianzhi.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianzhi.newsapp.R;
import com.tianzhi.newsapp.beans.GroupPicListItem;

import java.util.ArrayList;

/**
 * 组图界面子组图gridview适配器适配器
 * @author xxf
 *
 */
public class PicsbaseAdapter extends BaseAdapter {
	private ArrayList<GroupPicListItem> lists;
	private LayoutInflater inflater;
	private  Context mContext;
	public PicsbaseAdapter(Context context, ArrayList<GroupPicListItem> lists){
		super();
		this.lists = lists;
		inflater = LayoutInflater.from(context);
		mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_pics, null);
			holder.title = (TextView) convertView.findViewById(R.id.tv_item_pics_title);
			holder.favorite = (TextView) convertView.findViewById(R.id.tv_item_pics_favorite);
			holder.remark = (TextView) convertView.findViewById(R.id.tv_item_pics_remark);
			holder.pics = (TextView) convertView.findViewById(R.id.tv_item_pics_pics);
			holder.image = (ImageView) convertView.findViewById(R.id.tv_item_pics_image);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(lists.get(position).getTitle());
		holder.remark.setText("评论:"+lists.get(position).getCommentNum());
		holder.favorite.setText("收藏:"+lists.get(position).getFavoriteNum());
		holder.pics.setText("组图:"+lists.get(position).getImageNum());
		
		//this.loadImage(lists.get(position).getListPic(), holder.image, this.getDefaultBitmap());
		Picasso.with(mContext).load(lists.get(position).getListPic()).error(R.drawable.common_default_picbox).centerInside().resize(100,100).into(holder.image);
		return convertView;
	}

	private class ViewHolder{
		private TextView title;
		private TextView remark;
		private TextView favorite;
		private TextView pics;
		private ImageView image;
	}
	
}
