package model;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ModelCarBlackList {
    public String strCarNumber; //车牌号
    public String strCarOwner;  //车主
    public String strCarStatus; //车状态
    public String  strAddTime; //报案时间

    public ModelCarBlackList( String carNumber,String carOwner,String carStatus,String add_time ){
        strCarNumber = carNumber;
        strCarOwner = carOwner;
        strCarStatus = carStatus;
        strAddTime = add_time;
    }
}
