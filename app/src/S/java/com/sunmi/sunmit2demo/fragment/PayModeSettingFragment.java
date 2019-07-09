package com.sunmi.sunmit2demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sunmi.sunmit2demo.BaseFragment;
import com.sunmi.sunmit2demo.MyApplication;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.dialog.PayDialog;
import com.sunmi.sunmit2demo.ui.MainActivity;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

public class PayModeSettingFragment extends BaseFragment {

    Switch face;
    private Switch swVipFace;
    private Switch swPaymentFace;

    public final static String VIP_PAY_KEY = "VIP_PAY_KEY";
    public final static String PAYMENT_PAY_KEY = "PAYMENT_PAY_KEY";

    @Override
    protected int setView() {
        return R.layout.fragment_pay_mode_setting;
    }

    @Override
    protected void init(View view) {

        swVipFace = view.findViewById(R.id.sw_vip_face);
        swPaymentFace = view.findViewById(R.id.sw_payment_face);


        face = view.findViewById(R.id.sw_face);

        int payMode = (int) SharePreferenceUtil.getParam(getContext(), PayDialog.PAY_MODE_KEY, 7);
        switch (payMode) {
            case PayDialog.PAY_FACE:
                face.setChecked(true);
                break;
            case PayDialog.PAY_FACE | PayDialog.PAY_CODE | PayDialog.PAY_CASH:
                face.setChecked(false);
                break;
        }
        boolean vip = (boolean) SharePreferenceUtil.getParam(getContext(), PayModeSettingFragment.VIP_PAY_KEY, false);
        boolean payment = (boolean) SharePreferenceUtil.getParam(getContext(), PayModeSettingFragment.PAYMENT_PAY_KEY, false);

        swVipFace.setChecked(vip);
        swPaymentFace.setChecked(payment);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (MainActivity.isVertical || MyApplication.getInstance().isHaveCamera() ) {
                        SharePreferenceUtil.setParam(getContext(), PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE);
                    }
                } else {
                    if (MainActivity.isVertical) {
                        SharePreferenceUtil.setParam(getContext(), PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE | PayDialog.PAY_CODE);
                    } else {
                        SharePreferenceUtil.setParam(getContext(), PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE | PayDialog.PAY_CODE | PayDialog.PAY_CASH);
                    }
                }
            }
        });

        swVipFace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferenceUtil.setParam(getContext(), PayModeSettingFragment.VIP_PAY_KEY, isChecked);
            }
        });
        swPaymentFace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferenceUtil.setParam(getContext(), PayModeSettingFragment.PAYMENT_PAY_KEY, isChecked);

            }
        });

    }
}
