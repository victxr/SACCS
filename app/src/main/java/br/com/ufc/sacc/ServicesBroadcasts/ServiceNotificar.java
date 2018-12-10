package br.com.ufc.sacc.ServicesBroadcasts;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.ufc.sacc.Activity.Activities.PrincipalActivity;
import br.com.ufc.sacc.Activity.Activities.VisualizarConsultasActivity;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ServiceNotificar extends Service {

    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;

    private Context context;

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

        iniciarFirebase();
        context = getApplicationContext();

        Downloader worker = new Downloader(startId);
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
                databaseReference = FirebaseDatabase.getInstance().getReference("ItemConsultaMarcada");
                ChildEventListener childListener = new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Intent intent = new Intent(context, VisualizarConsultasActivity.class);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                        NotificationCompat.Builder builderNotification = new NotificationCompat.Builder(context);
                        builderNotification.setTicker("SACSS");
                        builderNotification.setContentTitle("NOVA CONSULTA MARCADA!");
                        builderNotification.setContentText("Você tem uma nova consulta, clique aqui.");
                        builderNotification.setSmallIcon(R.drawable.ic_notification);
                        builderNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sacss));
                        builderNotification.setContentIntent(pendingIntent);

                        Notification notification = builderNotification.build();
                        notification.vibrate = new long[]{150, 300, 150, 600};
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(R.drawable.ic_notification, notification);

                        try {
                            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone toque = RingtoneManager.getRingtone(context, som);
                            toque.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                databaseReference.addChildEventListener(childListener);

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

    private void iniciarFirebase() {
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

}
