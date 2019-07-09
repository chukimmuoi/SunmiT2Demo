package com.sunmi.sunmit2demo.presentation;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.ListView;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.adapter.MenusAdapter;
import com.sunmi.sunmit2demo.bean.MenusBean;

import java.util.List;

/**
 * Created by highsixty on 2018/3/9.
 * mail  gaolulin@sunmi.com
 */

public class MenusPresentation extends Presentation {

    private ListView lv_muens;
    private MenusAdapter menusAdapter;
    private List<MenusBean> mMenusBeans;
    private TextView tv;

    public MenusPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    public MenusPresentation(Context outerContext, Display display, List<MenusBean> menusBeans) {
        super(outerContext, display);
        this.mMenusBeans = menusBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus_presentation_layout);
        lv_muens = (ListView) this.findViewById(R.id.lv_menus);
        menusAdapter = new MenusAdapter(this.getContext(), mMenusBeans);
        lv_muens.setAdapter(menusAdapter);
        tv = (TextView) findViewById(R.id.tv);
    }

    public void update() {
        tv.setText("22222222222");
    }
}
