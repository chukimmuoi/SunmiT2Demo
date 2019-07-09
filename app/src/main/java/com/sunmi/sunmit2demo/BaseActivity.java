package com.sunmi.sunmit2demo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    private final int container = R.id.container;

    private ArrayList<BaseFragment> fragments;// back fragment list.
    private BaseFragment fragment;// current fragment.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS //
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);

    }

    /**
     * replace the current fragment.
     *
     * @param fragment       the new fragment to shown.
     * @param addToBackStack if it can back.
     */
    public void addContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }


        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    // use replace method to show fragment.
    public void replaceContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    public void backTopFragment() {
        if (fragments != null && fragments.size() > 1) {
            removeContent();
            backTopFragment();
        }
    }

    /**
     * set current fragment.
     */
    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    /**
     * get the current fragment.
     *
     * @return current fragment
     */
    public BaseFragment getFirstFragment() {
        return fragment;
    }

    /**
     * get amount of fragment.
     *
     * @return amount of fragment
     */
    public int getFragmentNum() {
        return fragments != null ? fragments.size() : 0;
    }

    /**
     * clear fragment list
     */
    protected void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }

    /**
     * remove previous fragment
     */
    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }

    /**
     * init fragment list.
     */
    private void initFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    /**
     * remove current fragment and back to front fragment.
     */
    public void removeContent() {
        removePrevious();
        setFragment();

        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * remove all fragment from back stack.
     */
    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * 显示长时间Toast
     *
     * @param msg
     */
    public void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时间Toast
     *
     * @param resId
     */
    public void showLongToast(@StringRes int resId) {
        showLongToast(getResources().getString(resId));
    }

    /**
     * 显示长时间Toast
     *
     * @param msg
     */
    public void showLongToast(String msg, int code) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    public void showToast(int code, String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param resId
     */
    public void showToast(@StringRes int resId) {
        showToast(getResources().getString(resId));
    }

    /**
     * 显示Toast
     *
     * @param resId
     */
    public void showToast(@StringRes int resId, int code) {
        showToast(code, getResources().getString(resId));
    }
    private void showToast(String msg, int duration) {
        Toast.makeText(this,msg,duration).show();
    }
}
