package com.sunmi.sunmit2demo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.sunmi.sunmit2demo.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.SF;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.callback.ISendFilesCallback;
import sunmi.ds.callback.QueryCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DataPacket;

/**
 * Created by highsixty on 2018/3/27.
 * mail  gaolulin@sunmi.com
 * 双屏通讯帮助类
 */

public class CommunicateManager {

    private final String TAG = "SUNMI";

    private Context mContext;
    private DSKernel mDSKernel;
    private ProgressDialog dialog;

    private String[] prices = {"10.00", "12.00", "13.00", "10.00", "15.00", "16.00", "10.00", "18.00", "18.00", "10.00"};
    private String[] products = {"招牌奶茶", "鸳鸯奶茶", "卡布奇诺", "金桔果汁", "木瓜果汁", "双皮奶", "拿铁", "柠檬果汁", "苹果汁", "西瓜汁"};
    public static CommunicateManager manager = null;
    private SendAndShowCallBack sendAndShowCallBack;
    private String msgSend = "";
    private String msgShow = "";
    private String packageName=DSKernel.getDSDPackageName();
    public static CommunicateManager getInstance() {
        if (null == manager) {
            synchronized (CommunicateManager.class) {
                if (null == manager) {
                    manager = new CommunicateManager();
                }
            }
        }
        return manager;
    }

    public void init(Context context, DSKernel dsKernel) {
        this.mContext = context;
        this.mDSKernel = dsKernel;
        dialog = new ProgressDialog(context);
        packageName=DSKernel.getDSDPackageName();
    }

    public CommunicateManager registereSendAndShowCallBack(SendAndShowCallBack sendAndShowCallBack) {
        this.sendAndShowCallBack = sendAndShowCallBack;
        return this;
    }

    public void unregistereSendAndShowCallBack() {
        this.sendAndShowCallBack = null;
    }

    /**
     * 传递单个文件
     *
     * @param key  缓存key
     * @param path
     */
    public CommunicateManager checkFileExist(CommunicateType key, String path) {
        checkFileExist(key, path, null);
        return this;
    }

    /**
     * 传递多个文件
     *
     * @param key   缓存key
     * @param paths
     */
    public CommunicateManager checkFileExist(CommunicateType key, List<String> paths) {
        checkFileExist(key, null, paths);
        return this;
    }

    /**
     * 检查副屏指定缓存文件是否存在
     *
     * @param key 缓存key
     */
    public CommunicateManager checkFileExist(final CommunicateType key, final String path, final List<String> paths) {
        final long taskId = (long) SharePreferenceUtil.getParam(mContext, key.getValue(), 0L);
        if (taskId <= 0) {
            switch (key.getId()) {
                case 0: //检查单张图片文件是否存在
                    sendPicture(path);
                    break;
                case 1://一边清单一边幻灯片
                    sendImgsMenu(paths);
                    break;
                case 2: //多视频文件
                    sendVideos(1, paths);
                    break;
                case 3: //多视频文件加清单文件
                    sendVideos(2, paths);
                    break;
                case 4: //单个视频文件
                    sendVideo(1, path);
                    break;
                case 5: //单个视频文件加清单文件
                    sendVideo(2, path);
                    break;
                case 6://图片轮播
                    sendImgs(1, paths, 0);
                    break;
                case 7://一张图加清单
                    sendPictureAndMenu(path);
                    break;
                case 8://图片轮播加清单
                    sendImgs(2, paths, 0);
                    break;
                default:
                    break;
            }
            msgSend = "";
            return this;
        }

        checkFileExist(taskId, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                //检查缓存文件失败
                Log.d(TAG, "onCheckFail: ------------>file not exist");
                switch (key.getId()) {
                    case 0: //检查单张图片文件是否存在
                        sendPicture(path);
                        break;
                    case 1://一边清单一边幻灯片
                        sendImgsMenu(paths);
                        break;
                    case 2: //多视频文件
                        sendVideos(1, paths);
                        break;
                    case 3: //多视频文件加清单文件
                        sendVideos(2, paths);
                        break;
                    case 4: //单个视频文件
                        sendVideo(1, path);
                        break;
                    case 5: //单个视频文件加清单文件
                        sendVideo(2, path);
                        break;
                    case 6://图片轮播
                        sendImgs(1, paths, 0);
                        break;
                    case 7://一张图加清单
                        sendPictureAndMenu(path);
                        break;
                    case 8://图片轮播加清单
                        sendImgs(2, paths, 0);
                        break;
                    default:
                        break;
                }
                msgSend = "";
            }

            @Override
            public void onResult(boolean exist) {
                Log.d(TAG, "检查类型:" + key.getId() + "结果是" + exist+"taskId"+taskId);
                if (exist) {
                    //缓存文件存在
                    dismissDialog();
                    switch (key.getId()) {
                        case 0: //检查单张图片文件是否存在
                            showPicture(taskId);
                            break;
                        case 1://一边清单一边幻灯片
                            showImgsMenu(taskId);
                            break;
                        case 2: //多视频文件
                            playvideos(taskId);
                            break;
                        case 3: //多视频文件加清单文件
                            playMenuVideos(taskId);
                            break;
                        case 4: //单个视频文件
                            playvideo(taskId);
                            break;
                        case 5: //单个视频文件加清单文件
                            playVideoMenu(taskId);
                            break;
                        case 6://图片轮播
                            showImgs(taskId);
                            break;
                        case 7://一张图加清单
                            showPicture(taskId);
                            break;
                        case 8://图片轮播加清单
                            showImgMenu(taskId);
                            break;
                        default:
                            break;
                    }
                    msgShow = "";
                } else {
                    //缓存文件不存在
                    Log.d(TAG, "检查类型:" + key.getId() + "结果是不存在");
                    switch (key.getId()) {
                        case 0: //检查单张图片文件是否存在
                            sendPicture(path);
                            break;
                        case 1://一边清单一边幻灯片
                            sendImgsMenu(paths);
                            break;
                        case 2: //多视频文件
                            sendVideos(1, paths);
                            break;
                        case 3: //多视频文件加清单文件
                            sendVideos(2, paths);
                            break;
                        case 4: //单个视频文件
                            sendVideo(1, path);
                            break;
                        case 5: //单个视频文件加清单文件
                            sendVideo(2, path);
                            break;
                        case 6://图片轮播
                            sendImgs(1, paths, 0);
                            break;
                        case 7://一张图加清单
                            sendPictureAndMenu(path);
                            break;
                        case 8://图片轮播加清单
                            sendImgs(2, paths, 0);
                            break;
                        default:
                            break;
                    }
                    msgSend = "";
                }
            }
        });
        return this;
    }


    private void checkFileExist(long fileId, final ICheckFileCallback mICheckFileCallback) {
        DataPacket packet = new DataPacket.Builder(DSData.DataType.CHECK_FILE).data("def").
                recPackName(packageName).addCallback(new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {

            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (mICheckFileCallback != null) {
                    mICheckFileCallback.onCheckFail();
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        }).isReport(true).build();
        packet.getData().fileId = fileId;
        mDSKernel.sendQuery(packet, new QueryCallback() {
            @Override
            public void onReceiveData(DSData data) {
                boolean exist = TextUtils.equals("true", data.data);
                if (mICheckFileCallback != null) {
                    mICheckFileCallback.onResult(exist);
                }
            }
        });
    }

    /**
     * 设置发送的清单文件
     *
     * @param msg
     */
    public CommunicateManager setMsgSend(String msg) {
        this.msgSend = msg;
        return this;
    }

    /**
     * 设置显示的清单文件
     *
     * @param msg
     */
    public CommunicateManager setMsgShow(String msg) {
        this.msgShow = msg;
        return this;
    }

    /**
     * 发送轮播图片
     */
    private void sendImgs(final int childType, List<String> imgs, int time) {
        showDialog("正在发送轮播图");
        JSONObject json = new JSONObject();
        try {
            //轮播图切换时间
            if (time > 0) {
                json.put("rotation_time", time);
            } else {
                json.put("rotation_time", 2000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(msgSend)) {
            msgSend = json.toString();
        }
        mDSKernel.sendFiles(packageName, msgSend, imgs, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long l) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(l);
                }
                dismissDialog();
                switch (childType) {
                    case 1:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.imgsKey.getValue(), l);
                        showImgs(l);
                        break;
                    case 2:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.imgsMenuKey.getValue(), l);
                        showImgsMenu(l);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSendSuccess(final String path, final long fileId) {
                Log.d(TAG, "onSendSuccess: ----------->");
            }

            @Override
            public void onSendFaile(final int errorId, final String s) {
                Log.d(TAG, "onSendFaile: ------------->" + s);
                dismissDialog();
                showToast("发送轮播图片失败---->" + s);
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, s);
                }
            }

            @Override
            public void onSendFileFaile(String s, int i, String s1) {
                Log.d(TAG, "onSendFileFaile: --------------->" + s + "  " + s1);
                dismissDialog();
                showToast("发送轮播图片失败---->" + s + "  " + s1);
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(s, i, s1);
                }
            }

            @Override
            public void onSendProcess(final String s, final long l, final long l1) {
                Log.d(TAG, "onSendProcess: ----------->" + s + "  " + l + "  " + l1);
            }
        });
    }

    /**
     * 展示轮播图片
     */
    private void showImgs(long taskId) {
        String json = UPacketFactory.createJson(DataModel.IMAGES, "");
        mDSKernel.sendCMD(packageName, json, taskId, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * @param childType 1 单个视频文件 2 单个视频文件加清单文件
     */
    private void sendVideo(final int childType, String path) {
        showDialog("正在发送单个视频文件");
        mDSKernel.sendFile(packageName, path, new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(l);
                }
                switch (childType) {
                    case 1:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.videoKey.getValue(), l);
                        playvideo(l);
                        break;
                    case 2:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.videoMenusKey.getValue(), l);
                        playVideoMenu(l);
                        break;
                    default:
                        break;
                }
                dismissDialog();
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                Log.d(TAG, "发送单个文件视频文件失败 ------------>" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(final long l, final long l1) {
            }
        });
    }

    /**
     * 播放单个视频文件加清单文件
     *
     * @param fileId
     */
    private void playVideoMenu(long fileId) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "商米奶茶店收银");
            JSONObject head = new JSONObject();
            head.put("param1", "序号");
            head.put("param2", "商品名");
            head.put("param3", "单价");
            data.put("head", head);
            data.put("flag", "true");
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products[i - 1]);
                listItem.put("param3", prices[i - 1]);
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", "总计 ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", "优惠 ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", "数量 ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", "应收 ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            String json = UPacketFactory.createJson(DataModel.SHOW_VIDEO_LIST, data.toString());
            if (TextUtils.isEmpty(msgShow)) {
                msgShow = json.toString();
            }
            mDSKernel.sendCMD(packageName, msgShow, fileId, new ISendCallback() {
                @Override
                public void onSendSuccess(long taskId) {
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onShowSuccess(taskId);
                    }
                }

                @Override
                public void onSendFail(int errorId, String errorInfo) {
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onShowFail(errorId, errorInfo);
                    }
                }

                @Override
                public void onSendProcess(long totle, long sended) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 播放单个视频文件
     *
     * @param taskID
     */
    private void playvideo(long taskID) {
        String json = UPacketFactory.createJson(DataModel.VIDEO, "true");
        mDSKernel.sendCMD(packageName, json, taskID, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 发送多视频文件
     */
    private void sendVideos(final int chlidType, List<String> files) {
        showDialog("正在发送多个视频文件");
        //请对文件是否存在做判断
        mDSKernel.sendFiles(packageName, "", files, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long fileid) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(fileid);
                }
                Log.d(TAG, "onAllSendSuccess: ----------->" + fileid);
                dismissDialog();
                switch (chlidType) {
                    case 1:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.videosKey.getValue(), fileid);
                        playvideos(fileid);
                        break;
                    case 2:
                        SharePreferenceUtil.setParam(mContext, CommunicateType.videosMenusKey.getValue(), fileid);
                        playMenuVideos(fileid);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSendSuccess(String path, long taskId) {
                Log.d(TAG, "onSendSuccess: --------------->");
            }

            @Override
            public void onSendFaile(int errorId, String errorInfo) {
                Log.d(TAG, "onSendFaile: --------------->");
                showToast("发送多视频文件失败------>" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                }
            }

            @Override
            public void onSendFileFaile(String path, int errorId, String errorInfo) {
                showToast("发送多视频文件失败------>" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(path, errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(String path, long totle, long sended) {

            }
        });
    }

    /**
     * 播放多视频文件
     *
     * @param taskID
     */
    private void playvideos(long taskID) {
        String json = UPacketFactory.createJson(DataModel.VIDEOS, "true");
        mDSKernel.sendCMD(packageName, json, taskID, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 播放多视频文件加清单文件
     *
     * @param taskID
     */
    private void playMenuVideos(long taskID) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "商米奶茶店收银");
            JSONObject head = new JSONObject();
            head.put("param1", "序号");
            head.put("param2", "商品名");
            head.put("param3", "单价");
            data.put("head", head);
            data.put("flag", "true");
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products[i - 1]);
                listItem.put("param3", prices[i - 1]);
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", "总计 ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", "优惠 ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", "数量 ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", "应收 ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            String json = UPacketFactory.createJson(DataModel.MENUVIDEOS, data.toString());
            if (TextUtils.isEmpty(msgShow)) {
                msgShow = json;
            }
            mDSKernel.sendCMD(packageName, msgShow, taskID, new ISendCallback() {
                @Override
                public void onSendSuccess(long taskId) {
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onShowSuccess(taskId);
                    }
                }

                @Override
                public void onSendFail(int errorId, String errorInfo) {
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onShowFail(errorId, errorInfo);
                    }
                }

                @Override
                public void onSendProcess(long totle, long sended) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送单张图片加清单
     */
    private void sendPictureAndMenu(String path) {

        mDSKernel.sendFile(packageName, path, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(taskId);
                }
                dismissDialog();
                SharePreferenceUtil.setParam(mContext, CommunicateType.imgMenuKey.getValue(), taskId);
                showImgMenu(taskId);
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                Log.d("TAG", "onSendFail: -------------------->" + errorId + "  " + errorInfo);
                showToast("发送单张图片失败" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {
                Log.d(TAG, "sendPicture: --------->" + totle + "  " + sended);
            }
        });
    }

    /**
     * 显示单张图片加清单
     */
    private void showImgMenu(long taskId) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "商米奶茶店收银");
            JSONObject head = new JSONObject();
            head.put("param1", "序号");
            head.put("param2", "商品名");
            head.put("param3", "单价");
            data.put("head", head);
            data.put("flag", "true");
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products[i - 1]);
                listItem.put("param3", prices[i - 1]);
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", "总计 ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", "优惠 ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", "数量 ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", "应收 ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            String json = UPacketFactory.createJson(DataModel.SHOW_IMG_LIST, data.toString());
            if (TextUtils.isEmpty(msgShow)) {
                msgShow = json;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mDSKernel.sendCMD(packageName, msgShow, taskId, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 发送单张图片
     */
    private void sendPicture(String path) {
        showDialog("正在发送单张图片");
        mDSKernel.sendFile(packageName, path, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(taskId);
                }
                dismissDialog();
                SharePreferenceUtil.setParam(mContext, CommunicateType.imgKey.getValue(), taskId);
                showPicture(taskId);
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                Log.d("TAG", "onSendFail: -------------------->" + errorId + "  " + errorInfo);
                showToast("发送单张图片失败" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {
                Log.d(TAG, "sendPicture: --------->" + totle + "  " + sended);
            }
        });
    }

    /**
     * 副屏展示单张图片
     *
     * @param taskId
     */
    private void showPicture(long taskId) {
        //显示图片
        String json1 = UPacketFactory.createJson(DataModel.SHOW_IMG_WELCOME, "default");
        mDSKernel.sendCMD(packageName, json1, taskId, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                Log.e(TAG,"发送图片成功"+taskId);
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                Log.e(TAG,"发送图片失败"+errorId);
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 发送清单轮播图资源
     */
    private void sendImgsMenu(List<String> imgs) {
        showDialog("正在发送清单轮播图");
        JSONObject json = new JSONObject();
        try {
            //轮播图切换时间
            json.put("rotation_time", 2000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(msgSend)) {
            msgSend = json.toString();
        }
        mDSKernel.sendFiles(packageName, msgSend, imgs, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long fileId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(fileId);
                }
                dismissDialog();
                showImgsMenu(fileId);
                SharePreferenceUtil.setParam(mContext, CommunicateType.PHPMenusKey.getValue(), fileId);
            }

            @Override
            public void onSendSuccess(String path, long taskId) {
            }

            @Override
            public void onSendFaile(int errorId, String errorInfo) {
                showToast("发送轮播图失败---->" + errorInfo);
                dismissDialog();
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                }
            }

            @Override
            public void onSendFileFaile(String path, int errorId, String errorInfo) {
                dismissDialog();
                showToast("发送轮播图失败---->" + errorInfo);
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendFail(path, errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(String path, long totle, long sended) {
            }
        });
    }

    private void showImgsMenu(long taskId) {
        final JSONObject data = new JSONObject();
        try {

            data.put("title", "商米奶茶店收银");
            JSONObject head = new JSONObject();
            head.put("param1", "序号");
            head.put("param2", "商品名");
            head.put("param3", "单价");
            data.put("head", head);
            data.put("alternateTime", 1000);
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products[i - 1]);
                listItem.put("param3", prices[i - 1]);
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", "总计 ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", "优惠 ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", "数量 ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", "应收 ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json1 = UPacketFactory.createJson(DataModel.SHOW_IMGS_LIST, data.toString());
        if (TextUtils.isEmpty(msgShow)) {
            msgShow = json1;
        }
        mDSKernel.sendCMD(packageName, msgShow, taskId, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowSuccess(taskId);
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onShowFail(errorId, errorInfo);
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 展示清单
     */
    public void showMenus() {
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("title", "商米奶茶店收银");
            JSONObject head = new JSONObject();
            head.put("param1", "序号");
            head.put("param2", "商品名");
            head.put("param3", "单价");
            data.put("head", head);
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products[i - 1]);
                listItem.put("param3", prices[i - 1]);
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", "总计 ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", "优惠 ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", "数量 ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", "应收 ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            json.put("data", data.toString());
            json.put("dataModel", "TEXT");
            JSONObject j = new JSONObject();
            j.put("data", json.toString());
            j.put("dataType", "DATA");
            if (TextUtils.isEmpty(msgShow)) {
                msgShow = j.toString();
            }
            mDSKernel.TEST(msgShow);
            msgShow="";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示欢迎
     */
    public void showWelcome() {
            DataPacket packet1 = UPacketFactory.buildShowText(packageName,"hello world",null);
            mDSKernel.sendData(packet1);
    }

    /**
     * 显示微信支付
     */
    public void showCodePay(String path) {
        showDialog("正在发送微信二维码");
        JSONObject json = new JSONObject();
        try {
            json.put("title", "微信支付");
            json.put("content", "10.00");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(msgSend)) {
            msgSend = json.toString();
        }
        if (TextUtils.isEmpty(path)) {
            path = Environment.getExternalStorageDirectory().getPath() + "/qrcode.png";
        }
        mDSKernel.sendFile(packageName, msgSend, path, new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                if (sendAndShowCallBack != null) {
                    sendAndShowCallBack.onSendSuccess(l);
                }
                dismissDialog();
                //显示图片
                try {
                    JSONObject json = new JSONObject();
                    json.put("dataModel", "QRCODE");
                    json.put("data", "default");
                    if (TextUtils.isEmpty(msgShow)) {
                        msgShow = json.toString();
                    }
                    mDSKernel.sendCMD(packageName, msgShow, l, new ISendCallback() {
                        @Override
                        public void onSendSuccess(long taskId) {
                            if (sendAndShowCallBack != null)
                                sendAndShowCallBack.onShowSuccess(taskId);
                        }

                        @Override
                        public void onSendFail(int errorId, String errorInfo) {
                            if (sendAndShowCallBack != null)
                                sendAndShowCallBack.onShowFail(errorId, errorInfo);
                        }

                        @Override
                        public void onSendProcess(long totle, long sended) {

                        }
                    });
                    msgShow="";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSendFail(int i, String s) {
                showToast("发送二维码图片失败---->" + s);
                dismissDialog();
                if (sendAndShowCallBack != null)
                    sendAndShowCallBack.onSendFail(null, i, s);
            }

            @Override
            public void onSendProcess(long l, long l1) {

            }
        });
        msgSend = "";
    }

    /**
     * 获取副屏缓存文件大小
     */
    public void getSubCacheSize() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dataModel", "GETVICECACHEFILESIZE");
            jsonObject.put("data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/" + mContext.getPackageName().replace(".", "_"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket packet = new DataPacket.Builder(DSData.DataType.CMD).recPackName(packageName).data(jsonObject.toString())
                .addCallback(null).build();
        mDSKernel.sendQuery(packet, new QueryCallback() {
            @Override
            public void onReceiveData(final DSData data) {
                Log.d(TAG, "onReceiveData: ------------>" + data.data);
                showToast("副屏缓存文件大小字节数为" + data.data);
            }
        });
    }

    /**
     * 清除副屏缓存文件
     */
    public void clearSubCacheSize() {
        try {
            JSONObject clearPath = new JSONObject();
            clearPath.put("dataModel", "CLEAN_FILES");
            clearPath.put("data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/" + mContext.getPackageName().replace(".", "_"));
            mDSKernel.sendCMD(packageName, clearPath.toString(), -1, new ISendCallback() {
                @Override
                public void onSendSuccess(long taskId) {
                    showToast("清除当前应用在副屏缓存文件成功");
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onSendSuccess(taskId);
                    }
                }

                @Override
                public void onSendFail(int errorId, String errorInfo) {
                    showToast("清除当前应用在副屏缓存文件失败");
                    if (sendAndShowCallBack != null) {
                        sendAndShowCallBack.onSendFail(null, errorId, errorInfo);
                    }
                }

                @Override
                public void onSendProcess(long totle, long sended) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取副屏model
     */
    public void getSubModel() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dataModel", "GET_MODEL");
            jsonObject.put("data", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket p = new DataPacket.Builder(DSData.DataType.CMD).recPackName(packageName).data(jsonObject.toString())
                .addCallback(new ISendCallback() {
                    @Override
                    public void onSendSuccess(long taskId) {

                    }

                    @Override
                    public void onSendFail(int errorId, String errorInfo) {

                    }

                    @Override
                    public void onSendProcess(long totle, long sended) {

                    }
                }).build();
        mDSKernel.sendQuery(p, new QueryCallback() {
            @Override
            public void onReceiveData(final DSData data) {
                Log.d(TAG, "onReceiveData: ------------>" + data.data);
                showToast("从副屏获取副屏分辨率-->" + data.data);
            }
        });
    }

    /**
     * 清除副屏指定缓存文件id
     *
     * @param taskId
     */
    public void clearSubCacheTaskId(long taskId, ICheckFileCallback iCheckFileCallback) {

        mDSKernel.deleteFileExist(taskId, iCheckFileCallback);
    }

    public void sendCMD(String cmd, ISendCallback iSendCallback) {
        mDSKernel.sendCMD(packageName, cmd, -1, iSendCallback);
    }

    public void sendQuery(DataPacket dataPacket, QueryCallback queryCallback) {
        mDSKernel.sendQuery(dataPacket, queryCallback);
    }

    /**
     * 打开副屏设置应用
     */
    public void openSubSetting() {
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("dataModel", "OPEN_APP");
            jsonObject1.put("data", "com.android.settings");
            mDSKernel.sendCMD(packageName, jsonObject1.toString(), -1, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示等待层
     *
     * @param title
     */
    private synchronized void showDialog(String title) {
        Log.d(TAG, "showDialog: ----------------->");
        if (dialog != null && !dialog.isShowing()) {
            dialog.setTitle(title);
            dialog.show();
        }
    }

    /**
     * 关闭等待层
     */
    private synchronized void dismissDialog() {
        Log.d(TAG, "dismissDialog: ------------->");
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void showToast(final String message) {
        ((BaseActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface SendAndShowCallBack {
        void onSendSuccess(long taskId);

        void onSendFail(String path, int errorId, String errorInfo);

        void onShowSuccess(long taskId);

        void onShowFail(int errorId, String errorInfo);
    }

    public enum CommunicateType {
        imgKey(0, "IMGKEY", "单张图片缓存key"),//单张图片缓存key
        PHPMenusKey(1, "IMGSMENUSKEY", "一边清单一边幻灯片"),//一边清单一边幻灯片
        videosKey(2, "VIDEOSKEY", "多视频文件"),//多视频文件
        videosMenusKey(3, "VIDEOSMENUSKEY", "多视频文件加清单"),//多视频文件加清单
        videoKey(4, "VIDEOKEY", "播放单个视频文件"),//播放单个视频文件
        videoMenusKey(5, "VIDEOMENUKEY", "播放单个视频文件加清单"),//播放单个视频文件加清单
        imgsKey(6, "IMGSKEY", "图片轮播"),//图片轮播
        imgMenuKey(7, "IMGMENUKEY", "一张图加清单"),//一张图加清单
        imgsMenuKey(8, "IMGSMENUKEY", "图片轮播加清单");//图片轮播加清单
        private int id;
        private String value;
        private String directions;

        CommunicateType(int id, String value, String directions) {
            this.value = value;
            this.id = id;
            this.directions = directions;
        }

        public int getId() {
            return this.id;
        }

        public String getValue() {
            return this.value;
        }

        public String getDirections() {
            return this.directions;
        }
    }
}
