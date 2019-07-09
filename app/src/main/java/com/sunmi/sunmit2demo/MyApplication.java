package com.sunmi.sunmit2demo;

import android.app.Activity;
import android.app.Application;
import android.content.res.AssetManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.sunmi.payment.PaymentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by highsixty on 2017/11/20.
 * mail  gaolulin@sunmi.com
 */

public class MyApplication extends Application {

    public static MyApplication app = null;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        PaymentService.getInstance().init(this);
//        CrashReport.initCrashReport(getApplicationContext(), "af2f441e4c", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initAssets();
            }
        }).start();

    }

    public static MyApplication getInstance() {
        return app;
    }

    private void initAssets() {
        Log.d("TAG", "initAssets: ------------->");
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            String fileNames[] = assetManager.list("custom_resource");
            boolean isHaveCamera = isHaveCamera();
            String rootPath = Environment.getExternalStorageDirectory().getPath();
            for (int i = 0; i < fileNames.length; i++) {
                File file = new File(rootPath + "/" + fileNames[i]);
//                if (fileNames[i].contains("smile") && InstallApkUtils.checkApkExist(this,InstallApkUtils.smilePkgName) &&
//                        InstallApkUtils.getVersionCode(this, InstallApkUtils.smilePkgName) < InstallApkUtils.smileVersion) {
//
//                }else {

                if (file.exists()) {
                    Log.d("TAG", "initAssets: -------->文件存在");
                    continue;
                }
//                if (!isHaveCamera && fileNames[i].contains("smile")) {
//                    Log.d("TAG", "initAssets: -------->无摄像头时，不拷贝smile");
//                    continue;
//                }
//                }
                Log.d("TAG", "initAssets: -------->文件不存在");
                inputStream = getClass().getClassLoader().getResourceAsStream("assets/custom_resource/" + fileNames[i]);
                fos = new FileOutputStream(new File(rootPath + "/" + fileNames[i]));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                inputStream.close();
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean isHaveCamera() {
        HashMap<String, UsbDevice> deviceHashMap = ((UsbManager) getSystemService(Activity.USB_SERVICE)).getDeviceList();
        for (Map.Entry entry : deviceHashMap.entrySet()) {
            UsbDevice usbDevice = (UsbDevice) entry.getValue();
            if (!TextUtils.isEmpty(usbDevice.getInterface(0).getName()) && usbDevice.getInterface(0).getName().contains("Orb")) {
                return true;
            }
            if (!TextUtils.isEmpty(usbDevice.getInterface(0).getName()) && usbDevice.getInterface(0).getName().contains("Astra")) {
                return true;
            }
        }
        return false;
    }
}
