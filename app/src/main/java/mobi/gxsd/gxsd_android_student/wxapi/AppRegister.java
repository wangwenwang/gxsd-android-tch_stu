package mobi.gxsd.gxsd_android_student.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {
    public AppRegister() {
    }

    public void onReceive(Context context, Intent intent) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, (String)null);
        msgApi.registerApp("wx99c047234ba1b87a");
    }
}
