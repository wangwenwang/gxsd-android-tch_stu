package mobi.gxsd.gxsd_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LaumchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(LaumchActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
