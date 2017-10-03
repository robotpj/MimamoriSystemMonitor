package com.example.nakajima.mimamorisystemmonitor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * プッシュ通知を受け取るサービスです。
 * <p/>
 * on 2016/08/05.
 */

public class MimamoriSystemFirebaseMessagingService extends FirebaseMessagingService {
    private static final String EXTRA_IMG = "img";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_MESSAGE = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        if(TextUtils.isEmpty(data.get(EXTRA_MESSAGE))) return;
        if(TextUtils.isEmpty(data.get(EXTRA_TITLE))) data.put(EXTRA_TITLE,getResources().getString(R.string.app_name));

        sendNotification(data);
    }


    private void sendNotification(Map<String, String> data) {
        // 履歴削除,activity再利用
        final Intent intent = new Intent(this, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //intent.putExtra(WebViewActivity.EXTRA_NOTIFY_IMG, data.get(EXTRA_IMG));
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap img = downloadImage(data.get(EXTRA_IMG));

        int width = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        int height = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data.get(EXTRA_TITLE))
                .setContentText(data.get(EXTRA_MESSAGE))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent)
                .setLargeIcon(Bitmap.createScaledBitmap(img, width, height, true));

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final int notifyId = getNotifyId();
        notificationManager.notify(notifyId, builder.build());
    }

    // notification複数表示
    private int getNotifyId() {
        Calendar calendar = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.JAPAN).format(calendar.getTime()));
    }

    private Bitmap downloadImage(String address) {
        Bitmap bmp = null;

        try {
            URL url = new URL( address );

            // HttpURLConnection インスタンス生成
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);

            // リクエストメソッド
            urlConnection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);

            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");

            // 接続
            urlConnection.connect();

            int resp = urlConnection.getResponseCode();

            switch (resp){
                case HttpURLConnection.HTTP_OK:
                    InputStream is = urlConnection.getInputStream();
                    byte[] bytes = readAll(is);
                    is.close();
                    bmp = decodeImage(bytes);
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("MSFMS", "downloadImage error");
            e.printStackTrace();
        }

        return bmp;
    }

    private byte[] readAll(InputStream inputStream) throws java.io.IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        while(true) {
            int len = inputStream.read(buffer);
            if(len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        return bout.toByteArray();
    }

    private Bitmap decodeImage(byte[] bytes) {
        List<Byte> bytesList = new ArrayList<>();
        for (int i=0; i<bytes.length; ++i) {
            bytesList.add(bytes[i]);
        }
        bytesList.subList(0, 16).clear();
        Collections.reverse(bytesList);
        byte[] signature = new byte[]{(byte)0x89, (byte)0x50, (byte)0x4e, (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A};
        for (int i=0; i<signature.length; ++i) {
            bytesList.add(i, signature[i]);
        }

        return BitmapFactory.decodeByteArray(list2ByteArray(bytesList), 0, bytesList.size());
    }

    private byte[] list2ByteArray(List<Byte> list) {
        byte[] ret = new byte[list.size()];

        int i = 0;
        for (byte n: list) {
            ret[i++] = n;
        }

        return ret;
    }

}
