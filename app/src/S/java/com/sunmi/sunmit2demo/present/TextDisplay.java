package com.sunmi.sunmit2demo.present;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunmi.sunmit2demo.BasePresentation;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.dialog.PayDialog;
import com.sunmi.sunmit2demo.ui.MainActivity;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;
import com.sunmi.sunmit2demo.utils.ScreenManager;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

/**
 * Created by highsixty on 2018/3/23.
 * mail  gaolulin@sunmi.com
 */

public class TextDisplay extends BasePresentation {

    private LinearLayout root;
    private TextView tvTitle;
    private TextView tv;
    private LinearLayout llPresentChoosePayMode;
    private LinearLayout llPresentInfo;
    private TextView tvPaySuccess;
    private TextView paymodeOne;
    private TextView paymodeTwo;
    private TextView paymodeThree;
    private ImageView ivTitle;
    private ProgressBar presentProgress;


    private LinearLayout llPresentPayFail;
    private TextView presentFailOne;
    private TextView presentFailTwo;
    private TextView presentFailThree;
    public int state;

    public TextDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ScreenManager.getInstance().isMinScreen()) {
            setContentView(R.layout.vice_text_min_layout);

        }else {
            setContentView(R.layout.vice_text_layout);
        }

        root = (LinearLayout) findViewById(R.id.root);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tv = (TextView) findViewById(R.id.tv);
        llPresentChoosePayMode = (LinearLayout) findViewById(R.id.ll_present_choose_pay_mode);
        tvPaySuccess = (TextView) findViewById(R.id.tv_pay_success);
        paymodeOne = (TextView) findViewById(R.id.paymode_one);
        paymodeTwo = (TextView) findViewById(R.id.paymode_two);
        paymodeThree = (TextView) findViewById(R.id.paymode_three);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        llPresentInfo = (LinearLayout) findViewById(R.id.ll_present_info);


        llPresentPayFail = (LinearLayout) findViewById(R.id.ll_present_pay_fail);
        presentFailOne = (TextView) findViewById(R.id.present_fail_one);
        presentFailTwo = (TextView) findViewById(R.id.present_fail_two);
        presentFailThree = (TextView) findViewById(R.id.present_fail_three);


        paymodeOne.setOnClickListener(this);
        paymodeTwo.setOnClickListener(this);
        paymodeThree.setOnClickListener(this);

        presentFailOne.setOnClickListener(this);
        presentFailTwo.setOnClickListener(this);
        presentFailThree.setOnClickListener(this);


        presentProgress = (ProgressBar) findViewById(R.id.present_progress);


        root.setClipToOutline(true);
        root.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 15);
            }
        });

    }


    public void update(String tip) {
        update(tip, 0);
    }


    public void setPaying() {
        tvTitle.setText(R.string.pay_paying);
        presentProgress.setVisibility(View.VISIBLE);
        tvPaySuccess.setVisibility(View.VISIBLE);
        llPresentChoosePayMode.setVisibility(View.GONE);
        llPresentPayFail.setVisibility(View.GONE);

        root.setBackgroundResource(R.drawable.present_bg_text1);
        ivTitle.setImageResource(R.drawable.present_pay_iv1);

        tvPaySuccess.setText(R.string.pay_paying_wait);
    }

    public void setPayFail() {
        tvTitle.setText(R.string.pay_fail);
        llPresentInfo.setVisibility(View.VISIBLE);
        presentProgress.setVisibility(View.GONE);
        tvPaySuccess.setVisibility(View.GONE);
        llPresentChoosePayMode.setVisibility(View.GONE);
        llPresentPayFail.setVisibility(View.VISIBLE);

        setSelect(0);

        root.setBackgroundResource(R.drawable.present_bg_text4);
        ivTitle.setImageResource(R.drawable.present_pay_iv4);
    }


    public void update(String tip, final int state) {
        this.state = state;
        String unit = ResourcesUtils.getString(R.string.units_money_units);
        String[] strings = tip.split(unit);
        if(unit.equals("$")){
            strings = tip.split("\\$");
        }
        llPresentPayFail.setVisibility(View.GONE);
        presentProgress.setVisibility(View.GONE);
        switch (state) {
            case 0:
                llPresentInfo.setVisibility(View.VISIBLE);
                tvPaySuccess.setVisibility(View.GONE);
                llPresentChoosePayMode.setVisibility(View.VISIBLE);
                root.setBackgroundResource(R.drawable.present_bg_text1);
                ivTitle.setImageResource(R.drawable.present_pay_iv1);

                setSelect(0);
                tvTitle.setText(strings[0].replace(":", ""));
                tv.setText(zoomString(unit + strings[1]));
                tv.setTextSize(ScreenManager.getInstance().isMinScreen()?136:68);
                break;
            case 1:
                tvPaySuccess.setVisibility(View.VISIBLE);
                llPresentChoosePayMode.setVisibility(View.GONE);
                root.setBackgroundResource(R.drawable.present_bg_text2);
                ivTitle.setImageResource(R.drawable.present_pay_iv2);


                tvTitle.setText(strings[0].replace(":", ""));
                tvPaySuccess.setText(R.string.pay_thank_you);

                tv.setText(zoomString(unit + strings[1]));
                tv.setTextSize(ScreenManager.getInstance().isMinScreen()?136:68);
                playAnim();

                break;
            case 2:

                llPresentInfo.setVisibility(View.GONE);
                root.setBackgroundResource(R.drawable.present_bg_text3);
                ivTitle.setImageResource(R.drawable.present_pay_iv3);
                tvTitle.setText(ResourcesUtils.getString(R.string.tips_bye_again));

                tv.setText(tip);
                tv.setTextSize(ScreenManager.getInstance().isMinScreen()?90:45);
                break;
        }

    }


    private SpannableString zoomString(String strings){
        SpannableString ss = new SpannableString(strings);
        ss.setSpan(new RelativeSizeSpan(0.65f), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // set size
        return  ss;
    }

    void playAnim(){
        AnimationDrawable animationDrawable = (AnimationDrawable) ivTitle.getDrawable();
        animationDrawable.start();
    }


    public void setSelect(int index) {
        paymodeOne.setSelected(index == 0 ? true : false);
        paymodeTwo.setSelected(index == 1 ? true : false);
        paymodeThree.setSelected(index == 2 ? true : false);

    }

    @Override
    public void show() {
        super.show();
        int payMode = (int) SharePreferenceUtil.getParam(getContext(), PayDialog.PAY_MODE_KEY, 7);
        switch (payMode) {
            case PayDialog.PAY_FACE:
                paymodeOne.setVisibility(View.GONE);
                paymodeTwo.setVisibility(View.GONE);
                paymodeThree.setVisibility(View.VISIBLE);
                presentFailTwo.setVisibility(View.GONE);
                break;
            case PayDialog.PAY_CODE |PayDialog.PAY_FACE:
                paymodeOne.setVisibility(View.GONE);
                paymodeTwo.setVisibility(View.VISIBLE);
                paymodeThree.setVisibility(View.VISIBLE);
                presentFailTwo.setVisibility(View.VISIBLE);

                break;

            case PayDialog.PAY_FACE | PayDialog.PAY_CODE | PayDialog.PAY_CASH:
                paymodeOne.setVisibility(View.VISIBLE);
                paymodeTwo.setVisibility(View.VISIBLE);
                paymodeThree.setVisibility(View.VISIBLE);
                presentFailTwo.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.paymode_one:
                setSelect(0);
                break;
            case R.id.paymode_two:
                setSelect(1);
                break;
            case R.id.paymode_three:
                setSelect(2);
                break;
            case R.id.present_fail_one:

                break;
            case R.id.present_fail_two:
                break;
            case R.id.present_fail_three:
                break;


        }

    }

    @Override
    public void onSelect(boolean isShow) {

    }
}
