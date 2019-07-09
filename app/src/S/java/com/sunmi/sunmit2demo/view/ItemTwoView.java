package com.sunmi.sunmit2demo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 徐荣 on 2016/9/8.
 */
public class ItemTwoView extends LinearLayout {
    Context mContext;
    public ItemTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    public void refreshView(List<String> data) {
        removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            String info = data.get(i);

            RelativeLayout rl = new RelativeLayout(mContext);
            rl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));

            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView tv = new TextView(mContext);
            tv.setTextSize(17);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setText(info + "");
            rl.addView(tv, rlp);
            addView(rl);
        }

    }
}
