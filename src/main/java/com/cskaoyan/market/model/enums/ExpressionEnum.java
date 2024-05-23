package com.cskaoyan.market.model.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/22/14:42
 * @Description:
 */
public enum ExpressionEnum {
    ZTO("中通快递","ZTO","1171111"),
    STO("申通快递","STO","1172222"),
    YTO("圆通快递","YTO","1173333"),
    EMS("EMS","EMS","1174444"),
    SF("顺丰快递","SF","1175555"),
    HKTY("百世快递","HKTY","1176666"),
    JD("京东快递","JD","1177777"),
    YD("韵达快递","YD","1178888"),
    ZJS("宅急送","ZJS","1179999"),
    TNT("TNT快递","TNT","1170000"),
    UPS("UPS快递","UPS","1170001"),
    DHL("DHL快递","DHL","1170002"),
    FEDEX("FEDEX快递","FEDEX","1170003"),
    OTHER("其他快递","OTHER","1170004");



    private final String expName;
    private final String expCode;
    private final String expNo;

    ExpressionEnum(String expName, String expCode, String expNo){
        this.expName = expName;
        this.expCode = expCode;
        this.expNo = expNo;
    }

    public String getExpName() {
        return expName;
    }

    public String getExpCode() {
        return expCode;
    }

    public String getExpNo() {
        return expNo;
    }
}
