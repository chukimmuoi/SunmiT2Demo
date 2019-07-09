package com.sunmi.sunmit2demo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.scalelibrary.ScaleManager;
import com.sunmi.sunmit2demo.BaseActivity;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.fragment.DeviceFragment;
import com.sunmi.sunmit2demo.fragment.GoodsManagerFragment;
import com.sunmi.sunmit2demo.fragment.HandSettingFragment;
import com.sunmi.sunmit2demo.fragment.PayModeSettingFragment;
import com.sunmi.sunmit2demo.present.VideoDisplay;
import com.sunmi.sunmit2demo.utils.ScreenManager;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class MoreActivity extends BaseActivity implements View.OnClickListener {


    private ImageView ivBack;
    public ScaleManager mScaleManager;
    public ScreenManager screenManager = null;
    public Display[] displays;
    public IWoyouService woyouService;
    public VideoDisplay videoDisplay = null;
    public final  static String GOODSMODE_KEY= "GOODSMODE_KEY";
    public static int Goods_Mode= 16;

    public final static int Goods_1= 1;
    public final static int Goods_2 = 2;
    public final static int Goods_3 = 4;
    public final static int Goods_4 = 8;

    private DeviceFragment deviceFragment;
    private GoodsManagerFragment goodsManagerFragment;
    private HandSettingFragment braceletFragment;
    private PayModeSettingFragment payModeSettingFragment;
    int openHand = 0;

    private TextView tv_device,tv_goods_manage,tv_pay_mode,tv_hand,tv_more;

    private FrameLayout fl_1,fl_2,fl_3,fl_4;

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        connectPrintService();
        initView();
        initAction();
        initData();
        connectScaleService();
    }

    private void connectPrintService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private void connectScaleService() {
        mScaleManager = ScaleManager.getInstance(this);
        mScaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
            @Override
            public void onServiceConnected() {
                Log.e("@@@@@@@@", "电子称连接成功");
            }

            @Override
            public void onServiceDisconnect() {
                Log.e("@@@@@@@@", "电子称连接失败");

            }
        });
    }

    private StringBuilder sb = new StringBuilder();
    boolean isQRcode = false;
    private Handler myHandler = new Handler();
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                int unicodeChar = event.getUnicodeChar();
                sb.append((char) unicodeChar);
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
                    return super.dispatchKeyEvent(event);
                }
                if (!isQRcode) {
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (sb.length() > 0) {
                                if (deviceFragment.isVisible()&&deviceFragment.isShowScanResult) {
                                    deviceFragment.append(sb.toString());
                                }

                                    sb.setLength(0);
                            }
                            isQRcode = false;
                        }
                    }, 300);
                    return true;
                }
                isQRcode = true;
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        deviceFragment=new DeviceFragment();
        goodsManagerFragment=new GoodsManagerFragment();
        payModeSettingFragment=new PayModeSettingFragment();
        braceletFragment = new HandSettingFragment();
        tv_device=findViewById(R.id.tv_device);
        tv_goods_manage=findViewById(R.id.tv_goods_manage);
        tv_pay_mode=findViewById(R.id.tv_pay_mode);
        tv_hand=findViewById(R.id.tv_hand);
        tv_more = findViewById(R.id.tv_more);
        fl_1=findViewById(R.id.fl_1);
        fl_2=findViewById(R.id.fl_2);
        fl_3=findViewById(R.id.fl_3);
        fl_4=findViewById(R.id.fl_4);

        addContent(deviceFragment,false);
        checkState(0);
    }

    private void initAction() {
        ivBack.setOnClickListener(this);
        fl_1.setOnClickListener(this);
        fl_2.setOnClickListener(this);
        fl_3.setOnClickListener(this);
        fl_4.setOnClickListener(this);
        tv_more.setOnClickListener(this);
    }

    private void initData() {
        Goods_Mode= (int) SharePreferenceUtil.getParam(this, MoreActivity.GOODSMODE_KEY,16);
        screenManager = ScreenManager.getInstance();
        screenManager.init(this);
        displays = screenManager.getDisplays();
        videoDisplay = new VideoDisplay(this, displays[0], Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4");

    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tv_more:
              openHand++;
              if(openHand == 15){
                  fl_4.setVisibility(View.VISIBLE);
              }
              break;
          case R.id.fl_1:
              checkState(0);
              replaceContent(deviceFragment,false);
              break;
          case R.id.fl_2:
              checkState(1);
              replaceContent(goodsManagerFragment,false);
              break;
          case R.id.fl_3:
              checkState(2);
              replaceContent(payModeSettingFragment,false);
              break;
          case R.id.fl_4:
              checkState(3);
              this.replaceContent(braceletFragment, false);
              break;
          case R.id.iv_back:
              finish();
              break;
      }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoDisplay != null && videoDisplay.isShowing()) {
            videoDisplay.dismiss();
        }
    }
    private void checkState(int index){
        fl_1.setBackgroundColor(Color.TRANSPARENT);
        fl_2.setBackgroundColor(Color.TRANSPARENT);
        fl_3.setBackgroundColor(Color.TRANSPARENT);
        fl_4.setBackgroundColor(Color.TRANSPARENT);
        switch (index){
            case 0:
                fl_1.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
            case 1:
                fl_2.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
            case 2:
                fl_3.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
        }
    }

}
