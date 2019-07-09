package com.sunmi.sunmit2demo.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.bean.MenuBean;
import com.sunmi.sunmit2demo.model.AlipaySmileModel;
import com.sunmi.sunmit2demo.utils.BitmapUtils;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class PrinterPresenter {
    private Context context;
    private static final String TAG = "PrinterPresenter";
    private IWoyouService printerService;

    public PrinterPresenter(Context context, IWoyouService printerService) {
        this.context = context;
        this.printerService = printerService;
    }

    public void print(final String json, final int payMode) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MenuBean menuBean = JSON.parseObject(json, MenuBean.class);
                int fontsizeTitle = 40;
                int fontsizeContent = 30;
                int fontsizeFoot = 35;
                String divide = "**************************************" + "\n";
                String divide2 = "-------------------------------------" + "\n";
                int width = divide2.length() * 5 / 12;
                String goods = formatTitle(width);
                try {
                    if (printerService.updatePrinterState() != 1) {
                        return;
                    }
                    printerService.setAlignment(1, null);
                    printerService.sendRAWData(boldOn(), null);
                    printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.menus_title) + "\n" + ResourcesUtils.getString(context, R.string.print_proofs) + "\n", "", fontsizeTitle, null);
                    printerService.setAlignment(0, null);
                    printerService.sendRAWData(boldOff(), null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_order_number) + SystemClock.uptimeMillis() + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_order_time) + formatData(new Date()) + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_payment_method), "", fontsizeContent, null);
                    if (payMode == 0) {
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.pay_money) + "\n", "", fontsizeContent, null);
                    } else if (payMode == 1) {
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_ali_wx) + "\n", "", fontsizeContent, null);
                    } else if (payMode == 2) {
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.pay_face) + "\n", "", fontsizeContent, null);
                    }
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont(goods + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide2, "", fontsizeContent, null);
                    printGoods(menuBean, fontsizeContent, divide2, payMode, width);

                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.sendRAWData(boldOn(), null);
                    if (payMode == 2) {
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_tips_havemoney), "", fontsizeFoot, null);
                    } else {
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_tips_nomoney), "", fontsizeFoot, null);
                    }
                    printerService.sendRAWData(boldOff(), null);
                    printerService.lineWrap(4, null);

                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.print_logo);
                    if (bitmap.getWidth() > 384) {
                        int newHeight = (int) (1.0 * bitmap.getHeight() * 384 / bitmap.getWidth());
                        bitmap = BitmapUtils.scale(bitmap, 384, newHeight);
                    }
                    printerService.printBitmap(bitmap, null);
                    printerService.printText("\n\n", null);
                    printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_thanks), "", fontsizeContent, null);

                    printerService.lineWrap(4, null);
                    printerService.cutPaper(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String formatTitle(int width) {
        Log.e("@@@@@", width + "=======");

        String[] title = {
                ResourcesUtils.getString(context, R.string.shop_car_goods_name),
                ResourcesUtils.getString(context, R.string.shop_car_number),
                ResourcesUtils.getString(context, R.string.shop_car_unit_money),
        };
        StringBuffer sb = new StringBuffer();
        int blank1 = width - String_length(title[0]);
        int blank2 = width - String_length(title[1]);

        sb.append(title[0]);
        sb.append(addblank(blank1));

        sb.append(title[1]);
        sb.append(addblank(blank2));

        sb.append(title[2]);

//        int w1 = width / 3;
//        int w2 = width / 3 + 2;
//        String str = String.format("%-" + w1 + "s%-" + w2 + "s%s", title[0], title[1], title[2]);
        return sb.toString();
    }

    private void printGoods(MenuBean menuBean, int fontsizeContent, String divide2, int payMode, int width) throws RemoteException {
        int blank1;
        int blank2;
        StringBuffer sb = new StringBuffer();
        for (MenuBean.ListBean listBean : menuBean.getList()) {
            sb.setLength(0);
            blank1 = width - String_length(listBean.getParam2()) + 1;
            blank2 = width - 2;
            sb.append(listBean.getParam2());
            sb.append(addblank(blank1));

            sb.append(1);
            sb.append(addblank(blank2));

            sb.append(listBean.getParam3());
            printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
        }
        printerService.printTextWithFont(divide2, "", fontsizeContent, null);
        String total = ResourcesUtils.getString(context, R.string.print_total_payment);
        String real = ResourcesUtils.getString(context, R.string.print_real_payment);

        sb.setLength(0);
        blank1 = width * 2 - String_length(total) - 1;
        blank2 = width * 2 - String_length(real) - 1;
        sb.append(total);
        sb.append(addblank(blank1));
        sb.append(ResourcesUtils.getString(context, R.string.units_money_units));
        sb.append(menuBean.getKVPList().get(0).getValue());

        printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
        sb.setLength(0);
        sb.append(real);
        sb.append(addblank(blank2));
        sb.append(ResourcesUtils.getString(context, R.string.units_money_units));
        if (payMode == 2) {
            sb.append("0.0" + (AlipaySmileModel.i));
        } else {
            sb.append("0.00");
        }
        printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
        sb.setLength(0);
    }

    private String formatData(Date nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

    private String addblank(int count) {
        String st = "";
        if (count < 0) {
            count = 0;
        }
        for (int i = 0; i < count; i++) {
            st = st + " ";
        }
        return st;
    }

    private static final byte ESC = 0x1B;// Escape

    /**
     * 字体加粗
     */
    private byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消字体加粗
     */
    private byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    private boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private int String_length(String rawString) {
        return rawString.replaceAll("[\\u4e00-\\u9fa5]", "SH").length();
    }
}
