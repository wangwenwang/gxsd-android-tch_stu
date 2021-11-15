package mobi.gxsd.gxsd_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.android.volley.RequestQueue;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.service.LocationService;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baoyz.actionsheet.ActionSheet;

import changed.org.apache.commons.codec.binary.Base64;
import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.PrivacyBean;
import cn.jiguang.verifysdk.api.VerifyListener;
import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;
import mobi.gxsd.gxsd_android.CheckVersionLib.BaseDialog;
import mobi.gxsd.gxsd_android.Tools.KT_AnimationUtils;
import mobi.gxsd.gxsd_android.Tools.Constants;
import mobi.gxsd.gxsd_android.Tools.DownPicUtil;
import mobi.gxsd.gxsd_android.Tools.LocationApplication;
import mobi.gxsd.gxsd_android.Tools.MPermissionsUtil;
import mobi.gxsd.gxsd_android.Tools.SystemUtil;
import mobi.gxsd.gxsd_android.Tools.Tools;
import mobi.gxsd.gxsd_android_student.R;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.victor.loading.rotate.RotateLoading;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.widget.Toast.LENGTH_LONG;
import static mobi.gxsd.gxsd_android.Tools.Constants.WXLogin_AppID;


public class MainActivity extends FragmentActivity implements
        ActionSheet.ActionSheetListener {

    private final int SDK_PERMISSION_REQUEST = 127;
    private final int RequestAddContact = 1001;
    private String permissionInfo;
    private LocationService locationService;

    public static WebView mWebView;

    public static Context mContext;

    String inputName = "";

    String appName;

    String address;

    double lng;

    double lat;

    // APP当前版本号
    public static String mAppVersion;


    // 微信开放平台APP_ID
    private static final String APP_ID = WXLogin_AppID;

    static public IWXAPI mWxApi;

    public final static String DestFileName = "kdy-qh.apk";
    public final static String ZipFileName = "dist.zip";

    // zip解压路径
    String unZipOutPath;

    //5.0以下使用
    private ValueCallback<Uri> uploadMessage;
    // 5.0及以上使用
    private ValueCallback<Uri[]> uploadMessageAboveL;
    //图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;
    //拍照
    private final static int FILE_CAMERA_RESULT_CODE = 129;
    //拍照图片路径
    private String cameraFielPath;
    private Uri mImageUri;

    private String CURR_ZIP_VERSION = "1.9.3";
    private String WhoCheckVersion;

    //检测版本更新
    private final String TAG_CHECKVERSION = "check_version";
//    private OrderAsyncHttpClient mClient;
    private final int RequestPermission_STATUS_CODE0 = 8800;
    private RequestQueue mRequestQueue;

    // 点击物理返回键的次数
    private int return_key_times = 0;

    private AMapLocationClient locationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    // vue已加载完成，关闭启动图
    private Boolean launch_HIDDEN = false;

    // 启动图
    LinearLayout laumch_Layout;
    // load背景，不可点击
    LinearLayout loadingBgView;

    // 评测对象
    private SpeechEvaluator mIse;
    private boolean is_begin;
    private String xml = "";
    private String appNav = null;

    private RotateLoading rotateLoading;

    MediaPlayer mMediaPlayer;

    private Uri photoUri;

    private AlertDialog alertDialog;

    boolean has_READ_PHONE_NUMBERS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        // 配合启动图，遮挡住自动登录的过程
        laumch_Layout = (LinearLayout)findViewById(R.id.launch_image);
        laumch_Layout.setBackgroundResource(R.drawable.launch_image);
        loadingBgView = (LinearLayout)findViewById(R.id.loadingBgView);
        loadingBgView.setAlpha(0.2f);

        appNav = this.getIntent().getStringExtra("appNav");

        // 修复targetSdkVersion为28时，拍照闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build());
        }

        mContext = this;
        Log.d("LM", "程序启动");

        new Thread() {
            public void run() {

                // 等待用户赋予存储权限
                for (int i = 0; i < 99; i++) {

                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getPackageName()));
                    if (permission) {
                        checkVersion("原生");
                        break;
                    }else{
                        Log.d("LM", "缺少存储权限，等待1秒...");
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        try {
            mAppVersion = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        unZipOutPath = "/data/data/" + getPackageName() + "/upzip/";

        // 首次安装APP，设置ZIP版本号
        String curr_zip_version = Tools.getAppZipVersion(mContext);
        if (curr_zip_version != null && curr_zip_version.equals("")) {

            Tools.setAppZipVersion(mContext, CURR_ZIP_VERSION);
        }

        curr_zip_version = Tools.getAppZipVersion(mContext);
        Log.d("LM", "本地zip版本号：： " + curr_zip_version);

        appName = getResources().getString(R.string.app_name);

        mWebView = (WebView) findViewById((R.id.lmwebview));

        requestPermissions();





        /************************** 高德地图H5辅助定位开始 **************************/
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != locationClient){
            locationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation();
            locationClient.startLocation();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        locationClient = new AMapLocationClient(getApplicationContext());
        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(mLocationOption);
        //启动定位
        locationClient.startLocation();
        //建议在设置webView参数之前调用启动H5辅助定位接口
        locationClient.startAssistantLocation(mWebView);
        /************************** 高德地图H5辅助定位结束 **************************/

        mWebView.getSettings().setTextZoom(100);

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("LM", "当前位置: " + url);

                new Thread() {
                    public void run() {
                        SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
                        String user_info = p.getString("user_info", "");
                        Log.d("LM", "run: ");
                        int delay = 0;
                        // 本地没有账号密码，没有自动登录功能，不能等待自动
                        if(user_info.equals("")){
                            delay = 1500;
                        }else{
                            delay = 10000;
                        }
                        try {
                            sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!launch_HIDDEN) {
                                    launch_HIDDEN = true;
                                    KT_AnimationUtils.showAndHiddenAnimation(laumch_Layout, KT_AnimationUtils.AnimationState.STATE_HIDDEN, 500);
                                }
                            }
                        });
                    }
                }.start();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            // js拔打电话
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("LM", "------------------------: ");

                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                return false;
            }
        });


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }


            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return false;
            }

            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            //            @Override
//            // 设置网页加载的进度条
//            public void onProgressChanged(WebView view, int newProgress) {
//                TestJSLocation.this.getWindow().setFeatureInt(
//                        Window.FEATURE_PROGRESS, newProgress * 100);
//                super.onProgressChanged(view, newProgress);
//            }
            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        // 长按点击事件
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String picFile = (String) msg.obj;
                    String[] split = picFile.split("/");
                    String fileName = split[split.length - 1];
                    try {
                        MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
                    Toast.makeText(mContext, "图片保存成功", Toast.LENGTH_LONG).show();
                }
            };

            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String url = hitTestResult.getExtra();

                            // 下载图片到本地
                            DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener() {

                                @Override
                                public void getDownPath(String s) {
                                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG).show();
                                    Message msg = Message.obtain();
                                    msg.obj = s;
                                    handler.sendMessage(msg);
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });


        // 获取上次启动记录的版本号
        String lastVersion = Tools.getAppLastTimeVersion(mContext);
        Log.d("LM", "上次启动记录的版本号: " + lastVersion);


        boolean isExists = Tools.fileIsExists("/data/data/" + getPackageName() + "/upzip/dist/index.html");
        if (lastVersion.equals(mAppVersion)) {

            Log.d("LM", "html已存在，无需解压");
        } else {

            Log.d("LM", "html不存在或有新版本，开始解压");
            try {
                Tools.unZip(mContext, "dist.zip", unZipOutPath, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("LM", "解压完成，加载html");
        }

        mWebView.loadUrl("file:///data/data/" + getPackageName() + "/upzip/dist/index.html");
        // 老师
//        mWebView.loadUrl("https://gxsd.mobi/gxsdTeacherApk/gxsd-test");
        // 学生
//        mWebView.loadUrl("https://gxsd.mobi/gxsdStudentApk/gxsd-test");
        Tools.setAppLastTimeVersion(mContext);
        lastVersion = Tools.getAppLastTimeVersion(mContext);
        Log.d("LM", "上次启动记录的版本号已设置为: " + lastVersion);

        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollbarOverlay(true);
//        mWebView.loadUrl("http://163xw.com/jsAlbum.html");

        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(this), "CallAndroidOrIOS");

        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);

        initPermission("存储");

        SpeechUtility.createUtility(MainActivity.this, "appid=5f9a1b08");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(mContext);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearCache(true);
    }

    public void WXsharePic(String transaction, final boolean isSession, Bitmap bitmap) {
        // 注册微信登录
        registToWX();
        //初始化WXImageObject和WXMediaMessage对象
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;
        //设置缩略图
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        bitmap.recycle();
        msg.thumbData = this.getBitmapByte(scaledBitmap);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction + Long.toString(System.currentTimeMillis());
        req.message = msg;
        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = isSession ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        //调用api接口发送数据到微信
        mWxApi.sendReq(req);
    }

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1= view.getDrawingCache();// 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height =  activity.getWindowManager().getDefaultDisplay()
                .getHeight();// 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);view.destroyDrawingCache();
        return b;
    }

    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);
    }

    private void initPermission(String permissionName) {

        Log.d("LM", "申请存储权限");

        try {

            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissList = null;
                if (permissionName.equals("存储")) {
                    permissList = new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_SETTINGS};
                }
                else if(permissionName.equals("位置")){
                    permissList = new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.LOCATION_HARDWARE};
                }
                else if(permissionName.equals("麦克风")){
                    permissList = new String[]{
                            Manifest.permission.RECORD_AUDIO};
                }
                else if(permissionName.equals("摄像头")){
                    permissList = new String[]{
                            Manifest.permission.CAMERA};
                }
                else{
                    permissList = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE, Manifest.permission.WRITE_SETTINGS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
                }
                if (MPermissionsUtil.checkAndRequestPermissions(MainActivity.this, permissList, RequestPermission_STATUS_CODE0)) {

                    Log.d("LM", "initPermission: ");
                    new Thread() {
                        public void run() {

                        }
                    }.start();
                }
            } else {

                new Thread() {
                    public void run() {
                        while (true){

                            Log.d("LM", "未获取服务器地址，等待一秒2");
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(Tools.getServerAddress(MainActivity.mContext) != ""){
                                String fds = Tools.getServerAddress(MainActivity.mContext);
                                checkVersion("原生");
                                break;
                            }
                        }
                    }
                }.start();
            }
        } catch (Exception e) {

            Log.d("LM", "initPermissi: " + e.getMessage());
        }
    }


    private void openImageChooserActivity() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("拍照/相册");
        builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        });
        builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initPermission("摄像头");
                new Thread() {
                    public void run() {
                        // 等待用户赋予存储权限
                        for (int i = 0; i < 99; i++) {
                            PackageManager pm = getPackageManager();
                            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                    pm.checkPermission("android.permission.CAMERA", getPackageName()));
                            if (permission) {
                                takeCamera();
                                break;
                            } else {
                                Log.d("LM", "缺少摄像头权限，等待1秒...");
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    //选择图片
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    //当前资源裁剪后保存的目标位置
    private Uri getDestinationUri()  {
        String fileName = String.format("fr_crop_%s.jpg", System.currentTimeMillis());
        File cropFile = null;
        try {
            cropFile = File.createTempFile(fileName, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(cropFile);
    }

    //拍照
    private void takeCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSDCard() && intent.resolveActivity(getPackageManager()) != null) {
            photoUri = getDestinationUri();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                photoUri = FileProvider.getUriForFile(this, "mobi.gxsd.gxsd_android.fileprovider", new File(photoUri.getPath()));
            } else {
                photoUri = getDestinationUri();
            }
            // android11以后强制分区存储，外部资源无法访问，所以添加一个输出保存位置，然后取值操作
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
        }
    }


    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public boolean hasSDCard() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            cameraFielPath = imageFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }


    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }


//    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {


            Log.d("LM", "拍照5.9.1: ");
        }
        else if (requestCode == 100) {

            has_READ_PHONE_NUMBERS = true;
        }
        else if(requestCode== 1089) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentToContact();
            } else {
                Toast.makeText(MainActivity.this,"授权被禁止",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 1088);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("LM", "onActivityResult: ----");

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1088) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name","data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum= phoneNum.replaceAll(" ", "");
                }

                Log.d("LM", "contactName:" + contactName);
                Log.d("LM", "phoneNum:" + phoneNum);

                String url1 = "javascript:SetContactPeople('" + contactName + "','" + phoneNum + "')";
                MainActivity.mWebView.loadUrl(url1);
                Log.d("LM", url1);
            }
        }

        if (null == uploadMessage && null == uploadMessageAboveL) return;

        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法

            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }

        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {

            if (photoUri != null) {
                uploadMessageAboveL.onReceiveValue(new Uri[]{photoUri});
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {

            if (data != null) {
                result = data.getData();
            }
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null)
                results = new Uri[]{Uri.parse(dataString)};
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }


    /**
     * 判断文件是否存在
     */
    public static boolean hasFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        // 应腾讯应用宝要求，禁用30秒位置刷新
//        // TODO Auto-generated method stub
        super.onStart();
//        // -----------location config ------------
//        locationService = ((LocationApplication) getApplication()).locationService;
//        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//        locationService.registerListener(mListener);
//        //注册监听
//        int type = getIntent().getIntExtra("from", 0);
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // 应腾讯应用宝要求，禁用30秒位置刷新
//        // TODO Auto-generated method stub
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
        super.onStop();
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 应腾讯应用宝要求，禁用30秒位置刷新
//            // TODO Auto-generated method stub
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//
//                address = location.getAddrStr();
//                lng = location.getLongitude();
//                lat = location.getLatitude();
//
//                String url = "javascript:SetCurrAddress('" + address + "','" + lng + "','" + lat + "')";
//                MainActivity.mWebView.loadUrl(url);
//                Log.d("LM", url);
//
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
//                /**
//                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
//                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
//                 */
//                sb.append(location.getTime());
//                sb.append("\nlocType : ");// 定位类型
//                sb.append(location.getLocType());
//                Log.d("LM", "" + location.getLocType());
//                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");// 半径
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");// 国家码
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");// 国家名称
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");// 城市编码
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");// 城市
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");// 地址信息
//                sb.append(location.getAddrStr());
//                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
//                sb.append(location.getUserIndoorState());
//                sb.append("\nDirection(not all devices have value): ");
//                sb.append(location.getDirection());// 方向
//                sb.append("\nlocationdescribe: ");
//                sb.append(location.getLocationDescribe());// 位置语义化信息
//                sb.append("\nPoi: ");// POI信息
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    sb.append("\nspeed : ");
//                    sb.append(location.getSpeed());// 速度 单位：km/h
//                    sb.append("\nsatellite : ");
//                    sb.append(location.getSatelliteNumber());// 卫星数目
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 海拔高度 单位：米
//                    sb.append("\ngps status : ");
//                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
//                    sb.append("\ndescribe : ");
//                    sb.append("gps定位成功");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    // 运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\noperationers : ");// 运营商信息
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ");
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//                }
//
//                locationService.stop();
//            }
        }

    };

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] str = new String[2];
            str[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            if (Build.VERSION.SDK_INT >= 30) {
                str[1] = Manifest.permission.READ_PHONE_NUMBERS;
            } else {
                str[1] = Manifest.permission.READ_PHONE_STATE;
            }
            requestPermissions(str, 100);
        }
    }

    private void setUIConfig(boolean isDialogMode) {
        JVerifyUIConfig portrait = getPortraitConfig(isDialogMode);
        // 支持授权页设置横竖屏两套config，在授权页中触发横竖屏切换时，sdk自动选择对应的config加载。
        JVerificationInterface.setCustomUIWithConfig(portrait, null);
    }

    public void showLoadingDialog() {
        dismissLoadingDialog();
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private JVerifyUIConfig getPortraitConfig(boolean isDialogMode) {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        ImageView loadingView = new ImageView(getApplicationContext());
        loadingView.setImageResource(R.drawable.umcsdk_load_dot_white);
        RelativeLayout.LayoutParams loadingParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingView.setLayoutParams(loadingParam);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.umcsdk_anim_loading);
        List<PrivacyBean> beanArrayList = new ArrayList<>();

        if (isDialogMode) {
            // 自定义返回按钮示例
            ImageButton sampleReturnBtn = new ImageButton(getApplicationContext());
            sampleReturnBtn.setImageResource(R.drawable.umcsdk_return_bg);
            RelativeLayout.LayoutParams returnLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            returnLP.setMargins(10, 10, 0, 0);
            sampleReturnBtn.setLayoutParams(returnLP);
            configBuilder.setAuthBGImgPath("main_bg")
                    .setNavColor(0xff0086d0)
                    .setNavText("登录")
                    .setNavTextColor(0xffffffff)
                    .setNavReturnImgPath("umcsdk_return_bg")
                    .setLogoWidth(70)
                    .setLogoHeight(70)
                    .setLogoHidden(false)
                    .setNumberColor(0xff333333)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    .setLogBtnImgPath("umcsdk_login_btn_bg")
                    .setPrivacyNameAndUrlBeanList(beanArrayList)
                    .setAppPrivacyColor(0xff666666, 0xff0085d0)
                    .setUncheckedImgPath("umcsdk_uncheck_image")
                    .setCheckedImgPath("umcsdk_check_image")
                    .setSloganTextColor(0xff999999)
                    .setLogoOffsetY(25)
                    .setLogoImgPath("logo_cm")
                    .setNumFieldOffsetY(130)
                    .setSloganOffsetY(160)
                    .setLogBtnOffsetY(224)
                    .setLogBtnWidth(200)
                    .setNumberSize(18)
                    .setPrivacyState(true)
                    .setNavTransparent(false)
                    .setPrivacyOffsetY(10)
                    .setNeedCloseAnim(true)
                    .setNeedStartAnim(true)
                    .overridePendingTransition(R.anim.activity_slide_enter_bottom, R.anim.activity_slide_exit_bottom)
                    .setDialogTheme(300, 390, 0, 0, false)
                    .setLoadingView(loadingView, animation)
                    .enableHintToast(true, Toast.makeText(getApplicationContext(), "请同意服务条款", Toast.LENGTH_SHORT))
                    .addCustomView(sampleReturnBtn, true, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            rotateLoadHidden();
                        }
                    });
        } else {
            //导航栏按钮示例
            Button navBtn = new Button(this);
            navBtn.setText("导航栏按钮");
            navBtn.setTextColor(0xff3a404c);
            navBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            RelativeLayout.LayoutParams navBtnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            navBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            navBtn.setLayoutParams(navBtnParam);

            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.RED);

            RelativeLayout.LayoutParams o = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 200);
            o.topMargin = 200;
            textView.setLayoutParams(o);

            configBuilder.setAuthBGImgPath("main_bg")
                    .setNavColor(0xff0086d0)
                    .setNavText("登录")
                    .setNavTextColor(0xffffffff)
                    .setNavReturnImgPath("umcsdk_return_bg")
                    .setPrivacyNavReturnBtnPath("qq")
                    .setNeedCloseAnim(true)
                    .setNeedStartAnim(true)
                    .setLogoWidth(70)
                    .setLogoHeight(70)
                    .setLogoHidden(false)
                    .setNumberColor(0xff333333)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    .setLogBtnImgPath("umcsdk_login_btn_bg")
                    .setPrivacyNameAndUrlBeanList(beanArrayList)
                    .setPrivacyWithBookTitleMark(true)
                    .setPrivacyUnderlineText(true)
                    .setPrivacyText("登录","同意")
                    .setAppPrivacyColor(0xff666666, 0xff0085d0)
                    .setUncheckedImgPath("umcsdk_uncheck_image")
                    .setCheckedImgPath("umcsdk_check_image")
                    .setSloganTextColor(0xff999999)
                    .setLogoOffsetY(50)
                    .setLogoImgPath("logo_cm")
                    .setNumFieldOffsetY(190)
                    .setSloganOffsetY(220)
                    .setLogBtnOffsetY(254)
                    .setNumberSize(18)
                    .setPrivacyState(false)
                    .setNavTransparent(true)
                    .setStatusBarHidden(false)
                    .setStatusBarTransparent(true)
                    .setVirtualButtonTransparent(true)
                    .setPrivacyVirtualButtonTransparent(true)
                    .setPrivacyVirtualButtonColor(Color.YELLOW)
                    .setVirtualButtonColor(Color.RED)
                    .setStatusBarDarkMode(true)
                    .setPrivacyCheckboxInCenter(true)
                    .setPrivacyCheckboxHidden(false)
                    .addNavControlView(navBtn, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            Toast.makeText(context, "导航栏自定义按钮", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return configBuilder.build();
    }

    private void loginAuth(boolean isDialogMode) {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (!verifyEnable) {
            Toast.makeText(this, "当前网络环境不支持认证", Toast.LENGTH_SHORT).show();
            return;
        }
        rotateLoadShow();
        setUIConfig(isDialogMode);
        JVerificationInterface.loginAuth(this, true, new VerifyListener() {
            @Override
            public void onResult(final int code, final String content, final String operator) {
                if(code == 6002){
                    rotateLoadHidden();
                    return;
                }
                Log.d("LM", "[" + code + "]message=" + content + ", operator=" + operator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread() {
                            public void run() {
                                getPhone(content);
                            }
                        }.start();
                        rotateLoadHidden();
                    }
                });
            }
        }, new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                Log.d("LM", "[onEvent]. [" + cmd + "]message=" + msg);
            }
        });
    }

    private void rotateLoadShow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rotateLoading.start();
            }
        });
    }

    private void rotateLoadHidden(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rotateLoading.stop();
            }
        });
    }

    private void getPhone(String loginToken){

        String Strurl = "https://www.gxsd.mobi/gxsd-prod/system/jiGuang/loginTokenVerifyBody";
        rotateLoadShow();
        HttpURLConnection conn=null;
        try {
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject param = new JSONObject();
            param.put("loginToken", loginToken);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            writer.write(param.toString());
            writer.flush();
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
                Log.d("LM","通过token换取手机号");
                InputStream in=conn.getInputStream();
                String resultStr = Tools.inputStream2String(in);
                resultStr = URLDecoder.decode(resultStr,"UTF-8");
                try {
                    JSONObject jsonObj = (JSONObject)(new JSONParser().parse(resultStr));
                    Log.i("LM",jsonObj.toJSONString() + "\n" + jsonObj.getClass());
                    long code = (long) jsonObj.get("code");
                    if(code == 200) {
                        String data = (String) jsonObj.get("data");
                        login(data);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                in.close();
            }
            else {
                Log.i("LM","get请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            rotateLoadHidden();
            conn.disconnect();
        }
    }

    private void login(String account){

        String Strurl = "https://www.gxsd.mobi/gxsd-prod/read/readUser/login?account=" + account + "&yzm=999999" + "&accountType=1";
        Log.d("LM", "登录链接: " + Strurl);
        rotateLoadShow();
        HttpURLConnection conn=null;
        try {
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){

                Log.d("LM","登录成功");
                InputStream in=conn.getInputStream();
                String resultStr = Tools.inputStream2String(in);
                resultStr = URLDecoder.decode(resultStr,"UTF-8");

                try {
                    JSONObject jsonObj = (JSONObject)(new JSONParser().parse(resultStr));
                    Log.i("LM",jsonObj.toJSONString() + "\n" + jsonObj.getClass());
                    long code = (long) jsonObj.get("code");
                    String message = (String)jsonObj.get("message");
                    if(code == 200) {

                        JSONObject data = (JSONObject) jsonObj.get("data");
                        String jsonArray = data.toJSONString().toString();
                        String LMEncoding = URLEncoder.encode(jsonArray);
                        String WXBind_YES_Ajax_PARAMS = LMEncoding;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String LMurl = "javascript:WXBind_YES_Ajax('" + WXBind_YES_Ajax_PARAMS + "')";
                                MainActivity.mWebView.loadUrl(LMurl);
                            }
                        });
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                in.close();
            }
            else {
                Log.i("LM","get请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            rotateLoadHidden();
            conn.disconnect();
        }
    }

    // js调用java
    private class JsInterface extends Activity {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        // 经纬坐标转地址，抽象函数
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            }
        };

        @JavascriptInterface
        public void callAndroid(String exceName) {

            Log.d("LM", "执行:" + exceName);

            if (exceName.equals("检查APP和VUE版本更新")) {
                new Thread() {
                    public void run() {

                        checkVersion("vue");

                    }
                }.start();
            }
            else if (exceName.equals("一键登录")) {
                new Thread() {
                    public void run() {
                        quick_login(false);
                    }
                }.start();
            }
        }

        //在js中调用window.CallAndroidOrIOS.callAndroid(name)，便会触发此方法。
        @JavascriptInterface
        public void callAndroid(String exceName, final String inputName) {

            Log.d("LM", "执行:" + exceName + "    " + "输入框:" + inputName);
            MainActivity.this.inputName = inputName;

            final SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);


            if (exceName.equals("微信登录")) {

                Log.d("LM", "微信登录");

                // 注册
                registToWX();

                new Thread() {
                    public void run() {

                        if (!mWxApi.isWXAppInstalled()) {
                            Log.d("LM", "您还未安装微信客户端");
                            return;
                        } else {
                            Log.d("LM", "微信客户端已安装");
                        }
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";//官方固定写法
                        req.state = "wechat_sdk_gxsd";//自定义一个字串

                        mWxApi.sendReq(req);
                    }
                }.start();
            } else if (exceName.equals("登录页面已加载")) {

                Log.d("LM", "登录页面已加载");

                final String u = sp.getString("UserName", "");
                final String p = sp.getString("Password", "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String url = "javascript:SetUserNameAndPassword('" + u + "','" + p + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);

                        url = "javascript:VersionShow('" + Tools.getVerName(mContext) + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);

                        url = "javascript:Device_Ajax('Android')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);

                        url = "javascript:PackageName_Ajax('" +  mContext.getPackageName() + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
                        String user_info = p.getString("user_info", "");
                        String url = "javascript:LM_AndroidIOSToVue_userInfo('" + user_info + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.i("LM", "发送 | 用户信息");
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
                        String user_info = p.getString("user_habbit", "");
                        String url = "javascript:LM_AndroidIOSToVue_userHabbit('" + user_info + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.i("LM", "发送 | 用户习惯");
                    }
                });



            } else if (exceName.equals("获取当前位置页面已加载")) {

                Log.d("LM", "获取当前位置页面已加载");
                // 应腾讯应用宝要求，禁用30秒位置刷新
//                locationService.start();
            } else if (exceName.equals("查看路线")) {

                Log.d("LM", "查看路线");

            }
            // 服务器地址
            else if(exceName.equals("服务器地址")) {

                Tools.setServerAddress(mContext, inputName);
            }
            // 调用通讯录
            else if(exceName.equals("调用通讯录")) {
//
                new Thread() {
                    public void run() {

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {

                                //**版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取**
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //ContextCompat.checkSelfPermission() 方法 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
                                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        // 若不为GRANTED(即为DENIED)则要申请权限了
                                        // 申请权限 第一个为context 第二个可以指定多个请求的权限 第三个参数为请求码
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{android.Manifest.permission.READ_CONTACTS},
                                                1089);
                                    } else {
                                        //权限已经被授予，在这里直接写要执行的相应方法即可
                                        Log.d("LM", "通讯录已授权");
                                        intentToContact();
                                    }
                                }else {
                                    // 低于6.0的手机直接访问
                                    intentToContact();
                                }
//                            }
//                        });
                    }
                }.start();
            }
            // 存储用户信息
            else if(exceName.equals("用户信息")) {

                // 接收用户信息，表明登录成功，结束启动页（预留0.8秒给vue渲染，渐变时长0.5秒）
                if(!launch_HIDDEN){
                    launch_HIDDEN = true;
                    new Thread() {
                        public void run() {
                            try {
                                sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    KT_AnimationUtils.showAndHiddenAnimation(laumch_Layout, KT_AnimationUtils.AnimationState.STATE_HIDDEN, 500);
                                }
                            });
                        }
                    }.start();
                }
                SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", Context.MODE_MULTI_PROCESS);
                p.edit().putString("user_info", inputName).commit();
                Log.i("LM","接收 | 用户信息");
            }
            // 存储用户习惯
            else if(exceName.equals("用户习惯")) {

                // 接收用户习惯
                SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", Context.MODE_MULTI_PROCESS);
                p.edit().putString("user_habbit", inputName).commit();
                Log.i("LM","接收 | 用户习惯");
            }
            // 存储文章阅读时长
            else if(exceName.equals("文章阅读时长")) {

                SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", Context.MODE_MULTI_PROCESS);
                p.edit().putString("read_accum_time", inputName).commit();
                Log.i("LM","接收 | 文章阅读时长");
            }
            // 取文_章阅读时长
            else if(exceName.equals("取文_章阅读时长")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
                        String accum_time = p.getString("read_accum_time", "");
                        String url = "javascript:LM_AndroidIOSToVue_read_accum_time('" + accum_time + "')";
                        MainActivity.mWebView.loadUrl(url);
                        Log.i("LM", "发送 | 取文_章阅读时长");
                    }
                });
            }
            // 分享检测成绩-聊天界面
            else if(exceName.equals("分享检测成绩-聊天界面")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        WXsharePic("fd", true, takeScreenShot(MainActivity.this));
                    }
                });
            }
            // 分享检测成绩-朋友圈
            else if(exceName.equals("分享检测成绩-朋友圈")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        WXsharePic("fd", false, takeScreenShot(MainActivity.this));
                    }
                });
            }
            // 分享图片-聊天界面
            else if(exceName.equals("分享图片-聊天界面")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        WXsharePic("fd", true, takeScreenShot(MainActivity.this));
                    }
                });
            }
            // 分享图片-朋友圈
            else if(exceName.equals("分享图片-朋友圈")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        WXsharePic("fd", true, takeScreenShot(MainActivity.this));
                    }
                });
            } else if (exceName.equals("邀请加入班级")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通过appId得到IWXAPI这个对象
                        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, WXLogin_AppID);
                        // 检查手机或者模拟器是否安装了微信
                        if (!wxapi.isWXAppInstalled()) {
                            Toast.makeText(mContext, "您还没有安装微信", LENGTH_LONG).show();
                            return;
                        }

                        // 初始化一个WXWebpageObject对象
                        WXWebpageObject webpageObject = new WXWebpageObject();
                        // 填写网页的url
                        webpageObject.webpageUrl = inputName;

                        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
                        WXMediaMessage msg = new WXMediaMessage(webpageObject);
                        // 填写网页标题、描述、位图
                        msg.title = "家长们，快帮助孩子加入我的班级吧！";
                        msg.description = "让孩子提高阅读速度";
                        // 如果没有位图，可以传null，会显示默认的图片
                        Bitmap bitmap =  BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/icon.png"));
                        msg.setThumbImage(bitmap);

                        // 构造一个Req
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        // transaction用于唯一标识一个请求（可自定义）
                        req.transaction = "webpage";
                        // 上文的WXMediaMessage对象
                        req.message = msg;
                        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
                        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
                        req.scene = SendMessageToWX.Req.WXSceneSession;

                        // 向微信发送请求
                        wxapi.sendReq(req);
                    }
                });
            }
        }

        @JavascriptInterface
        public String getWXAppInstalled() {
            // 注册
            registToWX();
            if (mWxApi.isWXAppInstalled()) {
                return "yes";
            } else {
                return "no";
            }
        }

            @JavascriptInterface
        public void callAndroid(String exceName, String u, String p) {

            Log.d("LM", "执行:" + exceName + "    " + "SetCurrAddressSetCurrAddress名:" + u + "    " + "密码:" + p);

            if (exceName.equals("记住帐号密码")) {

                if (u != null && p != null && !u.equals("") && !p.equals("")) {

                    SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);
                    sp.edit().putString("UserName", u).apply();
                    sp.edit().putString("Password", p).apply();
                }
                new Thread() {
                    public void run() {

                        checkVersion("原生");

                    }
                }.start();
            }
        }

        @JavascriptInterface
        public void callAndroid(String exceName, final String lng1, final String lat1, final String address1) {

            Log.d("LM", "执行:" + exceName + "    " + "经度:" + lng + "    " + "纬度:" + lat + "    " + "地址:" + address);

            if (exceName.equals("导航")) {

                Log.d("LM", "导航");

                lng = Double.valueOf(lng1).doubleValue();
                lat = Double.valueOf(lat1).doubleValue();
                address = address1;


                new Thread() {

                    public void run() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                List list = new ArrayList();
                                if (SystemUtil.isInstalled(mContext, "com.autonavi.minimap")) {

                                    list.add("高德地图");
                                }
                                if (SystemUtil.isInstalled(mContext, "com.baidu.BaiduMap")) {

                                    list.add("百度地图");
                                }

                                if (list.size() == 2) {

                                    ActionSheet.createBuilder(MainActivity.this, getSupportFragmentManager())
                                            .setCancelButtonTitle("取消")
                                            .setOtherButtonTitles("高德地图", "百度地图")
                                            .setCancelableOnTouchOutside(true)
                                            .setListener(MainActivity.this).show();
                                } else if (list.size() == 1) {

                                    if (list.get(0).equals("高德地图")) {

                                        Log.d("LM", "调用高德地图");
                                        minimap(mContext, lng, lat, address, appName);
                                    } else if (list.get(0).equals("百度地图")) {

                                        Log.d("LM", "调用百度地图");
                                        BaiduMap(mContext, lng, lat, address, appName);
                                    }
                                } else {

                                    Toast.makeText(mContext, "未检索到本机已安装‘百度地图’或‘高德地图’App", LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }.start();
            }
        }
        @JavascriptInterface
        public void share_url(final String url, final String title, final String description) {

            Log.d("LM", "微信分享网页");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 通过appId得到IWXAPI这个对象
                    IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, WXLogin_AppID);
                    // 检查手机或者模拟器是否安装了微信
                    if (!wxapi.isWXAppInstalled()) {
                        Toast.makeText(mContext, "您还没有安装微信", LENGTH_LONG).show();
                        return;
                    }
                    // 初始化一个WXWebpageObject对象
                    WXWebpageObject webpageObject = new WXWebpageObject();
                    // 填写网页的url
                    webpageObject.webpageUrl = url;

                    // 用WXWebpageObject对象初始化一个WXMediaMessage对象
                    WXMediaMessage msg = new WXMediaMessage(webpageObject);
                    // 填写网页标题、描述、位图
                    msg.title = title;
                    msg.description = description;
                    // 如果没有位图，可以传null，会显示默认的图片
                    Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/icon.png"));
                    msg.setThumbImage(bitmap);

                    // 构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    // transaction用于唯一标识一个请求（可自定义）
                    req.transaction = "webpage";
                    // 上文的WXMediaMessage对象
                    req.message = msg;
                    // SendMessageToWX.Req.WXSceneSession是分享到好友会话
                    // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
                    req.scene = SendMessageToWX.Req.WXSceneSession;

                    // 向微信发送请求
                    wxapi.sendReq(req);
                }
            });
        }

        @JavascriptInterface
        public void callandroid_ise(String exceName, String txt) {
            if (exceName.equals("录音")) {
                initPermission("麦克风");
                new Thread() {
                    public void run() {

                        // 等待用户赋予存储权限
                        for (int i = 0; i < 99; i++) {

                            PackageManager pm = mContext.getPackageManager();
                            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.RECORD_AUDIO", mContext.getPackageName()));
                            if (permission) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        read_click(txt);
                                    }
                                });
                                break;
                            } else {
                                Log.d("LM", "缺少麦克风权限，等待1秒...");
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            } else if (exceName.equals("请求评测结果")) {
                Log.d("LM", "提交成绩");
                uploadMp3();
            } else if (exceName.equals("销毁录音")) {
                Log.d("LM", "销毁录音");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(is_begin){
                            stopEva();
                            is_begin = false;
                            String url = "javascript:LM_AndroidIOSToVue_stopRecord('stop')";
                            MainActivity.mWebView.loadUrl(url);
                        }
                    }
                });
            }else if (exceName.equals("播放范读")) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(txt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.prepareAsync();
                mMediaPlayer.start();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
            } else if (exceName.equals("停止范读")) {
                mMediaPlayer.stop();
            }
        }

        // 获取推荐人账号
        @JavascriptInterface
        public void receiveRecommend() {

            // 飞读速思APP不需要此功能
            if(mContext.getPackageName().equals("mobi.fdss.student")){
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
                        @Override
                        public void onInstall(AppData data) {
                            if(data.getParamsData() != null){

                                String query = data.getParamsData();
                                Map query_pairs = new HashMap<String, String>();
                                if(query.length()>0) {
                                    final String[] pairs = query.split("&");
                                    for (String pair : pairs) {
                                        final int idx = pair.indexOf("=");
                                        //如果等号存在且不在字符串两端，取出key、value
                                        if (idx > 0 && idx < pair.length() - 1) {
                                            String key = null;
                                            try {
                                                key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            String value = null;
                                            try {
                                                value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            query_pairs.put(key, value);
                                        }
                                    }
                                }
                                String url = "javascript:LM_AndroidIOSToVue_Recommend('" + (String) query_pairs.get("channel") + "','" + (String) query_pairs.get("tel") + "')";
                                MainActivity.mWebView.loadUrl(url);
                            }
                        }
                        @Override
                        public void onError(int code, String msg) {
                            Toast.makeText(mContext, "Get install trace info error. code=" + code + ",msg=" + msg, LENGTH_LONG).show();
                        }
                    });
                }
            });
        }


        @JavascriptInterface
        public void callandroid_pay(String partnerId, String prepayId, String nonceStr, String timeStamp, String _package, String sign, String appId) {

            Log.d("LM", "callandroid_pay: ");
            // 微信公众平台appid
            mWxApi = WXAPIFactory.createWXAPI(mContext, null);
            mWxApi.registerApp(appId);
            new Thread() {
                public void run() {

                    if (!mWxApi.isWXAppInstalled()) {
                        Log.d("LM", "您还未安装微信客户端");
                        return;
                    } else {
                        Log.d("LM", "微信客户端已安装");
                    }
                    PayReq req = new PayReq();
                    req.appId = appId;
                    req.partnerId = partnerId;
                    req.prepayId = prepayId;
                    req.nonceStr = nonceStr;
                    req.timeStamp = timeStamp;
                    req.packageValue = _package;
                    req.sign = sign;
                    mWxApi.sendReq(req);
                }
            }.start();
        }

        // 退出App
        @JavascriptInterface
        public void exitApp() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            });
        }

        // 国网平台
        @JavascriptInterface
        public void eduyun() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(MainActivity.mContext, TwoActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void quick_login(boolean auto){

        // 飞读速思APP不需要此功能
        if(mContext.getPackageName().equals("mobi.fdss.student")){
            return;
        }

        if(auto){
            SharedPreferences p = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
            String user_info = p.getString("user_info", "");
            if(!user_info.equals("")) {
                return;
            }
        }
        int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT == 30) {
                result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_NUMBERS);
            } else {
                result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
                Log.d("LM", "run: ");
            }
            if (result != PackageManager.PERMISSION_GRANTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(mContext, "缺少[获取手机号码]权限", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                loginAuth(true);
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        if(index == 0) {

            Log.d("LM", "调用高德地图");
            minimap(mContext, lng, lat, address, appName);
        }else if(index == 1) {

            Log.d("LM", "调用百度地图");
            BaiduMap(mContext, lng, lat, address, appName);
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    private static void BaiduMap(Context mContext, double lng, double lat, String address, String appName) {

        //跳转到百度导航
        try {
            Intent baiduintent = Intent.parseUri("intent://map/direction?" +
                    "origin=" + "" +
                    "&destination=" + address +
                    "&mode=driving" +
                    "&src=Name|AppName" +
                    "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
            mContext.startActivity(baiduintent);
        } catch (URISyntaxException e) {
            Log.d("LM", "URISyntaxException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void minimap(Context mContext, double lng, double lat, String address, String appName) {
        //跳转到高德导航
        Intent autoIntent = new Intent();
        try {
            autoIntent.setData(Uri
                    .parse("androidamap://route?" +
                            "sourceApplication=" + appName +
                            "&slat=" + "" +
                            "&slon=" + "" +
                            "&dlat=" + lat +
                            "&dlon=" + lng +
                            "&dname=" + address +
                            "&dev=0" +
                            "&m=2" +
                            "&t=0"
                    ));
        } catch (Exception e) {
            Log.i("LM", "高德地图异常" + e);
        }
        mContext.startActivity(autoIntent);
    }

    public void checkVersion(String who) {

        this.WhoCheckVersion = who;

        Log.d("LM", "检查apk及zip版本");
        String Strurl = "";
        switch(mContext.getPackageName()){
            case "mobi.fdss.student" :
                Strurl = "https://www.gxsd.mobi/gxsd-prod/read/task/getAppUpdateByAppType?appType=studentAndroidFdss";
                break;
            case "mobi.gxsd.gxsd_android_student" :
                Strurl = "https://www.gxsd.mobi/gxsd-prod/read/task/getAppUpdateByAppType?appType=studentAndroid";
                break;
            case "mobi.gxsd.gxsd_android_teacher" :
                Strurl = "https://www.gxsd.mobi/gxsd-prod/read/task/getAppUpdateByAppType?appType=teacherAndroid";
                break;
        }

        HttpURLConnection conn=null;
        try {

            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){

                InputStream in=conn.getInputStream();
                String resultStr = Tools.inputStream2String(in);
                resultStr = URLDecoder.decode(resultStr,"UTF-8");

                try {
                    JSONObject jsonObj = (JSONObject)(new JSONParser().parse(resultStr));
                    Log.i("LM",jsonObj.toJSONString() + "\n" + jsonObj.getClass());
                    long code = (Long) jsonObj.get("code");

                    String apkDownloadUrl = null;
                    String server_apkVersion = null;
                    String zipDownloadUrl = null;
                    String server_zipVersion = null;
                    String server_appContent = null;
                    if (code == 200) {

                        JSONArray array = (JSONArray) jsonObj.get("data");
                        if(array.size() > 0){
                            JSONObject dict = (JSONObject) array.get(0);
                            apkDownloadUrl = (String) dict.get("appDownloadUrl");
                            server_apkVersion = (String) dict.get("appVersion");
                            zipDownloadUrl = (String) dict.get("vueDownloadUrl");
                            server_zipVersion = (String) dict.get("vueVersion");
                            server_appContent = (String) dict.get("appContent");
                            server_appContent = (server_appContent == null) ? "" : server_appContent;
                        }
                    }
                    if (server_apkVersion != null && apkDownloadUrl != null) {
                        try {
                            String current_apkVersion = mAppVersion;
                            Log.d("LM","server_apkVersion:" + server_apkVersion + "\tcurrent_apkVersion:" + current_apkVersion);
                            int compareVersion = Tools.compareVersion(server_apkVersion, current_apkVersion);
                            if (compareVersion == 1) {
                                createUpdateDialog(current_apkVersion, server_apkVersion, apkDownloadUrl, server_appContent);
                            } else {
                                Log.d("LM", "apk为最新版本");
                                String curr_zipVersion = Tools.getAppZipVersion(mContext);
                                compareVersion = Tools.compareVersion(server_zipVersion, curr_zipVersion);
                                if (compareVersion == 1) {
                                    Log.d("LM", "服务器zip版本：" + server_zipVersion + "    " + "本地zip版本：" + CURR_ZIP_VERSION);
                                    CURR_ZIP_VERSION = server_zipVersion;
                                    Log.d("LM", "更新zip...");

                                    final String finalZipDownloadUrl = zipDownloadUrl;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            showUpdataZipDialog(finalZipDownloadUrl);
                                        }
                                    });

                                } else {
                                    Log.d("LM", "zip为最新版本");
                                    if(WhoCheckVersion.equals("vue")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setTitle("");                                                builder.setMessage("已经是最新版本！");
                                                builder.setPositiveButton("确定", null);
                                                builder.show();
                                            }
                                        });
                                    } else{
                                        new Thread() {
                                            public void run() {
                                                // 等待用户赋予【获取手机号码】权限
                                                for (int i = 0; i < 20; i++) {
                                                    if (has_READ_PHONE_NUMBERS) {
                                                        quick_login(true);
                                                        break;
                                                    }else{
                                                        Log.d("LM", "缺少【获取手机号码】权限，已等待" + i + "秒...");
                                                    }
                                                    try {
                                                        sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }.start();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.d("LM", "NameNotFoundException" + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                in.close();
            }
            else {
                Log.d("LM", "检查版本接口|queryAppVersion.do|请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            conn.disconnect();
        }
        Log.d("LM", "checkVersion: ");
    }

    /******************************************************** HTML版本更新功能 ********************************************************/
    /**
     * 弹出对话框
     */
    protected void showUpdataZipDialog(final String downUrl) {

        downLoadZip(downUrl);
    }

    protected void downLoadZip(final String downUrl) {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("");
        pd.show();
        pd.setOnKeyListener(onKeyListener);
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = Tools.getFileFromServer(downUrl, pd);
                    Log.d("LM", "Zip下载完毕，地址：" + file.getPath());

                    // 更新ZIP版本号
                    Tools.setAppZipVersion(mContext, CURR_ZIP_VERSION);
                    Log.d("LM", "zip更新成功，设置版本号为：" + CURR_ZIP_VERSION);

                    Log.d("LM", "取出验证为：" + Tools.getAppZipVersion(mContext));


                    try {
                        Log.d("LM", "SD卡开始解压...");
                        Tools.UnZipFolder("/storage/emulated/0/dist.zip", unZipOutPath);
                        Log.d("LM", "SD卡完成解压...");
                    } catch (Exception e) {
                        Log.d("LM", "SD卡解压异常..." + e.getMessage());
                        e.printStackTrace();
                    }

                    pd.dismiss(); //结束掉进度条对话框

                    new Thread() {
                        public void run() {

                            for (int i = 0; i < 5; i++) {
                                try {
                                    sleep(1 * 300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("LM", "开始刷新HTML  " + i);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mWebView.reload();
                                    }
                                });
                                Log.d("LM", "完成刷新HTML  " + i);
                            }
                        }
                    }.start();
                    quick_login(true);
                } catch (Exception e) {

                    Log.d("", "run: ");
                }
            }
        }.start();
    }

    // 下载进度时，点击屏幕不可取消
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            }
            return false;
        }
    };

    /**
     * 强制更新操作
     * 通常关闭整个activity所有界面，这里方便测试直接关闭当前activity
     */
    private void forceUpdate() {

        Toast.makeText(this, "请升级应用", Toast.LENGTH_SHORT).show();
    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
    private CustomVersionDialogListener createCustomDialogTwo() {
        return (context, versionBundle) -> {
            BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
            TextView textView = baseDialog.findViewById(R.id.tv_msg);
            textView.setText(versionBundle.getContent());
            // 强制更新，不允许退出弹窗，不允许安卓自带返回键
            // baseDialog.setCanceledOnTouchOutside(true);
            // 设置true，才会执行forceUpdate方法
            baseDialog.setCancelable(false);
            return baseDialog;
        };
    }

    /**
     * 版本更新对话框
     *
     * @param currentVersion 当前版本versionName
     * @param version        最新版本versionName
     * @param downUrl        最新版本安装包下载url
     */
    public void createUpdateDialog(String currentVersion, String version, final String downUrl, String server_appContent) {

        Log.d("LM", "createUpdateDialog: ");
        Log.d("LM", server_appContent);
        AllenVersionChecker
                .getInstance()
                .downloadOnly(
                        UIData.create()
                                .setDownloadUrl(downUrl)
                                .setTitle("更新版本")
                                .setContent(server_appContent))
                .setForceUpdateListener(() -> {
                    forceUpdate();
                })
                .setCustomVersionDialogListener(createCustomDialogTwo())
                .executeMission(mContext);
    }

    /**
     * 返回上一页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 登录页时不允许返回上一页
            String curURL = mWebView.getUrl();
            String orgURL = mWebView.getOriginalUrl();
            if (curURL.equals("file:///data/data/mobi.gxsd.gxsd_android/upzip/dist/index.html#/")) {

                Log.d("LM", "禁止返回上一页1：" + curURL);
                return false;
            }

            String pkName = this.getPackageName();
            // 学生端首页
            String Index_stu = "file:///data/data/" + pkName + "/upzip/dist/index.html#/index_stu";
            // 老师端首页
            String Index_tch = "file:///data/data/" + pkName + "/upzip/dist/index.html#/index_tch";
            // 学生任务
            String Waybill = "file:///data/data/" + pkName + "/upzip/dist/index.html#/task_stu";
            // 老师任务
            String task_tch = "file:///data/data/" + pkName + "/upzip/dist/index.html#/task_tch";
            // 阅读存折
            String ReportForms = "file:///data/data/" + pkName + "/upzip/dist/index.html#/read_passbook";
            // 我的
            String HomeIndex = "file:///data/data/" + pkName + "/upzip/dist/index.html#/my";

            // 主菜单时不允许返回上一页
            if (
                    curURL.indexOf(Index_stu + "?") != -1 || curURL.equals(Index_stu) ||
                            curURL.indexOf(Index_tch + "?") != -1 || curURL.equals(Index_tch) ||
                            curURL.indexOf(Waybill + "?") != -1 || curURL.equals(Waybill) ||
                            curURL.indexOf(task_tch + "?") != -1 || curURL.equals(task_tch) ||
                            curURL.indexOf(ReportForms + "?") != -1 || curURL.equals(ReportForms) ||
                            curURL.indexOf(HomeIndex + "?") != -1 || curURL.equals(HomeIndex)
                    ) {

                Log.d("LM", "到达程序根节点后，点击安卓自带返回键，返回到桌面" + curURL);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return false;
            }
            mWebView.goBack();
            Log.d("LM", "curURL: " + curURL);
            Log.d("LM", "orgURL: " + orgURL);
        }
        return false;
    }

    public void read_click(String text) {
        is_begin = !is_begin;
        if (is_begin) {
            Log.d("LM", "开始录制");
            xml = "";
            startEva(text);
            String url = "javascript:LM_AndroidIOSToVue_startRecord()";
            MainActivity.mWebView.loadUrl(url);
        } else {
            Log.d("LM", "结束录制");
            loadingBgView.setVisibility(View.VISIBLE);
            rotateLoadShow();
            stopEva();
            String url = "javascript:LM_AndroidIOSToVue_stopRecord('complete')";
            MainActivity.mWebView.loadUrl(url);
        }
    }

    // 结束评测
    public void stopEva() {
        mIse.stopEvaluating();
    }

    public void uploadMp3() {
        if(xml.equals("")){
            Toast.makeText(mContext, "音频处理中，请稍候...", Toast.LENGTH_LONG).show();
            Log.d("LM", "音频处理中，请稍候...");
            return;
        }
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingBgView.setVisibility(View.VISIBLE);
                        rotateLoadShow();
                        Toast.makeText(mContext, "音频解析中，请稍候...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
        Base64 base64 = new Base64();
        String result_xmlBase64 = "";
        try {
            Log.d("LM", "转码开始");
            result_xmlBase64 = base64.encodeToString(xml.getBytes("UTF-8"));
            Log.d("LM", "转码完成");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "开始提交，请稍候...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
        Log.d("LM", "上传音频文件");
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        try {
            URL url = new URL("https://www.gxsd.mobi/gxsd-prod/system/upload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20 * 1000);
            conn.setConnectTimeout(20 * 1000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", "utf-8");  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + "utf-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                int res = conn.getResponseCode();
                Log.e("LM", "response code:" + res);
                if(res == 200) {
                    Log.e("LM", "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    String result = sb1.toString();
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = (JSONObject)(new JSONParser().parse(result));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("LM", "result : " + result);
                    String mp3Url = jsonObj.get("data").toString();
                    String finalResult_xmlBase6 = result_xmlBase64;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String url_to_vue = "javascript:LM_AndroidIOSToVue_read_gsw_xml_result('" + finalResult_xmlBase6 + "','" + mp3Url + "')";
                            MainActivity.mWebView.loadUrl(url_to_vue);
                        }
                    });
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingBgView.setVisibility(View.INVISIBLE);
                            rotateLoadHidden();
                        }
                    });
                }
            }.start();
        }
    }

    // 开始评测
    private void startEva(String text) {
        mIse = SpeechEvaluator.createEvaluator(MainActivity.this, null);
        if (mIse == null) {
            return;
        }
        SharedPreferences pref = getSharedPreferences("ise_settings", MODE_PRIVATE);
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
        mIse.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, "read_chapter");
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, "10000");
        mIse.setParameter(SpeechConstant.VAD_EOS, "10000");
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, "complete");
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT_AUE,"opus");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
        Log.d("LM", "--------: " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
        //通过writeaudio方式直接写入音频时才需要此设置
//        mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");
        int ret = mIse.startEvaluating(text, null, mEvaluatorListener);
        Log.d("LM", "onClick: ");
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
		/*FlowerCollector.onResume(IseDemo.this);
		FlowerCollector.onPageStart(TAG);*/
        super.onResume();
    }

    @Override
    protected void onPause() {
/*		// 开放统计 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IseDemo.this);*/
        super.onPause();
    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {
        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d("LM", "评测音频采集结束:" + isLast);
            loadingBgView.setVisibility(View.INVISIBLE);
            rotateLoadHidden();
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                xml = builder.toString();
            }
        }

        @Override
        public void onError(SpeechError error) {
            if(error != null) {
                Toast.makeText(mContext, "error:"+ error.getErrorCode() + "," + error.getErrorDescription(), Toast.LENGTH_LONG).show();
            } else {
                Log.d("LM", "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d("LM", "evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d("LM", "evaluator stoped");
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.d("LM", "返回音频数据："+ data.length + "当前音量：" + volume);
            String url = "javascript:LM_AndroidIOSToVue_recordVolume('" + volume + "')";
            MainActivity.mWebView.loadUrl(url);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
}