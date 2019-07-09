package com.sunmi.sunmit2demo.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.sunmi.sunmit2demo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 徐荣 on 2016/9/8.
 */
public class ImgTextAdapter extends BaseAdapter {
    private Context mContext;
    private List<ArrayList<String>> mData;

    public ImgTextAdapter(Context context, List<ArrayList<String>> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_imgtext, null);
            mViewHolder = new ViewHolder();
            mViewHolder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            mViewHolder.mImgItemTwoView = (ImgItemTwoView) view.findViewById(R.id.imgitemview);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        if (i % 2 != 0) {
            mViewHolder.rl.setBackgroundColor(Color.parseColor("#00000000"));
        } else {
            mViewHolder.rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        ArrayList<String> data = mData.get(i);
        mViewHolder.mImgItemTwoView.refreshView(data);
        return view;
    }

    public void notifyDataSetChanged(List<ArrayList<String>> mData) {
        this.mData = mData;
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        RelativeLayout rl;
        ImgItemTwoView mImgItemTwoView;
    }
}
