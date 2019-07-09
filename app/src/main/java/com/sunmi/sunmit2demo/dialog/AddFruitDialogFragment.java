package com.sunmi.sunmit2demo.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.bean.GvBeans;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import java.text.DecimalFormat;

/**
 * Created by highsixty on 2018/3/15.
 * mail  gaolulin@sunmi.com
 */

public class AddFruitDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {

    private Button btnCancel;
    private Button btnAdd;
    private int Flag = 0; //0苹果 、1梨子 、2香蕉、 3火龙果
    private TextView tvDes;
    private TextView tvPrice;
    private TextView tvQuality;
    private TextView tvTotal;
    private ImageView ivLogo;
    private DecimalFormat decimalFormat = new DecimalFormat("0.000");
    private DecimalFormat meonyFormat = new DecimalFormat("0.00");

    private float price;
    private String name;
    private GvBeans gvBeans;
    private String total = "0.00";//总价
    boolean isShow = false;//防多次点击
    static int defaultNet = -10;
    private int now_net = defaultNet;
    public static float[] goodsAmount={
            16.66f,10.01f,12.85f,18.59f

    };
    public AddFruitDialogFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addfruit_dialog_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();
    }

    private void initView(View view) {
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        tvDes = (TextView) view.findViewById(R.id.tv_des);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvQuality = (TextView) view.findViewById(R.id.tv_quality);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);


        tvQuality.setAlpha(0.5f);
    }

    private void initAction() {
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAdd.setEnabled(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        int net=bundle.getInt("net", 0);
        name = gvBeans.getName();
        price = Float.parseFloat(gvBeans.getPrice().substring(1));
        tvDes.setText(ResourcesUtils.getString(getContext(), R.string.tips_fruit_please)+gvBeans.getName()+ResourcesUtils.getString(getContext(), R.string.tips_fruit_put));
        tvPrice.setText(gvBeans.getPrice());
        tvQuality.setText("0.000");
        tvTotal.setText("0.00");
        ivLogo.setImageResource(gvBeans.getLogo());

        if(net!=0) {
            btnAdd.setEnabled(true);
            tvTotal.setAlpha(1);
            tvQuality.setAlpha(1);
            btnAdd.setAlpha(1);
            btnCancel.setAlpha(1);
            tvQuality.setText(decimalFormat.format(net * 1.0f / 1000));
            total = meonyFormat.format(net * price / 1000);
            tvTotal.setText(total);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_add:

                if (listener != null) {
                    listener.onAddResult(total, name);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 防抖动
     *
     * @param net
     * @return
     */
    private boolean unShake(int net) {
        if (Math.abs(net - now_net) < -defaultNet) {
            return false;
        }
        now_net = net;
        return true;
    }


    public void update(int status, int net) {
        if (status == 1 && net > 0) {
            btnAdd.setEnabled(true);
            tvTotal.setAlpha(1);
            tvQuality.setAlpha(1);
            btnAdd.setAlpha(1);
            btnCancel.setAlpha(1);
            if (!unShake(net)) {
                return;
            }
        } else {
            now_net = defaultNet;
            btnAdd.setEnabled(false);
            tvTotal.setAlpha(0.5f);
            tvQuality.setAlpha(0.5f);
            btnCancel.setAlpha(0.6f);
            btnAdd.setAlpha(0.6f);
        }
        Log.d("SUNMI", "update: ----------------->" + decimalFormat.format(net * 1.0f / 1000));
        tvQuality.setText(decimalFormat.format(net * 1.0f / 1000));
        total = meonyFormat.format(net * price / 1000);
        tvTotal.setText(total);
    }
    public void updateView(GvBeans gvBeans){
        this.gvBeans=gvBeans;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShow) {
            return;
        }
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    private AddListener listener = null;

    public void setListener(AddListener listener) {
        this.listener = listener;
    }

    public interface AddListener {
        void onAddResult(String total, String name);
    }
}
