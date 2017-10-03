package com.example.nakajima.mimamorisystemmonitor;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * プッシュ通知に使用する登録トークンの生成、更新をハンドルするサービスです。
 * <p/>
 * on 2016/08/05.
 */

public class MimamoriSystemFirebaseInstanceIdService extends FirebaseInstanceIdService {
    /**
     * ログ出力用
     */
    private static final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendRegistrationToServer();
    }

    private void sendRegistrationToServer() {
        // Add custom implementation, as needed.
        startService(new Intent(this, RegistrationIntentService.class));
    }
}
