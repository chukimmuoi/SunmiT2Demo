package com.sunmi.sunmit2demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sunmi.sunmit2demo.BaseFragment;
import com.sunmi.sunmit2demo.ui.MoreActivity;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

public class GoodsManagerFragment extends BaseFragment{

    Switch scale ,face;

    @Override
    protected int setView() {
        return R.layout.fragment_goods_setting;
    }

    @Override
    protected void init(View view) {
        scale =view.findViewById(R.id.sw_scale);
        face =view.findViewById(R.id.sw_face);
        int goodsMode= (int) SharePreferenceUtil.getParam(getContext(), MoreActivity.GOODSMODE_KEY,15);

        switch (goodsMode){
            case 0:
                face.setChecked(false);
                scale.setChecked(false);
                break;
            case MoreActivity.Goods_1| MoreActivity.Goods_2:
                face.setChecked(true);
                scale.setChecked(false);
                break;
            case MoreActivity.Goods_3|MoreActivity.Goods_4:
                face.setChecked(false);
                scale.setChecked(true);
                break;
            case MoreActivity.Goods_1|MoreActivity.Goods_2|MoreActivity.Goods_3|MoreActivity.Goods_4:
                break;
        }
        MoreActivity.Goods_Mode =goodsMode;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MoreActivity.Goods_Mode += MoreActivity.Goods_1|MoreActivity.Goods_2;

                }else {
                    MoreActivity.Goods_Mode -= MoreActivity.Goods_1|MoreActivity.Goods_2;
                }
                SharePreferenceUtil.setParam(getContext(),MoreActivity.GOODSMODE_KEY, MoreActivity.Goods_Mode);
            }
        });

        scale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MoreActivity.Goods_Mode += MoreActivity.Goods_3|MoreActivity.Goods_4;
                }else {
                    MoreActivity.Goods_Mode -= MoreActivity.Goods_3|MoreActivity.Goods_4;
                }
                SharePreferenceUtil.setParam(getContext(),MoreActivity.GOODSMODE_KEY, MoreActivity.Goods_Mode);
            }
        });
    }
}
