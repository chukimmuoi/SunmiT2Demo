package com.sunmi.sunmit2demo.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 徐荣 on 2016/9/8.
 */
public class ImgStatementTwoView extends LinearLayout {
    Context mContext;


    public ImgStatementTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        mContext = getContext();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 刷新数据
     *
     * @param KVPList
     */
    public void refreshView(JSONArray KVPList, int screenWidth) {
        Log.d("TAG", "refreshView: ---------->" + screenWidth);
        removeAllViews();
        LinearLayout layoutone = new LinearLayout(mContext);
        layoutone.setOrientation(LinearLayout.HORIZONTAL);
        layoutone.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout layouttwo = new LinearLayout(mContext);
        layouttwo.setOrientation(LinearLayout.HORIZONTAL);
        layouttwo.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < KVPList.length(); i++) {
            try {
                JSONObject json = KVPList.getJSONObject(i);
                RelativeLayout rl = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth / 2, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rl.setLayoutParams(rlp);
                rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                TextView tv = new TextView(mContext);
                tv.setGravity(Gravity.CENTER);
                if (i == KVPList.length() - 1) {
                    SpannableString spannableString = new SpannableString(json.getString("name") + json.getString("value"));
                    spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.menu_text_name), 0, json.getString("name").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.menu_text_value), json.getString("name").length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(spannableString, TextView.BufferType.SPANNABLE);
                } else {
                    SpannableString spannableString = new SpannableString(json.getString("name") + json.getString("value"));
                    spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.menu_text_name), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(spannableString, TextView.BufferType.SPANNABLE);
                }
                rl.addView(tv, rlp);
                if (i < 2) {
                    layoutone.addView(rl);
                } else {
                    layouttwo.addView(rl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (KVPList.length() <= 2) {
            addView(layoutone);
        } else {
            addView(layoutone);
            addView(layouttwo);
        }
    }
}
