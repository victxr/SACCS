package br.com.ufc.sacc.ServicesBroadcasts;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ExpandableListView;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapter;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ServiceDownloadFaq extends Service {

    DownloadManager downloadManager;

    FirebaseDatabase fireBaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<ItemFaq> listaItens = new ArrayList<ItemFaq>();
    ExpandableListAdapter adapter;
    ExpandableListView expandableListViewItens;
    Context context;
    int selected;

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

                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://optclean.com.br/wp-content/uploads/2018/04/fe7c88e53390305442ae1ce3661f27ed.jpg");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);

                Log.d("Script", "BAIXANDO ARQUIVO");

                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(reference));
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.d("Script", "STATUS  - baixado");
                        stopSelf(startId);
                    }
                }

//                databaseReference.child("ItemFaq").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        listaItens.clear();
//                        for(DataSnapshot objSnap: dataSnapshot.getChildren()){
//                            ItemFaq itemFaq = objSnap.getValue(ItemFaq.class);
//
//                            if(itemFaq.getTipo().equals("Psicologia")) listaItens.add(itemFaq);
//
//                        }
//                        adapter = new ExpandableListAdapter(context, listaItens);
//                        expandableListViewItens.setAdapter(adapter);
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });

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
