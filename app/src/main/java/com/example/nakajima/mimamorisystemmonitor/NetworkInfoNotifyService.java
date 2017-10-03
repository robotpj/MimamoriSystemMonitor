package com.example.nakajima.mimamorisystemmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by nakajima on 2017/09/28.
 */

public class NetworkInfoNotifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
