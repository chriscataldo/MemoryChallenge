package com.cataldo.chris.memorychallenge;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Chris on 5/16/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.cover, R.drawable.cover, R.drawable.cover, R.drawable.cover,
            R.drawable.cover, R.drawable.cover, R.drawable.cover, R.drawable.cover,
            R.drawable.cover, R.drawable.cover, R.drawable.cover, R.drawable.cover,
            R.drawable.cover, R.drawable.cover, R.drawable.cover, R.drawable.cover,
            R.drawable.cover, R.drawable.cover, R.drawable.cover, R.drawable.cover
    };

    // Constructor
    public GridViewAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(
                (int) mContext.getResources().getDimensionPixelSize(R.dimen.grid_image_width),
                (int) mContext.getResources().getDimensionPixelSize(R.dimen.grid_image_height)));
        return imageView;
    }

}
