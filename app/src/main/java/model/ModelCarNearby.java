package model;

/**
 * Created by Administrator on 2017/11/26.
 */

public class ModelCarNearby {

    public String strCarNumber; //车牌号
    public String strCarOwner;  //车主
    public String strCarStatus; //车状态
    public int    iCarScanedTime;//当前车被扫描的次数

    public ModelCarNearby( String carNumber,String carOwner,String carStatus,int crScanedTime ){
        strCarNumber = carNumber;
        strCarOwner = carOwner;
        strCarStatus = carStatus;
        iCarScanedTime = crScanedTime;
    }
}
