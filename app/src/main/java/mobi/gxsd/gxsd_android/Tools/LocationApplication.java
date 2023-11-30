package mobi.gxsd.gxsd_android.Tools;


import com.baidu.location.service.LocationService;
import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import cn.jiguang.verifysdk.api.JVerificationInterface;
//import cn.net.shoot.sharetracesdk.ShareTrace;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class LocationApplication extends Application {
	public LocationService locationService;
    public Vibrator mVibrator;


    /**
     * MyApplication 实例
     */
    private static LocationApplication instance;

    /**
     * MyApplication 的上下文
     */
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        LocationApplication.instance = this;
        LocationApplication.applicationContext = this;

        /***
         * 初始化定位sdk，建议在Application中创建
         */
//        locationService = new LocationService(getApplicationContext());
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        if(!LocationApplication.instance.getPackageName().equals("mobi.gxsd.gxsd_android_teacher")) {
            // 初始化ShareTrace
//            ShareTrace.init(this);
            JVerificationInterface.init(this);
            JVerificationInterface.setDebugMode(true);
        }

//        LocationApplication tContext = this;
//        Log.d("LM", "申请IMSI权限");
//        try {
//            new Thread() {
//                public void run() {
//                    while (true){
//                        Log.d("LM", "777777777777: ");
//                        try {
//                            sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("LM", Tools.getAgreeGetAuth(LocationApplication.instance).toString());
//                        if(Tools.getAgreeGetAuth(LocationApplication.instance)) {
//                            // 初始化ShareTrace
//                            ShareTrace.init(LocationApplication.instance);
//                            JVerificationInterface.init(LocationApplication.instance);
//                            JVerificationInterface.setDebugMode(true);
//                            break;
//                        }
//                    }
//                }
//            }.start();
//        } catch (Exception e) {
//            Log.d("LM", "initPermissi: " + e.getMessage());
//        }
    }

    /**
     * 获取 Application 的实例
     *
     * @return Application 实例
     */
    public static LocationApplication getInstance() {
        return instance;
    }


    /**
     * 获取 Application 的上下文
     *
     * @return Application 的上下文
     */
    public static Context getAppContext() {
        return applicationContext;
    }
}
