package com.example.litingting.gps_baidumap;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.Poi;

public class MainActivity extends AppCompatActivity {

    private final static String TAG=".MainActivity ";
    private TextView mTextView;
    private Button btnStopService;
    public LocationClient mLocationClient=null;
//    public BDLocationListener myListener=new MyLocationListener();
    public MyLocationListener myListener=new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=(TextView)findViewById(R.id.GPSmessage);
        btnStopService=(Button)findViewById(R.id.stopService);

        mLocationClient=new LocationClient(getApplicationContext());//声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        mLocationClient.start();//启动定位SDK

        if (mLocationClient!=null&&mLocationClient.isStarted()){
            mLocationClient.requestLocation();
        }else {
            Log.d(TAG,"LocationClient is null or not started");
        }

        /**
         * 双击事件：关掉百度位置服务
         */
        final GestureDetector gestureDetector=new GestureDetector(MainActivity.this,new GestureDetector.SimpleOnGestureListener(){
            /**
             * 发生确定的单击时执行
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                return false;
            }

            /**
             * 双击发生时的通知
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                mTextView.setText(myListener.getResult());
                return super.onDoubleTap(e);
            }

            /**
             * 双击手势过程中发生的事件，包括按下、移动和抬起事件
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });

        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.stop();
            }
        });


//        mTextView.setText(myListener.getResult());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }

    /**
     * 设置定位参数包括：定位模式（高精度定位模式，低功耗定位模式和仅用设备定位模式），
     * 返回坐标类型，是否打开GPS，是否返回地址信息、位置语义化信息、POI信息等等。
     */
    private void initLocation(){

       //LocationClientOption类，该类用来设置定位SDK的定位方式
        LocationClientOption option=new LocationClientOption();

        /**
         * 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
         * 低功耗定位模式：不会使用GP，只会使用网络定位（WIFI和基站定位）；
         * 仅用设备定位模式：不需连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd0911");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
}
