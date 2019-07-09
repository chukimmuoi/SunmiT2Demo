package com.sunmi.sunmit2demo.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 徐荣 on 2016/9/8.
 */
public class ImgStatementTwoView1 extends RelativeLayout {
    Context mContext;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    public ImgStatementTwoView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = getContext();
        initView();
    }

    private void initView() {
        View.inflate(mContext, R.layout.item_sm_textimg, this);
        tv0 = (TextView) findViewById(R.id.tv0);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    public void refreshView(List<ArrayList<String>> data) {
        clearText();
        for (int i = 0; i < data.size(); i++) {
            String info = new String();
            ArrayList<String> mData = data.get(i);
            for (int j = 0; j < mData.size(); j++) {
                if (j == 0) {
                    info = "<font color='#bfc3cd'>" + mData.get(j) + "</font>";
                } else if (j == 1) {
                    info = info + "  <font color='#ffffff''>" + mData.get(j) + "</font>";
                }
            }
            setText(i, info);
        }
    }

    private void setText(int num, String info) {
        switch (num) {
            case 0:
                tv0.setText(Html.fromHtml(info));
                break;
            case 1:
                tv1.setText(Html.fromHtml(info));
                break;
            case 2:
                tv2.setText(Html.fromHtml(info));
                break;
            case 3:
                tv3.setText(Html.fromHtml(info));
                break;
        }
    }

    /**
     * 清空
     */
    private void clearText() {
        tv0.setText("");
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
    }
}
