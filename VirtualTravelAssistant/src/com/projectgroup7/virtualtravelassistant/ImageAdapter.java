package com.projectgroup7.virtualtravelassistant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            int pad = 8;
	        	imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
	            //imageView.setAdjustViewBounds(true);
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(pad, pad, pad, pad);
	       
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]); //set as image resource for the ImageView
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.sightseeing, R.drawable.dining, R.drawable.hotels,
	            R.drawable.transportation, R.drawable.shopping, R.drawable.health
	    };
}