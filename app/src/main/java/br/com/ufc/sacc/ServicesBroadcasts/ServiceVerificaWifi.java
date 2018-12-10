package br.com.ufc.sacc.ServicesBroadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import br.com.ufc.sacc.Activity.Activities.VisualizarConsultasActivity;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ServiceVerificaWifi extends Service {

    private Context context;
    private WifiManager wifiManager;

    public ArrayList<Downloader> threads = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Script", "OnCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Script", "OnStarCommand");
        Downloader worker = new Downloader(startId);

        context = getApplicationContext();

        worker.start();
        threads.add(worker);

        return super.onStartCommand(intent, flags, startId);
    }

    public class Downloader extends Thread {

        int startId;
        boolean ativo = true;

        public Downloader(int startId) {
            this.startId = startId;
        }

        public void run() {

            do {
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if(!wifiManager.isWifiEnabled()){
                    Log.d("MENSAGEM", "Por favor, ative seu wifi. O SACCS precisa de acesso a internet para funcionar completamente.");
                    Intent intent = new Intent("Wifi");
                    intent.putExtra("wifi", "Wifi desligado");
                    sendBroadcast(intent);
                }

                stopSelf(startId);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (ativo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0, tam = threads.size(); i < tam; i++) {
            threads.get(i).ativo = false;
        }
    }
}
