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
public class ImgHeadTwoView extends LinearLayout {
    Context mContext;


    public ImgHeadTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        mContext = getContext();
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
            rlp.addRule(RelativeLayout.ALIGN_PARENT_START);
            rlp.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView tv = new TextView(mContext);
            tv.setTextSize(12);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setText(info + "");

            rl.addView(tv, rlp);
            addView(rl);
        }

    }
}
