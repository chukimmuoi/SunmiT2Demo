package com.sunmi.sunmit2demo.view;

/**
 * Author: liuzhicheng
 * Time: 18-8-23  下午5:06
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;

public class VipPayDialog extends Dialog {
    private View.OnClickListener listener;
    private EditText vip;
    private TextView tvCancel;
    private TextView tvConfirm;

    public VipPayDialog(@NonNull Context var1) {
        super(var1, R.style.DialogStyle);
    }

    public VipPayDialog(@NonNull Context var1, int var2) {
        super(var1, var2);
    }

    private void initAction() {

    }

    private void initView() {
        vip = findViewById(R.id.vip_number);
        this.tvCancel = (TextView) this.findViewById(R.id.tv_cancel);
        this.tvConfirm = (TextView) this.findViewById(R.id.tv_confirm);
        if (this.listener != null) {
            this.tvCancel.setOnClickListener(listener);
            this.tvConfirm.setOnClickListener(listener);
        }
        vip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.length() == 11){
                        tvConfirm.setAlpha(1);
                        tvConfirm.setEnabled(true);
                    }else {
                        tvConfirm.setAlpha(0.5f);
                        tvConfirm.setEnabled(false);
                    }
            }
        });
    }

    public String getNum() {
        return this.vip.getText().toString();
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.setContentView(R.layout.dialog_vip_pay);
        this.setCancelable(false);
        this.initView();
        this.initAction();
    }

    public VipPayDialog setOnClickListener(View.OnClickListener var1) {
        this.listener = var1;
        return this;
    }

    public void show() {
        super.show();
    }
}
