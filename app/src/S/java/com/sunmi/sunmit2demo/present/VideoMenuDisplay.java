package com.sunmi.sunmit2demo.present;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunmi.sunmit2demo.BasePresentation;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.adapter.MenusAdapter;
import com.sunmi.sunmit2demo.adapter.PresentMenusAdapter;
import com.sunmi.sunmit2demo.bean.MenusBean;
import com.sunmi.sunmit2demo.player.IMPlayListener;
import com.sunmi.sunmit2demo.player.IMPlayer;
import com.sunmi.sunmit2demo.player.MPlayer;
import com.sunmi.sunmit2demo.player.MPlayerException;
import com.sunmi.sunmit2demo.player.MinimalDisplay;
import com.sunmi.sunmit2demo.ui.MainActivity;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;
import com.sunmi.sunmit2demo.utils.ScreenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by highsixty on 2018/3/7.
 * mail  gaolulin@sunmi.com
 */

public class VideoMenuDisplay extends BasePresentation {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private final String TAG = "SUNMI";
    private String path;
    private LinearLayout container;

    private LinearLayout llyRight;
    private TextView mTitle;
    private ListView mLv;
    private PresentMenusAdapter menusAdapter;
    private List<MenusBean> menus = new ArrayList<>();


    private TextView tvMenusLeft1;
    private TextView tvMenusRight1;
    private TextView tvMenusLeft2;
    private TextView tvMenusRight2;

    public VideoMenuDisplay(Context context, Display display, String path) {
        super(context, display);
        this.path = path;
        Log.d(TAG, "VideoDisplay: ------------>" + path);
        File file = new File(path);
        Log.d(TAG, "VideoDisplay: --------->" + file.exists());
    }

    public void update(List<MenusBean> menus, String json) {
        this.menus.clear();
        this.menus.addAll(menus);
        initData(json);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ScreenManager.getInstance().isMinScreen()) {
            setContentView(R.layout.vice_video_menu_min_layout);

        } else {
            setContentView(R.layout.vice_video_menu_layout);
        }
        initView();
        initPlayer();


    }

    private void initView() {
        mPlayerView = (SurfaceView) findViewById(R.id.mPlayerView);
        container = (LinearLayout) findViewById(R.id.playerContainer);
        llyRight = (LinearLayout) findViewById(R.id.lly_right);
        mTitle = (TextView) findViewById(R.id.title);
        mLv = (ListView) findViewById(R.id.lv);
        menusAdapter = new PresentMenusAdapter(getContext(), menus);
        mLv.setAdapter(menusAdapter);


        tvMenusLeft1 = (TextView) findViewById(R.id.tv_menus_left1);
        tvMenusRight1 = (TextView) findViewById(R.id.tv_menus_right1);
        tvMenusLeft2 = (TextView) findViewById(R.id.tv_menus_left2);
        tvMenusRight2 = (TextView) findViewById(R.id.tv_menus_right2);

    }

    private void initData(String jsonStr) {
        Log.d(TAG, "initData: ----------->" + jsonStr);
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject head = json.getJSONObject("head");
            setHeadview(head);
            JSONArray lists = json.getJSONArray("list");
            setlistView(lists);
            JSONArray statement = json.getJSONArray("KVPList");
            setSMView(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
        player.setPlayListener(new IMPlayListener() {
            @Override
            public void onStart(IMPlayer player) {

            }

            @Override
            public void onPause(IMPlayer player) {

            }

            @Override
            public void onResume(IMPlayer player) {

            }

            @Override
            public void onComplete(IMPlayer player) {
                try {
                    player.setSource(path, 0);
                } catch (MPlayerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setHeadview(JSONObject json) {
        List<String> valueLists = getValueListByJsonObject(json);
    }

    /**
     * 动态解析jsonObject获取值列表
     *
     * @param json
     * @return
     */
    private ArrayList<String> getValueListByJsonObject(JSONObject json) {
        ArrayList<String> valueLists = new ArrayList<String>();
        try {
            Iterator it = json.keys();
            while (it.hasNext()) {
                String value = json.getString(it.next().toString());
                valueLists.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueLists;
    }

    /**
     * 设置表内容
     *
     * @param jsonArray
     */
    private void setlistView(JSONArray jsonArray) {
        if (menusAdapter != null) {
            menusAdapter.notifyDataSetChanged();
            mLv.setSelection(menusAdapter.getCount() - 1);
            mLv.smoothScrollToPosition(menusAdapter.getCount() - 1);
        }
    }

    /**
     * 设置结算
     */
    private void setSMView(JSONArray KVPList) {

        for (int i = 0; i < KVPList.length(); i++) {
            try {
                JSONObject json = KVPList.getJSONObject(i);

                switch (i) {
                    case 0:
                        tvMenusRight1.setText(json.getString("name") + ":    " + ResourcesUtils.getString(R.string.units_money_units) + json.getString("value"));
                        break;
                    case 1:
                        break;
                    case 2:
                        tvMenusLeft1.setText(json.getString("name") + ":    " + json.getString("value"));
                        break;
                    case 3:
                        tvMenusLeft2.setText(json.getString("name") + ":    ");
                        SpannableString ss1 = new SpannableString(ResourcesUtils.getString(R.string.units_money_units) + json.getString("value"));
                        ss1.setSpan(new RelativeSizeSpan(0.65f), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // set size
                        tvMenusRight2.setText(ss1);

                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onSelect(boolean isShow) {
        if(player != null){
            if (isShow) {
                try {
                    player.setSource(path, VideoDisplay.positon);
                    player.onResume();
                } catch (MPlayerException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onCreate: ---------->" + e.getMessage());
                }
            }else {
                if(player.getPosition()!=0)
                    VideoDisplay.positon = player.getPosition();
            }
        }
    }

    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
//        player.onDestroy();
    }
}

