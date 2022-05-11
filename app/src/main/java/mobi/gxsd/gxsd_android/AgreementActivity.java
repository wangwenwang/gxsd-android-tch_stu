package mobi.gxsd.gxsd_android;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mobi.gxsd.gxsd_android_student.R;

public class AgreementActivity extends FragmentActivity {

    public static WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        mWebView = (WebView) findViewById((R.id.agreementWebView));
        mWebView.getSettings().setTextZoom(100);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        Intent i = getIntent();
        String url = i.getStringExtra("webview_url");
        mWebView.loadUrl(url);
    }

    public void backClicked(View view) {
        finish();
    }
}