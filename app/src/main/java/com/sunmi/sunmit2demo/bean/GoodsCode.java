package com.sunmi.sunmit2demo.bean;

import com.sunmi.sunmit2demo.MyApplication;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsCode {
    String[] code ={
            "6901939621257","6928804014686","6925303721367","6921581596048",//drinks

            "6948939635686","6948939611543","4895058313549","4895058313532",//snacks

            "1","2","3","4",
            "5","6","7","8",//fruits

            "9","10","11","12",//vegetables

            "6928804011142","6902827110013","6920202888883"//others
    };
    int[] icon ={
            R.drawable.goods_1,R.drawable.goods_2,R.drawable.goods_3,R.drawable.goods_4,//drinks

            R.drawable.goods_5,R.drawable.goods_6,R.drawable.goods_7,R.drawable.goods_8,//snacks

            R.drawable.apple,R.drawable.pears,R.drawable.banana,R.drawable.pitaya,
            R.drawable.goods_sc_1,R.drawable.goods_sc_2,R.drawable.goods_sc_3,R.drawable.goods_sc_4,//fruits

            R.drawable.goods_scs_1,R.drawable.goods_scs_2,R.drawable.goods_scs_3,R.drawable.goods_scs_4,//vegetables

            R.drawable.coco,R.drawable.sprit,R.drawable.redbull
    };
    int[] string ={
            R.string.goods_1,R.string.goods_2,R.string.goods_3,R.string.goods_4,//drinks

            R.string.goods_5,R.string.goods_6,R.string.goods_7,R.string.goods_8,//snacks

            R.string.goods_apple,R.string.goods_pear,R.string.goods_banana,R.string.goods_pitaya,
            R.string.goods_sc_1,R.string.goods_sc_2,R.string.goods_sc_3,R.string.goods_sc_4,//fruits

            R.string.goods_scs_1,R.string.goods_scs_2,R.string.goods_scs_3,R.string.goods_scs_4,//vegetables

            R.string.goods_coke,R.string.goods_sprite,R.string.goods_red_bull//others
    };

    float[] price ={
            3.00f,3.00f,3.50f,4.50f,//drinks

            6.80f,6.80f,6.60f,6.60f,//snacks

            9.90f,7.00f,12.0f,16.0f,
            13.0f,20.0f,12.0f,8.00f,//fruits

            5.50f,3.50f,4.70f,9.90f,//vegetables

            5.00f,4.00f,6.00f//others
    };

    int[] species = {
            4,
            4,
            8,
            4,
            3//other
    };

    int[] dialog_logo ={

            R.drawable.apple_dialog,R.drawable.pears_dialog,R.drawable.banana_dialog,R.drawable.pitaya_dialog,
            R.drawable.goods_sc_icon_1,R.drawable.goods_sc_icon_2,R.drawable.goods_sc_icon_3,R.drawable.goods_sc_icon_4,

            R.drawable.goods_scs_icon_1,R.drawable.goods_scs_icon_2,R.drawable.goods_scs_icon_3,R.drawable.goods_scs_icon_4,
    };


    private  Map<String, GvBeans> Goods = new HashMap<>();
    List<GvBeans> drinks=new ArrayList<>();
    List<GvBeans> snacks=new ArrayList<>();
    List<GvBeans> vegetables=new ArrayList<>();
    List<GvBeans> fruits=new ArrayList<>();
    private static GoodsCode instance=null;
    public static GoodsCode getInstance() {
        if(instance==null){
            instance=new GoodsCode();
        }
        return instance;
    }


    private  GoodsCode(){
        for (int i = 0; i < code.length; i++) {
            add(code[i], icon[i], string[i], price[i]);
        }
        for (int i = 0; i < species[0]; i++) {
            drinks.add(Goods.get(code[i]));
        }
        for (int i = 0; i < species[1]; i++) {
            snacks.add(Goods.get(code[i+species[0]]));
        }
        for (int i = 0; i < species[2]; i++) {
            fruits.add(Goods.get(code[i+species[0]+species[1]]).setLogo(dialog_logo[i]));
        }
        for (int i = 0; i < species[3]; i++) {
            vegetables.add(Goods.get(code[i+species[0]+species[1]+species[2]]).setLogo(dialog_logo[i+species[2]]));
        }
    }
    public List<GvBeans> getVegetables() {
        return vegetables;
    }

    public List<GvBeans> getFruits() {
        return fruits;
    }

    public Map<String, GvBeans> getGood(){
        return Goods;
    }

    public List<GvBeans> getSnacks(){

        return snacks;
    }
    public List<GvBeans> getDrinks() {
        return drinks;
    }

     void add(String code, int imageId, int resString, float price) {
        Goods.put(code, new GvBeans(imageId, ResourcesUtils.getString(MyApplication.getInstance(), resString), ResourcesUtils.getString(MyApplication.getInstance(), R.string.units_money) + price));
    }
}
