package com.jack.fibonacci;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.math.BigInteger;


/**
 * Created by Jack on 10/19/2016.
 */

public class CalculateService extends Service {

    private String mResultValue;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(getString(R.string.intent_filter_action));
        broadcastIntent.putExtra(getString(R.string.result), mResultValue);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int userNumber = intent.getIntExtra(getString(R.string.user_number), 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mResultValue = fibonacci(userNumber);
                stopSelf();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    String fibonacci(int n) {
        BigInteger x = new BigInteger("1");
        BigInteger y = new BigInteger("0");
        BigInteger ans = new BigInteger("0");
        for (int i = 1; i <= n; i++) {
            ans = x.add(y);
            x = y;
            y = ans;
        }
        return ans.toString();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
