package com.appsferry.app;

import android.app.Application;
import android.content.Context;

import com.appsferry.adapter.AdapterSDK;
import com.appsferry.adapter.AhParams;
import com.appsferry.analytics.AfAnalytics;

/**
 * description:
 *
 * @author Floyd
 * @since 2023/5/31
 */
public class MyApplication extends Application {
    private final static String APPID = "bnZ3YWRlbW86TFVCQU4";
    private final static String CV = "LUBAN1.0.01_Android";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AdapterSDK.initSdk(
            new AhParams()
                .withAppid(APPID)
                .withCv(CV)
                .withSmid( "Dukjhjgfghjkljhgfjkjhgfhjkhgfhjkjhfghj")
                .withHttpParams("https://your.bizhost.com", "https://your.upload_host.com", "your.report_url.com")
        );
        AfAnalytics.init();
        AfAnalytics.setPrivacyAgreement(true);
    }
}
