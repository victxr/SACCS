package br.com.ufc.sacc.ServicesBroadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastWifi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Script", "onReceive()");
        intent = new Intent("SERVICO_BROADCAST");
        context.startService(intent);
    }
}
