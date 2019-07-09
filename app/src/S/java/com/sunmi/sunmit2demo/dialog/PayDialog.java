package com.sunmi.sunmit2demo.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sunmi.sunmit2demo.bean.PaymentResponse;
import com.sunmi.sunmit2demo.fragment.PayModeSettingFragment;
import com.sunmi.sunmit2demo.presenter.PayMentPayPresenter;
import com.sunmi.sunmit2demo.receiver.ResultReceiver;
import com.sunmi.sunmit2demo.ui.MainActivity;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.bean.MenuBean;
import com.sunmi.sunmit2demo.model.AlipaySmileModel;
import com.sunmi.sunmit2demo.presenter.AlipaySmilePresenter;
import com.sunmi.sunmit2demo.utils.InstallApkUtils;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by highsixty on 2018/3/14.
 * mail  gaolulin@sunmi.com
 */

public class PayDialog extends AppCompatDialogFragment implements View.OnClickListener, ResultReceiver.AliResultCallback {
    private static final String TAG = "PayDialog";
    private RadioGroup radioGroup;
    private ImageView ivTop;
    private ImageView ivMid;
    private ImageView ivBottom;
    private ImageView ivLogo;
    private TextView tvDescrib;
    private TextView tvMoney;
    private TextView tvFaccPayTips, tv_pay_fail_money, tv_pay_success, tv_name;
    private Button btnCancel;
    private Button btnOk;
    public Button btnComplete;
    private LinearLayout llyPay;
    private LinearLayout llyPayComplete;
    private LinearLayout llPaying, llNoFace, llCanPay, llPayFail;
    private TextView noFaceTitle;
    private TextView noFaceContent;
    private RadioButton rbOne, rbTwo, rbthree;
    private String mMoney;
    private TextView tvMoneyComplete;
    boolean isShow = false;//防多次点击
    boolean isPay = false;//防多次点击
    private StringBuilder sb = new StringBuilder();
    private AlipaySmilePresenter alipaySmilePresenter;
    private AlipaySmileModel alipaySmileModel;
    MainActivity activity = null;

    private PayMentPayPresenter payMentPayPresenter;

    private Handler myHandler = new Handler(Looper.getMainLooper());

    public final static int PAY_CASH = 1;
    public final static int PAY_CODE = 2;
    public final static int PAY_FACE = 4;
    public final static String PAY_MODE_KEY = "PAY_MODE_KEY";
    private int payMode = 7;
    boolean payment;

    private String phoneNumber;

    public PayDialog() {
        super();
    }

    public interface OnCompleteListener {
        void onCancel();

        void onSuccess(int payMode);

        void onComplete(int payMode);
    }

    private OnCompleteListener completeListener = null;

    public void setCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
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
        View view = inflater.inflate(R.layout.paydialog_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();

    }


    private void initView(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        ivTop = (ImageView) view.findViewById(R.id.iv_top);
        ivMid = view.findViewById(R.id.iv_mid);
        ivBottom = (ImageView) view.findViewById(R.id.iv_bottom);
        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        tvDescrib = (TextView) view.findViewById(R.id.tv_describ);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnComplete = (Button) view.findViewById(R.id.btn_complete);
        llyPay = (LinearLayout) view.findViewById(R.id.lly_pay);
        llyPayComplete = (LinearLayout) view.findViewById(R.id.lly_pay_complete);
        llPaying = view.findViewById(R.id.ll_paying);
        llCanPay = (LinearLayout) view.findViewById(R.id.ll_can_pay);
        llNoFace = (LinearLayout) view.findViewById(R.id.ll_no_face);
        llPayFail = (LinearLayout) view.findViewById(R.id.ll_pay_fail);
        rbOne = (RadioButton) view.findViewById(R.id.rbone);
        rbTwo = (RadioButton) view.findViewById(R.id.rbtwo);
        rbthree = view.findViewById(R.id.rbthree);
        tvMoneyComplete = (TextView) view.findViewById(R.id.tv_money_complete);
        tvFaccPayTips = view.findViewById(R.id.tv_face_pay);
        tv_pay_fail_money = view.findViewById(R.id.tv_pay_fail_money);
        tv_pay_success = view.findViewById(R.id.tv_pay_success);
        tv_name = view.findViewById(R.id.tv_name);

        noFaceTitle = (TextView) view.findViewById(R.id.no_face_title);
        noFaceContent = (TextView) view.findViewById(R.id.no_face_content);

        rbOne.setAlpha(1f);
        rbTwo.setAlpha(0.7f);
        rbthree.setAlpha(0.7f);


    }

    private void initAction() {
        activity = (MainActivity) getActivity();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ivLogo.setVisibility(View.VISIBLE);
                switch (checkedId) {
                    case R.id.rbone:
                        ivTop.setVisibility(View.VISIBLE);
                        ivMid.setVisibility(View.INVISIBLE);
                        ivBottom.setVisibility(View.INVISIBLE);
                        tvFaccPayTips.setVisibility(View.GONE);
                        llCanPay.setVisibility(View.VISIBLE);
                        llNoFace.setVisibility(View.GONE);
                        llPayFail.setVisibility(View.GONE);
                        tvDescrib.setText(ResourcesUtils.getString(getContext(), R.string.tips_pay_money));
                        ivLogo.setImageResource(R.drawable.cash);
                        tv_name.setText(ResourcesUtils.getString(getContext(), R.string.pay_need_money));
                        tvMoney.setText(mMoney);
                        if (!MainActivity.isVertical && activity.textDisplay != null) {
                            activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + mMoney);
                        }
                        btnOk.setVisibility(View.VISIBLE);
                        btnOk.setText(ResourcesUtils.getString(getContext(), R.string.pay_confirm));
                        rbOne.setAlpha(1f);
                        rbTwo.setAlpha(0.7f);
                        rbthree.setAlpha(0.7f);
                        if (activity.textDisplay != null) {
                            activity.textDisplay.setSelect(0);
                        }
                        break;
                    case R.id.rbtwo:
                        ivTop.setVisibility(View.INVISIBLE);
                        ivMid.setVisibility(View.VISIBLE);
                        ivBottom.setVisibility(View.INVISIBLE);
                        tvFaccPayTips.setVisibility(View.GONE);
                        llCanPay.setVisibility(View.VISIBLE);
                        llNoFace.setVisibility(View.GONE);
                        llPayFail.setVisibility(View.GONE);
                        tvDescrib.setText(ResourcesUtils.getString(getContext(), R.string.tips_pay_qrcode));
                        ivLogo.setImageResource(R.drawable.paycode);
                        tv_name.setText(ResourcesUtils.getString(getContext(), R.string.pay_need_money));
                        tvMoney.setText(mMoney);
                        if (!MainActivity.isVertical && activity.textDisplay != null) {
                            activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + mMoney);
                        }

                        btnOk.setVisibility(View.GONE);
                        rbOne.setAlpha(0.7f);
                        rbTwo.setAlpha(1);
                        rbthree.setAlpha(0.7f);
                        if (activity.textDisplay != null) {
                            activity.textDisplay.setSelect(1);
                        }
                        break;
                    case R.id.rbthree:
                        ivTop.setVisibility(View.INVISIBLE);
                        ivMid.setVisibility(View.INVISIBLE);
                        ivBottom.setVisibility(View.VISIBLE);
                        rbOne.setAlpha(0.7f);
                        rbTwo.setAlpha(0.7f);
                        rbthree.setAlpha(1);
                        if(!isCanFacePay()){
                            return ;
                        }
                        tv_name.setText(ResourcesUtils.getString(getContext(), R.string.pay_total_moeny) + mMoney + "  " + ResourcesUtils.getString(getContext(), R.string.pay_give_money));
                        tvMoney.setText(ResourcesUtils.getString(getContext(), R.string.units_money_units) + "0.01");
                        if (!MainActivity.isVertical && activity.textDisplay != null) {
                            activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + ResourcesUtils.getString(getContext(), R.string.units_money_units) + "0.0" + AlipaySmileModel.i);
                        }
                        tvFaccPayTips.setVisibility(View.VISIBLE);
                        tvDescrib.setText(ResourcesUtils.getString(getContext(), R.string.pay_face_tips));
                        ivLogo.setImageResource(R.drawable.face_recognition);
                        btnOk.setVisibility(View.VISIBLE);
                        btnOk.setText(ResourcesUtils.getString(getContext(), R.string.pay_get_moeny));
                        if (activity.textDisplay != null) {
                            activity.textDisplay.setSelect(2);
                        }
                        break;
                    default:
                        break;
                }

                switch (payMode) {
                    case PAY_CASH + PAY_CODE + PAY_FACE:
                        break;
                    case PAY_CODE + PAY_FACE:
                        ivTop.setVisibility(View.GONE);
                        break;
                    case PAY_FACE:
                        ivTop.setVisibility(View.GONE);
                        ivMid.setVisibility(View.GONE);
                        break;
                }

            }
        });
        btnComplete.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                int action = event.getAction();
                switch (action) {
                    case KeyEvent.ACTION_DOWN:
                        int unicodeChar = event.getUnicodeChar();
                        sb.append((char) unicodeChar);
                        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                            return false;
                        }
                        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                            return false;
                        }
                        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                            return false;
                        }
                        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                            return false;
                        }
                        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
                            return false;
                        }
                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
                            return false;
                        }
                        final int len = sb.length();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (len != sb.length()) return;
                                if (sb.length() > 0) {
                                    if (rbTwo.isChecked()) {
                                        payByCode();
                                    }
                                    sb.setLength(0);
                                }
                            }
                        }, 200);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        if (activity.textDisplay != null) {
            activity.textDisplay.setOnClickListener(this);
        }
    }

    private boolean isCanFacePay() {
        if(isHaveCamera()&&InstallApkUtils.checkApkExist(getContext(),InstallApkUtils.smilePkgName)){
            return true;
        }
        ivLogo.setImageResource(R.drawable.face_unsupport);
        llCanPay.setVisibility(View.GONE);
        llNoFace.setVisibility(View.VISIBLE);
        llPayFail.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        if (!isHaveCamera() && !MainActivity.isVertical) {
            noFaceTitle.setText(ResourcesUtils.getString(R.string.pay_noface));
            noFaceContent.setText(ResourcesUtils.getString(R.string.pay_noface_change));
        }else if(!InstallApkUtils.checkApkExist(getContext(),InstallApkUtils.smilePkgName)){
            noFaceTitle.setText(ResourcesUtils.getString(R.string.tips_no_find_smile_title));
            noFaceContent.setText(ResourcesUtils.getString(R.string.tips_find_smile_content));
        }
        return false;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mMoney = bundle.getString("MONEY", "0.00");
        payMode = bundle.getInt("PAYMODE", 7);
        MenuBean menuBean = JSON.parseObject(bundle.getString("GOODS", ""), MenuBean.class);
        Log.e(TAG, menuBean.toString());
        tvMoney.setText(mMoney);
        tvMoneyComplete.setText(mMoney);

        payment = (boolean) SharePreferenceUtil.getParam(getContext(), PayModeSettingFragment.PAYMENT_PAY_KEY, false);
        if (payment) {
            payMentPayPresenter = new PayMentPayPresenter(getContext());
            payMentPayPresenter.init(this);
        } else {
            alipaySmileModel = new AlipaySmileModel();
            alipaySmilePresenter = new AlipaySmilePresenter(getContext().getApplicationContext(), alipaySmileModel);
            alipaySmileModel.setGoods(menuBean);
            alipaySmilePresenter.init(alipaySmileModel.buildMerchantInfo(), mMoney.substring(1), ResourcesUtils.getString(getContext(), R.string.menus_title), ResourcesUtils.getString(getContext(), R.string.menus_title2));
        }
        switch (payMode) {
            case PAY_CASH + PAY_CODE + PAY_FACE:
                break;
            case PAY_CODE + PAY_FACE:
                rbOne.setVisibility(View.GONE);
                ivTop.setVisibility(View.GONE);
                rbTwo.performClick();
                rbTwo.setChecked(true);
                break;
            case PAY_FACE:
                rbOne.setVisibility(View.GONE);
                ivTop.setVisibility(View.GONE);
                rbTwo.setVisibility(View.GONE);
                ivMid.setVisibility(View.GONE);
                rbthree.performClick();
                rbthree.setChecked(true);
                llyPay.setVisibility(View.GONE);
                llPaying.setVisibility(View.VISIBLE);
                startFacePaying();
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (completeListener != null) {
                    completeListener.onCancel();
                }
                dismiss();
                break;
            case R.id.btn_ok:
                if (isPay) {
                    return;
                }
                isPay = true;
                rbOne.setClickable(false);
                rbTwo.setClickable(false);
                rbthree.setClickable(false);
                if (!rbOne.isChecked()) {
                   startFacePaying();
                } else {
                    llyPay.setVisibility(View.GONE);
                    llyPayComplete.setVisibility(View.VISIBLE);
                    tv_pay_success.setText(R.string.pay_confirm_true);
                    if (completeListener != null) {
                        completeListener.onSuccess(0);
                    }
                }
                break;
            case R.id.btn_complete:
                isPay = false;
                if (completeListener != null) {
                    if (rbOne.isChecked()) {
                        completeListener.onComplete(0);
                    }
                    if (rbTwo.isChecked()) {
                        completeListener.onComplete(1);
                    }
                    if (rbthree.isChecked()) {
                        completeListener.onComplete(2);
                    }
                }
                dismiss();
                break;


            case R.id.paymode_one:
                rbOne.performClick();
                break;
            case R.id.paymode_two:
                rbTwo.performClick();
                break;
            case R.id.paymode_three:
                rbthree.performClick();
                break;

            case R.id.present_fail_one:
                btnOk.performClick();
                break;
            case R.id.present_fail_two:
                activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + mMoney);
                rbOne.performClick();
                break;
            case R.id.present_fail_three:
                btnCancel.performClick();
                break;
            default:
                break;
        }
    }


    //收银台支付回调

    @Override
    public void onSuccess(PaymentResponse response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isPay = false;
                tvMoneyComplete.setText(ResourcesUtils.getString(getContext(), R.string.units_money_units) + "0.0" + 1);
                llPaying.setVisibility(View.GONE);
                llyPayComplete.setVisibility(View.VISIBLE);
                tv_pay_success.setText(R.string.pay_pay_true);
                if (completeListener != null) {
                    completeListener.onSuccess(2);
                }

            }
        });
    }

    @Override
    public void onFail(PaymentResponse response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isPay = false;
                Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                rbOne.setClickable(true);
                rbTwo.setClickable(true);
                rbthree.setClickable(true);
                llyPay.setVisibility(View.VISIBLE);
                llPaying.setVisibility(View.GONE);
                llCanPay.setVisibility(View.GONE);
                llNoFace.setVisibility(View.GONE);
                llPayFail.setVisibility(View.VISIBLE);
                tv_pay_fail_money.setText(ResourcesUtils.getString(getContext(), R.string.pay_pay_moeny) + ResourcesUtils.getString(getContext(), R.string.units_money) + "0.0" + AlipaySmileModel.i);
                ivLogo.setVisibility(View.GONE);
                btnOk.setVisibility(View.VISIBLE);
                btnOk.setText(ResourcesUtils.getString(getContext(), R.string.pay_repay));
                if (activity.textDisplay != null) {
                    activity.textDisplay.setPayFail();
                }

            }
        });
    }

    private void startFacePaying(){
        llyPay.setVisibility(View.GONE);
        llPaying.setVisibility(View.VISIBLE);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (activity.textDisplay != null) {
                    activity.textDisplay.setPaying();
                }
            }
        });
        if (payment) {
            String orderId = "" + (System.currentTimeMillis() / 1000) + (int) (Math.random() * 9000 + 1000);
            payMentPayPresenter.startFaceService(orderId, phoneNumber);
        } else {
            alipaySmilePresenter.setPhoneNumber(phoneNumber);
            alipaySmilePresenter.startFaceService(callBack);
        }
    }

    //支付宝刷脸支付回调
    AlipaySmilePresenter.AlipaySmileCallBack callBack = new AlipaySmilePresenter.AlipaySmileCallBack() {
        @Override
        public void onStartFaceService() {
            sendMessageToUser("onStartFaceService");
            isPay = false;

        }

        @Override
        public void onFaceSuccess(String code, String msg) {
            sendMessageToUser("刷脸" + code + "  " + msg);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rbOne.setClickable(false);
                    rbTwo.setClickable(false);
                    rbthree.setClickable(false);
                    llyPay.setVisibility(View.GONE);
                    llPaying.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onSuccess(String code, String msg) {
            sendMessageToUser("支付成功" + code + "  " + msg);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMoneyComplete.setText(ResourcesUtils.getString(getContext(), R.string.units_money_units) + "0.0" + (AlipaySmileModel.i));
                    llPaying.setVisibility(View.GONE);
                    llyPayComplete.setVisibility(View.VISIBLE);
                    tv_pay_success.setText(R.string.pay_pay_true);
                    if (completeListener != null) {
                        completeListener.onSuccess(2);
                    }
                }
            });
        }

        @Override
        public void onGetMetaInfo(String metaInfo) {


        }

        @Override
        public void onGetZimIdSuccess(String zimId) {
            sendMessageToUser("获得id成功" + zimId);
            isPay = false;
        }


        @Override
        public void onFail(String code, final String msg) {
            sendMessageToUser("失败" + code + "  " + msg);
            isPay = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rbOne.setClickable(true);
                    rbTwo.setClickable(true);
                    rbthree.setClickable(true);
                    llyPay.setVisibility(View.VISIBLE);
                    llPaying.setVisibility(View.GONE);
                    llCanPay.setVisibility(View.GONE);
                    llNoFace.setVisibility(View.GONE);
                    llPayFail.setVisibility(View.VISIBLE);
                    tv_pay_fail_money.setText(ResourcesUtils.getString(getContext(), R.string.pay_pay_moeny) + ResourcesUtils.getString(getContext(), R.string.units_money) + "0.0" + AlipaySmileModel.i);
                    ivLogo.setVisibility(View.GONE);
                    btnOk.setVisibility(View.VISIBLE);
                    btnOk.setText(ResourcesUtils.getString(getContext(), R.string.pay_repay));
                    if (activity.textDisplay != null) {
                        activity.textDisplay.setPayFail();
                    }

                }
            });
        }

    };

    public void sendMessageToUser(final String msg) {
        Log.i("@@@@@@@@", msg);
    }

    //会员支付
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //扫码支付
    public void payByCode() {
        if (rbTwo.isChecked()) {
            rbOne.setClickable(false);
            rbTwo.setClickable(false);
            rbthree.setClickable(false);
            llyPay.setVisibility(View.GONE);
            llyPayComplete.setVisibility(View.VISIBLE);
            if (completeListener != null) {
                completeListener.onSuccess(1);
            }
        }
    }

    private boolean isHaveCamera() {
        HashMap<String, UsbDevice> deviceHashMap = ((UsbManager) getActivity().getSystemService(getActivity().USB_SERVICE)).getDeviceList();
        Log.e(TAG, "isHaveCamera: " + deviceHashMap.size());
        for (Map.Entry entry : deviceHashMap.entrySet()) {
            UsbDevice usbDevice = (UsbDevice) entry.getValue();
            Log.e(TAG, "detectUsbDeviceWithUsbManager: " + entry.getKey() + "======== " + usbDevice);
            Log.e(TAG, "isHaveCamera: " + usbDevice.getInterface(0).getName());
            if (!TextUtils.isEmpty(usbDevice.getInterface(0).getName()) && usbDevice.getInterface(0).getName().contains("Orb")) {
                return true;
            }
            if (!TextUtils.isEmpty(usbDevice.getInterface(0).getName()) && usbDevice.getInterface(0).getName().contains("Astra")) {
                return true;
            }
        }
        return false;
    }

    public void reFormatMoney(MainActivity activity) {
        if (activity.textDisplay != null && rbthree.isChecked()) {
            activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + ResourcesUtils.getString(getContext(), R.string.units_money_units) + "0.0" + AlipaySmileModel.i);
        } else if (activity.textDisplay != null) {
            activity.textDisplay.update(ResourcesUtils.getString(getContext(), R.string.pay_give_money) + mMoney);
        }
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
    public void onDestroy() {
        if (payMentPayPresenter != null) {
            payMentPayPresenter.destoryReceiver();
            payMentPayPresenter = null;
        }

        if (alipaySmilePresenter != null) {
            alipaySmilePresenter.close();
            alipaySmilePresenter = null;
        }
        super.onDestroy();

    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }
}
