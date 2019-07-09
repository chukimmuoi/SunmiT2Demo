package com.sunmi.sunmit2demo.bean;

/**
 * L3自定义配置类
 *
 * @author:sunhengzhi
 * @date:2018-12-06
 */
public class Config {

    /**
     * 交易过程中是否显示UI界面(不包括结果页)
     */
    private Boolean processDisplay;
    /**
     * 是否展示交易结果页
     */
    private Boolean resultDisplay;
    /**
     * 是否打印小票
     */
    private Boolean printTicket;
    /**
     * 指定签购单上的订单号类型
     */
    private String printIdType;
    /**
     * 备注
     */
    private String remarks;

    public Boolean getProcessDisplay() {
        return processDisplay;
    }

    public void setProcessDisplay(Boolean processDisplay) {
        this.processDisplay = processDisplay;
    }

    public Boolean getResultDisplay() {
        return resultDisplay;
    }

    public void setResultDisplay(Boolean resultDisplay) {
        this.resultDisplay = resultDisplay;
    }

    public Boolean getPrintTicket() {
        return printTicket;
    }

    public void setPrintTicket(Boolean printTicket) {
        this.printTicket = printTicket;
    }

    public String getPrintIdType() {
        return printIdType;
    }

    public void setPrintIdType(String printIdType) {
        this.printIdType = printIdType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Config{" +
                "processDisplay=" + processDisplay +
                ", resultDisplay=" + resultDisplay +
                ", printTicket=" + printTicket +
                ", printIdType='" + printIdType + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
