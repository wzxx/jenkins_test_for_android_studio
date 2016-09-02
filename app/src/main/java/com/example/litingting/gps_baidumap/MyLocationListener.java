package com.example.litingting.gps_baidumap;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

/**
 * BDLocation类，封装了定位SDK的定位结果，在BDLocationListener的onReceiveLocation方法中获取。
 * 通过该类用户可以获取error code，位置的坐标，精度半径等信息。
 *
 * Created by litingting on 16/7/26.
 */
public class MyLocationListener implements BDLocationListener {

    private String result;

    public String getResult(){
        return result;
    }
    /**
     * 有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
     * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
     * @param location
     */
    @Override
    public void onReceiveLocation(BDLocation location){
        StringBuffer stringBuffer=new StringBuffer(256);

        stringBuffer.append("time: ");
        stringBuffer.append(location.getTime());
        stringBuffer.append("\nerror code: ");
        /**
         * 为了方便查看，我直接把文档部分内容拷贝过来查看
         *
         * public int getLocType ( ):获取error code
         *
         * 返回值：
         * 61 ： GPS定位结果，GPS定位成功。
         * 62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
         * 63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
         * 65 ： 定位缓存的结果。
         * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
         * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
         * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
         *
         * 161： 网络定位结果，网络定位定位成功。
         * 162： 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
         * 167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
         * 502： key参数错误，请按照说明文档重新申请KEY。
         * 505： key不存在或者非法，请按照说明文档重新申请KEY。
         * 601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
         * 602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
         * 501～700：key验证失败，请按照说明文档重新申请KEY。
         */
        stringBuffer.append(location.getLocType());
        stringBuffer.append("\nlatitude: ");
        stringBuffer.append(location.getLatitude());
        stringBuffer.append("\nlontitude: ");
        stringBuffer.append(location.getLongitude());
        stringBuffer.append("\nradius: ");
        stringBuffer.append(location.getRadius());
        if (location.getLocType()==BDLocation.TypeGpsLocation){//GPS定位结果
            stringBuffer.append("\nspeed: ");
            stringBuffer.append(location.getSpeed());// 单位：公里每小时
            stringBuffer.append("\nsatellite: ");
            stringBuffer.append(location.getSatelliteNumber());
            stringBuffer.append("\nheight: ");
            stringBuffer.append(location.getAltitude());// 单位：米
            stringBuffer.append("\ndirection: ");
            stringBuffer.append(location.getDirection());// 单位度
            stringBuffer.append("\naddr: ");
            stringBuffer.append(location.getAddrStr());
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("gps定位成功");
        }else if(location.getLocType()==BDLocation.TypeNetWorkLocation){//网络定位结果
            stringBuffer.append("\naddr: ");
            stringBuffer.append(location.getAddrStr());
            //运营商信息
            stringBuffer.append("\noperationers: ");
            stringBuffer.append(location.getOperators());
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("网络定位成功");
        }else if (location.getLocType()==BDLocation.TypeOffLineLocation){// 离线定位结果
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("离线定位成功，离线定位结果也是有效的");
        }else if (location.getLocType()==BDLocation.TypeServerError){
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        }else if (location.getLocType()==BDLocation.TypeNetWorkException){
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("网络不同导致定位失败，请检查网络是否通畅");
        }else if (location.getLocType()==BDLocation.TypeCriteriaException){
            stringBuffer.append("\ndescribe: ");
            stringBuffer.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        stringBuffer.append("\nlocation describe: ");
        stringBuffer.append(location.getLocationDescribe());//位置语义化信息
        List<Poi> list=location.getPoiList();//POI数据
        if (list!=null){
            stringBuffer.append("\npoilist size= : ");
            stringBuffer.append(list.size());
            for (Poi p:list){
                stringBuffer.append("\npoi= : ");
                stringBuffer.append(p.getId()+" "+p.getName()+" "+p.getRank());
            }
        }
        result=stringBuffer.toString();
//        Log.i("BaiduLocationApiDem",stringBuffer.toString());
        Log.i("BaiduLocationApiDem",result);
    }
}
