package com.sunmi.sunmit2demo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.sunmi.electronicscaleservice.ScaleCallback;
import com.sunmi.extprinterservice.ExtPrinterService;
import com.sunmi.scalelibrary.ScaleManager;
import com.sunmi.sunmit2demo.BaseActivity;
import com.sunmi.sunmit2demo.BasePresentationHelper;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.adapter.GvAdapter;
import com.sunmi.sunmit2demo.adapter.MenusAdapter;
import com.sunmi.sunmit2demo.bean.GoodsCode;
import com.sunmi.sunmit2demo.bean.GvBeans;
import com.sunmi.sunmit2demo.bean.MenusBean;
import com.sunmi.sunmit2demo.dialog.AddFruitDialogFragment;
import com.sunmi.sunmit2demo.dialog.PayDialog;
import com.sunmi.sunmit2demo.fragment.PayModeSettingFragment;
import com.sunmi.sunmit2demo.model.AlipaySmileModel;
import com.sunmi.sunmit2demo.present.TextDisplay;
import com.sunmi.sunmit2demo.present.VideoDisplay;
import com.sunmi.sunmit2demo.present.VideoMenuDisplay;
import com.sunmi.sunmit2demo.presenter.KPrinterPresenter;
import com.sunmi.sunmit2demo.presenter.PrinterPresenter;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;
import com.sunmi.sunmit2demo.utils.ScreenManager;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;
import com.sunmi.sunmit2demo.view.MyGridView;
import com.sunmi.sunmit2demo.view.VipPayDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private ListView lvMenus;
    private MenusAdapter menusAdapter;
    private List<MenusBean> menus = new ArrayList<>();
    private MyGridView gvDrink;
    private MyGridView gvFruit;
    private MyGridView gv_snacks;
    private MyGridView gv_vegetables;
    private FrameLayout flUnlockUser;
    private TextView tv_user_lock;
    private CircleImageView ivUserHeadIcon;
    private FrameLayout flShoppingCar;

    private GvAdapter drinkAdapter;
    private GvAdapter fruitAdapter;
    private GvAdapter snackAdapter;
    private GvAdapter vegetableAdapter;
    private List<GvBeans> mDrinksBean = new ArrayList<>();
    private List<GvBeans> mFruitsBean = new ArrayList<>();
    private List<GvBeans> mSnacksBean = new ArrayList<>();
    private List<GvBeans> mVegetablesBean = new ArrayList<>();
    private TextView tvPrice;
    private TextView btnClear;
    private RelativeLayout rtlEmptyShopcar, rl_no_goods;
    private LinearLayout llyShopcar, ll_drinks, ll_snacks, ll_fruits, ll_vegetables, main_ll_pay;

    private ImageView ivCar;
    private RelativeLayout rlCar;
    private TextView tvCar, tvCarMoeny;
    private TextView tvVipPay, tvVipK1Pay;

    private Button btnPay;//去付款
    private BottomSheetLayout bottomSheetLayout;
    private LinearLayout llK1ShoppingCar;


    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Button btnMore;//更多功能
    private TextView tv_face_pay;//去付款
    private ScaleManager mScaleManager;
    private VideoDisplay videoDisplay = null;
    private ScreenManager screenManager = ScreenManager.getInstance();
    private VideoMenuDisplay videoMenuDisplay = null;
    private AddFruitDialogFragment dialogFragment = null;
    public TextDisplay textDisplay = null;
    private PayDialog payDialog;
    public static int net;
    private IWoyouService woyouService = null;//横屏台式 打印服务
    private ExtPrinterService extPrinterService = null;//k1 打印服务

    private String goods_data;
    private PrinterPresenter printerPresenter;
    private KPrinterPresenter kPrinterPresenter;
    boolean isHaveSound = false;//是否有声音
    public static boolean isShowTime = false;//是否是展会
    public GoodsCode goodsCode = GoodsCode.getInstance();
    private boolean willwelcome;

    public static boolean isVertical = false;
    SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
    private boolean isPayComplete = true;
    private int payCompleteTime = 5;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectScaleService();
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度
        int height = dm.heightPixels;// 屏幕宽度
        Log.e("@@@", dm.densityDpi + "  " + dm.density);
        isVertical = height > width;
        if (isVertical) {
            connectKPrintService();
        } else {
            connectPrintService();
        }
        menus.clear();
        initView();
        initData();
        initAction();

    }

    //连接K1打印服务
    private void connectKPrintService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.extprinterservice");
        intent.setAction("com.sunmi.extprinterservice.PrinterService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    //连接打印服务
    private void connectPrintService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
            extPrinterService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (isVertical) {
                extPrinterService = ExtPrinterService.Stub.asInterface(service);
                kPrinterPresenter = new KPrinterPresenter(MainActivity.this, extPrinterService);
            } else {
                woyouService = IWoyouService.Stub.asInterface(service);
                printerPresenter = new PrinterPresenter(MainActivity.this, woyouService);
            }
        }
    };

    protected void onStop() {
        super.onStop();
        this.willwelcome = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menus.size() > 0) {
            float price = 0.00f;
            for (MenusBean bean1 : menus) {
                price = price + Float.parseFloat(bean1.getMoney().substring(1));
            }
            buildMenuJson(menus, decimalFormat.format(price));
        } else {
            if (videoDisplay != null) {
                videoDisplay.show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        int goodsMode = (int) SharePreferenceUtil.getParam(this, MoreActivity.GOODSMODE_KEY, 16);
        switch (goodsMode) {
            case 0:
                ll_fruits.setVisibility(View.GONE);
                ll_vegetables.setVisibility(View.GONE);
                ll_snacks.setVisibility(View.GONE);
                ll_drinks.setVisibility(View.GONE);
                rl_no_goods.setVisibility(View.VISIBLE);
                break;
            case MoreActivity.Goods_1 | MoreActivity.Goods_2:
                ll_fruits.setVisibility(View.GONE);
                ll_vegetables.setVisibility(View.GONE);
                ll_drinks.setVisibility(View.VISIBLE);
                ll_snacks.setVisibility(View.VISIBLE);
                rl_no_goods.setVisibility(View.GONE);
                break;
            case MoreActivity.Goods_3 | MoreActivity.Goods_4:
                ll_snacks.setVisibility(View.GONE);
                ll_drinks.setVisibility(View.GONE);
                ll_fruits.setVisibility(View.VISIBLE);
                ll_vegetables.setVisibility(View.VISIBLE);
                rl_no_goods.setVisibility(View.GONE);
                break;
            case MoreActivity.Goods_1 | MoreActivity.Goods_2 + MoreActivity.Goods_3 | MoreActivity.Goods_4:
                ll_drinks.setVisibility(View.VISIBLE);
                ll_snacks.setVisibility(View.VISIBLE);
                ll_fruits.setVisibility(View.VISIBLE);
                rl_no_goods.setVisibility(View.GONE);
                ll_vegetables.setVisibility(View.VISIBLE);
                break;
        }
        int payMode = (int) SharePreferenceUtil.getParam(this, PayDialog.PAY_MODE_KEY, 7);
        switch (payMode) {
            case PayDialog.PAY_FACE:
                tv_face_pay.setVisibility(View.VISIBLE);
                break;
            case PayDialog.PAY_FACE | PayDialog.PAY_CODE | PayDialog.PAY_CASH:
                tv_face_pay.setVisibility(View.GONE);
                break;
        }
        boolean vip = (boolean) SharePreferenceUtil.getParam(this, PayModeSettingFragment.VIP_PAY_KEY, false);
        tvVipPay.setVisibility(vip ? View.VISIBLE : View.GONE);
        tvVipK1Pay.setVisibility(vip ? View.VISIBLE : View.GONE);
    }

    //连接电子秤服务
    private void connectScaleService() {
        mScaleManager = ScaleManager.getInstance(this);
        mScaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
            @Override
            public void onServiceConnected() {
                getScaleData();
            }

            @Override
            public void onServiceDisconnect() {

            }
        });
    }

    private void getScaleData() {
        try {
            mScaleManager.getData(new ScaleCallback.Stub() {
                @Override
                public void getData(final int i, int i1, final int i2) throws RemoteException {
                    net = i;
                    // i = 净重量 单位 克 ，i1 = 皮重量 单位 克 ，i2 = 稳定状态  1 为稳定。具体其他状态请参考商米开发者文档
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.update(i2, i);
                            }
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lvMenus = (ListView) findViewById(R.id.lv_menus);
        tvPrice = (TextView) findViewById(R.id.main_tv_price);
        btnClear = (TextView) findViewById(R.id.main_btn_clear);
        llyShopcar = (LinearLayout) findViewById(R.id.lly_shopcar);
        rtlEmptyShopcar = (RelativeLayout) findViewById(R.id.rtl_empty_shopcar);
        flShoppingCar = (FrameLayout) findViewById(R.id.fl_shopping_car);
        tv_face_pay = findViewById(R.id.tv_face_pay);
        main_ll_pay = findViewById(R.id.main_ll_pay);


        btnMore = (Button) findViewById(R.id.main_btn_more);

        gvDrink = findViewById(R.id.gv_drinks);
        gvFruit = findViewById(R.id.gv_fruits);
        gv_snacks = findViewById(R.id.gv_snacks);
        gv_vegetables = findViewById(R.id.gv_vegetables);

        gvDrink.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gvFruit.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_snacks.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_vegetables.setSelector(new ColorDrawable(Color.TRANSPARENT));

        ll_drinks = findViewById(R.id.ll_drinks);
        ll_snacks = findViewById(R.id.ll_snacks);
        ll_fruits = findViewById(R.id.ll_fruits);
        ll_vegetables = findViewById(R.id.ll_vegetables);
        rl_no_goods = findViewById(R.id.rl_no_goods);

        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        btnPay = findViewById(R.id.main_k1_btn_pay);
        tvCarMoeny = findViewById(R.id.tv_car_money);
        tvCar = findViewById(R.id.tv_car_num);
        ivCar = findViewById(R.id.iv_car);
        rlCar = findViewById(R.id.main_btn_car);
        llK1ShoppingCar = (LinearLayout) findViewById(R.id.ll_k1_shopping_car);

        tvVipPay = findViewById(R.id.vip_pay);
        tvVipK1Pay = findViewById(R.id.vip_k1__pay);
        if (isVertical) {
            llK1ShoppingCar.setVisibility(View.VISIBLE);
            flShoppingCar.setVisibility(View.GONE);
        } else {
            llK1ShoppingCar.setVisibility(View.GONE);
            flShoppingCar.setVisibility(View.VISIBLE);
            llyShopcar.setVisibility(View.GONE);
            rtlEmptyShopcar.setVisibility(View.VISIBLE);
        }

        flUnlockUser = (FrameLayout) findViewById(R.id.fl_unlock_user);
        tv_user_lock = (TextView) findViewById(R.id.tv_user_lock);
        ivUserHeadIcon = (CircleImageView) findViewById(R.id.iv_user_head_icon);

    }

    private void initAction() {
        gvDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mDrinksBean.get(position).getPrice());
                bean.setName(mDrinksBean.get(position).getName());
                menus.add(bean);
                float price = 0.00f;
                for (MenusBean bean1 : menus) {
                    price = price + Float.parseFloat(bean1.getMoney().substring(1));
                }
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + decimalFormat.format(price));
                menusAdapter.update(menus);

                buildMenuJson(menus, decimalFormat.format(price));

            }
        });
        gv_snacks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mSnacksBean.get(position).getPrice());
                bean.setName(mSnacksBean.get(position).getName());
                menus.add(bean);
                float price = 0.00f;
                for (MenusBean bean1 : menus) {
                    price = price + Float.parseFloat(bean1.getMoney().substring(1));
                }
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + decimalFormat.format(price));
                menusAdapter.update(menus);

                buildMenuJson(menus, decimalFormat.format(price));

            }
        });

        gv_vegetables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("FLAG", position);
                bundle.putInt("net", net);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "AddFruitDialogFragment");
                dialogFragment.updateView(mVegetablesBean.get(position));

            }
        });

        gvFruit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("FLAG", position);
                bundle.putInt("net", net);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "AddFruitDialogFragment");
                dialogFragment.updateView(mFruitsBean.get(position));

            }
        });

        btnClear.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        main_ll_pay.setOnClickListener(this);
        flUnlockUser.setOnClickListener(this);

        btnPay.setOnClickListener(this);
        rlCar.setOnClickListener(this);

        tvVipPay.setOnClickListener(this);
        tvVipK1Pay.setOnClickListener(this);
    }

    private void initData() {
        screenManager.init(this);
        Display[] displays = screenManager.getDisplays();
        Log.e(TAG, "屏幕数量" + displays.length);
        for (int i = 0; i < displays.length; i++) {
            Log.e(TAG, "屏幕" + displays[i]);
        }
        if (displays.length > 1 && !isVertical) {
            videoDisplay = new VideoDisplay(this, displays[1], Environment.getExternalStorageDirectory().getPath() + "/video_02.mp4");
            videoMenuDisplay = new VideoMenuDisplay(this, displays[1], Environment.getExternalStorageDirectory().getPath() + "/video_02.mp4");
            textDisplay = new TextDisplay(this, displays[1]);
        }
        initDrinks();
        initFruits();
        initSnacks();
        initVegetables();
        drinkAdapter = new GvAdapter(this, mDrinksBean, 1);
        gvDrink.setAdapter(drinkAdapter);
        fruitAdapter = new GvAdapter(this, mFruitsBean, 2);
        gvFruit.setAdapter(fruitAdapter);

        snackAdapter = new GvAdapter(this, mSnacksBean, 3);
        gv_snacks.setAdapter(snackAdapter);
        vegetableAdapter = new GvAdapter(this, mVegetablesBean, 2);
        gv_vegetables.setAdapter(vegetableAdapter);

        menus.clear();
        tvPrice.setText(ResourcesUtils.getString(this, R.string.units_money_units) + "0.00");

        menusAdapter = new MenusAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
        dialogFragment = new AddFruitDialogFragment();
        dialogFragment.setListener(new AddFruitDialogFragment.AddListener() {

            @Override
            public void onAddResult(String total, String name) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + total);
                bean.setName(name);
                menus.add(bean);
                float price = 0.00f;
                for (MenusBean bean1 : menus) {
                    price = price + Float.parseFloat(bean1.getMoney().substring(1));
                }
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + decimalFormat.format(price));
                menusAdapter.update(menus);

                buildMenuJson(menus, decimalFormat.format(price));
            }
        });

        payDialog = new PayDialog();
        payDialog.setCompleteListener(new PayDialog.OnCompleteListener() {
            @Override
            public void onCancel() {
                if (menus.size() > 0) {
                    if (videoMenuDisplay != null) {
                        videoMenuDisplay.show();
                    }

                }
            }

            @Override
            public void onSuccess(final int payMode) {
                if (!isPayComplete) {
                    return;
                }
                payDialog.btnComplete.setText(ResourcesUtils.getString(R.string.tips_confirm) + "(" + payCompleteTime + ")");
                paySuccessToAutoComplete();
                playSound(payMode);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paySuccessToPrinter(payMode);
                        payDialog.setPhoneNumber("");

                    }
                }, 1000);

            }

            @Override
            public void onComplete(int payMode) {
                isPayComplete = true;
                payCompleteTime = 5;
                payDialog.btnComplete.setText(ResourcesUtils.getString(R.string.tips_confirm));
                payCompleteToReMenu();
            }
        });

        soundPool.load(MainActivity.this, R.raw.audio, 1);// 1
        soundPool.load(MainActivity.this, isZh(this) ? R.raw.alipay : R.raw.alipay_en, 1);// 2
    }

    private void initVegetables() {
        mVegetablesBean.clear();
        for (GvBeans gvBeans : goodsCode.getVegetables()) {
            mVegetablesBean.add(gvBeans);
        }
    }

    private void initSnacks() {
        mSnacksBean.clear();
        for (GvBeans gvBeans : goodsCode.getSnacks()) {
            mSnacksBean.add(gvBeans);
        }
    }

    private void initFruits() {
        mFruitsBean.clear();
        for (GvBeans gvBeans : goodsCode.getFruits()) {
            mFruitsBean.add(gvBeans);
        }

    }

    private void initDrinks() {
        mDrinksBean.clear();
        for (GvBeans gvBeans : goodsCode.getDrinks()) {
            mDrinksBean.add(gvBeans);
        }

    }

    private void sayBye() {
        if (null != textDisplay && !textDisplay.isShow) {
            textDisplay.show();
            textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.tips_bye), 2);
        } else if (null != textDisplay) {
            textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.tips_bye), 2);
        }
    }

    private void payCompleteToReMenu() {
        if (!isVertical) {
            sayBye();
            llyShopcar.setVisibility(View.GONE);
            rtlEmptyShopcar.setVisibility(View.VISIBLE);
            tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + "0.00");
            menus.clear();
            menusAdapter.update(menus);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    standByTime();
                }
            }, 3000);
        } else {
            menus.clear();
            tvCarMoeny.setText("");
            tvCar.setText("");
            tvCar.setVisibility(View.GONE);
            ivCar.setImageResource(R.drawable.car_gray);
            bottomSheetLayout.dismissSheet();
            btnPay.setBackgroundColor(Color.parseColor("#999999"));
        }
    }


    private void paySuccessToPrinter(int payMode) {
        if (isVertical) {
            if (kPrinterPresenter != null) {
                kPrinterPresenter.print(goods_data, payMode);
            }
        } else {
            if (null != textDisplay && !textDisplay.isShow) {
                textDisplay.show();
                textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.pay_confirm_true) + tvPrice.getText().toString(), 1);
            } else if (null != textDisplay) {
                textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.pay_confirm_true) + tvPrice.getText().toString(), 1);
            }
            if (payMode == 2 && null != textDisplay) {
                textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.pay_confirm_true) + ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + "0.0" + (AlipaySmileModel.i), 1);
            }
            if (printerPresenter != null) {
                printerPresenter.print(goods_data, payMode);
            }
        }
    }

    private void paySuccessToAutoComplete() {
        isPayComplete = false;
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPayComplete) {
                    paySuccessToAutoComplete();
                    payCompleteTime--;
                    payDialog.btnComplete.setText(ResourcesUtils.getString(R.string.tips_confirm) + "(" + payCompleteTime + ")");
                    if (payCompleteTime == -1) {
                        payDialog.btnComplete.performClick();
                    }
                }
            }
        }, 950);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.main_btn_clear:
                if (isVertical) {
                    menus.clear();
                    tvCarMoeny.setText("");
                    tvCar.setText("");
                    tvCar.setVisibility(View.GONE);
                    ivCar.setImageResource(R.drawable.car_gray);
                    bottomSheetLayout.dismissSheet();
                    btnPay.setBackgroundColor(Color.parseColor("#999999"));
                } else {
                    llyShopcar.setVisibility(View.GONE);
                    rtlEmptyShopcar.setVisibility(View.VISIBLE);
                    menus.clear();
                    tvPrice.setText(ResourcesUtils.getString(this, R.string.units_money_units) + "0.00");
                    menusAdapter.update(menus);
                    if (videoDisplay != null) {
                        videoDisplay.show();
                    }
                }
                break;
            case R.id.main_btn_more:
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.vip_k1__pay:
                if (menus.size() > 0) {
                    vipPay();
                }
                break;
            case R.id.vip_pay:
                vipPay();
                break;
            case R.id.main_ll_pay:
                Bundle bundle = new Bundle();
                bundle.putString("MONEY", tvPrice.getText().toString());
                bundle.putString("GOODS", goods_data);

                int payMode = (int) SharePreferenceUtil.getParam(this, PayDialog.PAY_MODE_KEY, 7);

                bundle.putInt("PAYMODE", payMode);
                payDialog.setArguments(bundle);
                payDialog.show(getSupportFragmentManager(), "payDialog");
                if (null != textDisplay && !textDisplay.isShow) {
                    textDisplay.show();
                    textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.pay_give_money) + tvPrice.getText().toString());
                } else if (null != textDisplay) {
                    textDisplay.update(ResourcesUtils.getString(MainActivity.this, R.string.pay_give_money) + tvPrice.getText().toString());
                }

                break;

            case R.id.main_btn_car:
                if (menus.size() > 0) {
                    if (!bottomSheetLayout.isSheetShowing()) {
                        bottomSheetLayout.showWithSheetView(createBottomSheetView());
                    } else {
                        bottomSheetLayout.dismissSheet();
                    }
                }
                break;
            case R.id.main_k1_btn_pay:
                if (menus.size() > 0) {
                    int payModes = (int) SharePreferenceUtil.getParam(this, PayDialog.PAY_MODE_KEY, 6);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("MONEY", tvCarMoeny.getText().toString());
                    bundle1.putString("GOODS", goods_data);
                    bundle1.putInt("PAYMODE", payModes);
                    payDialog.setArguments(bundle1);
                    payDialog.show(getSupportFragmentManager(), "payDialog");
                }
                break;


            default:
                break;
        }
    }

    VipPayDialog vipPayDialog;

    private void vipPay() {
        vipPayDialog = new VipPayDialog(this).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_cancel:
                        payDialog.setPhoneNumber("");
                        break;
                    case R.id.tv_confirm:
                        payDialog.setPhoneNumber(vipPayDialog.getNum());
                        main_ll_pay.performClick();
                        break;
                }
                vipPayDialog.dismiss();
            }
        });

        vipPayDialog.show();
    }


    private void buildMenuJson(List<MenusBean> menus, String price) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "Sunmi " + ResourcesUtils.getString(this, R.string.menus_title));
            JSONObject head = new JSONObject();
            head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
            head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
            head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
            data.put("head", head);
            data.put("flag", "true");
            JSONArray list = new JSONArray();
            for (int i = 0; i < menus.size(); i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + (i + 1));
                listItem.put("param2", menus.get(i).getName());
                listItem.put("param3", menus.get(i).getMoney());
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total) + " ");
            KVPListOne.put("value", price);
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer) + " ");
            KVPListTwo.put("value", "0.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number) + " ");
            KVPListThree.put("value", "" + menus.size());
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable) + " ");
            KVPListFour.put("value", price);
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            Log.d("HHHH", "onClick: ---------->" + data.toString());
            goods_data = data.toString();
            Log.d(TAG, "buildMenuJson: ------->" + (videoMenuDisplay != null));
            if (payDialog.isVisible()) {
                return;
            }
            if (videoMenuDisplay != null && !videoMenuDisplay.isShow) {
                videoMenuDisplay.show();
                videoMenuDisplay.update(menus, data.toString());
            } else if (null != videoMenuDisplay) {
                videoMenuDisplay.update(menus, data.toString());
            }
            // 购物车有东西

            if (isVertical) {
                tvCarMoeny.setText(ResourcesUtils.getString(R.string.units_money_units) + price);
                tvCar.setText(menus.size() + "");
                tvCar.setVisibility(View.VISIBLE);
                ivCar.setImageResource(R.drawable.car_white);
                btnPay.setBackgroundColor(Color.parseColor("#FC5436"));
                if (bottomSheetLayout.isSheetShowing()) {
                    menusAdapter.notifyDataSetChanged();
                    lvMenus.setSelection(menusAdapter.getCount() - 1);
                    TextView tvPrice = bottomSheetLayout.findViewById(R.id.main_tv_price);
                    tvPrice.setText(tvCarMoeny.getText().toString());
                }
            } else {
                llyShopcar.setVisibility(View.VISIBLE);
                rtlEmptyShopcar.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder sb = new StringBuilder();
    private Handler myHandler = new Handler();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                int unicodeChar = event.getUnicodeChar();
                sb.append((char) unicodeChar);
                Log.e(TAG, "扫码===" + event.getKeyCode() + "   " + sb.toString());
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
                final int len = sb.length();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (len != sb.length()) return;
                        Log.e(TAG, "isQRcode");
                        if (sb.length() > 0) {
                            if (payDialog.isVisible()) {
                                Log.e(TAG, "支付中");
                            } else {
                                addDrink(sb.toString());
                            }
                            sb.setLength(0);
                        }
                    }
                }, 200);
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void addDrink(String code) {
        code = code.replaceAll("[^0-9a-zA-Z]", "");

        if (goodsCode.getGood().containsKey(code)) {
            GvBeans gvBeans = goodsCode.getGood().get(code);
            MenusBean bean = new MenusBean();
            bean.setId("" + (menus.size() + 1));
            bean.setMoney(gvBeans.getPrice());
            bean.setName(gvBeans.getName());
            menus.add(bean);
        } else {
            return;
        }

        float price = 0.00f;
        for (MenusBean bean1 : menus) {
            price = price + Float.parseFloat(bean1.getMoney().substring(1));
        }
        tvPrice.setText(ResourcesUtils.getString(this, R.string.units_money_units) + decimalFormat.format(price));
        menusAdapter.update(menus);

        buildMenuJson(menus, decimalFormat.format(price));

        if (isVertical) {
            if (menus.size() > 0 && !bottomSheetLayout.isSheetShowing()) {
                if (!bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.showWithSheetView(createBottomSheetView());
                }
            }
        }
    }

    //待机
    private void standByTime() {
        if (videoMenuDisplay != null && !videoMenuDisplay.isShow && textDisplay.state == 2) {
            if (videoDisplay != null) {
                videoDisplay.show();
            }
        }
    }


    private void playSound(final int payMode) {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(1, 1, 1, 10, 0, 1);
                if (payMode == 2) {
                    soundPool.play(2, 1, 1, 10, 0, 1);
                }
            }
        }, 200);
    }

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private View createBottomSheetView() {
        View bottomSheet = LayoutInflater.from(this).inflate(R.layout.sheet_layout, bottomSheetLayout, false);
        lvMenus = bottomSheet.findViewById(R.id.lv_menus);
        TextView tvPrice = bottomSheet.findViewById(R.id.main_tv_price);
        TextView btnClear = bottomSheet.findViewById(R.id.main_btn_clear);
        btnClear.setOnClickListener(this);
        menusAdapter = new MenusAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
        lvMenus.setSelection(menusAdapter.getCount() - 1);
        tvPrice.setText(tvCarMoeny.getText().toString());
        return bottomSheet;
    }


    @Override
    protected void onDestroy() {
        soundPool.release();
        if (woyouService != null || extPrinterService != null) {
            unbindService(connService);
        }

        try {
            mScaleManager.cancelGetData();
            mScaleManager.onDestroy();
            mScaleManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
        BasePresentationHelper.getInstance().dismissAll();
        super.onDestroy();
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainActivity.this, ResourcesUtils.getString(this, R.string.tips_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
