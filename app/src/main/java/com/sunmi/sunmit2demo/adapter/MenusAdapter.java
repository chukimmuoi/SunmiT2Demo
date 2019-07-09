package com.sunmi.sunmit2demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.bean.MenusBean;

import java.util.List;

/**
 * Created by highsixty on 2018/3/9.
 * mail  gaolulin@sunmi.com
 */

public class MenusAdapter extends BaseAdapter {

    private Context mContext;
    private List<MenusBean> mMenus;

    public MenusAdapter(Context context, List<MenusBean> menus) {
        this.mContext = context;
        this.mMenus = menus;
    }

    @Override
    public int getCount() {
        return mMenus == null ? 0 : mMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenus == null ? null : mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menus_presentation_items_layout, null);
            hold.tvId = (TextView) convertView.findViewById(R.id.tvId);
            hold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            hold.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            hold.llyBg = (LinearLayout) convertView.findViewById(R.id.lly_bg);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        Log.d("Sunmi", "getView: ------->" + (hold.tvId == null) + "  " + (mMenus == null));
        hold.tvId.setText(mMenus.get(position).getId());
        hold.tvName.setText(mMenus.get(position).getName());
        hold.tvMoney.setText(mMenus.get(position).getMoney());
        if ((position + 1) % 2 == 0) {
            hold.llyBg.setBackgroundColor(Color.parseColor("#F1F5F6"));
        } else {
            hold.llyBg.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        return convertView;
    }

    class ViewHold {
        private TextView tvId;
        private TextView tvName;
        private TextView tvMoney;
        private LinearLayout llyBg;
    }

    public void update(List<MenusBean> menus) {
        this.mMenus = menus;
        notifyDataSetChanged();
    }
}
