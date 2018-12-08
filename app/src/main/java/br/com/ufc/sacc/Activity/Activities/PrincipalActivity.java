package br.com.ufc.sacc.Activity.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import br.com.ufc.sacc.Activity.Fragments.NavigationDrawer.*;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth autenticacao;
    private String emailAlunoLogado;
    private Usuario usuarioLogado = new Usuario();
    private ItemConsultaMarcada itemConsultaMarcada= new ItemConsultaMarcada();
    private TextView nomeUser, emailUser;

    private static final String QTD_CONSULTA = "br.com.ufc.sacc.Activity.Activities.qtd_consulta";
    private int qtdConsultasMarcadas = 0;
    private ArrayList<ItemConsultaMarcada> listaConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listaConsultas = new ArrayList<>();

        iniciarFirebase();
        pegarUsuarioLogado();
        pegarConsultasMarcadas();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        qtdConsultasMarcadas = prefs.getInt(QTD_CONSULTA, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nomeUser = header.findViewById(R.id.nomeUser);

        emailUser = header.findViewById(R.id.emailUser);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_search:
                fragment = new ConsultaFragment();
                break;
            case R.id.nav_message:
                fragment = new MessageFragment();
                break;
            case R.id.nav_help:
                Intent intent_faq = new Intent(getApplicationContext(), FaqActivity.class);
                startActivity(intent_faq);
                break;
            case R.id.nav_cad_consulta:
                Intent intent_cad_consulta = new Intent(getApplicationContext(), CadastroConsultaActivity.class);
                startActivity(intent_cad_consulta);
                break;
            case R.id.cad_faq:
                Intent intent_cad_faq = new Intent(getApplicationContext(), CadastroFaqActivity.class);
                startActivity(intent_cad_faq);
                break;
            case R.id.nav_location:
                fragment = new LocationFragment();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                abrirTelaSobre();
                break;
            case R.id.sair:
                deslogar();
                break;
        }
        return true;
    }

    private void deslogar() {
        Toast.makeText(this, "Você deslogou!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirTelaSobre() {
        Intent intent = new Intent(PrincipalActivity.this, SobreActivity.class);
        startActivity(intent);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(PrincipalActivity.this);
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

    private void pegarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        emailAlunoLogado = autenticacao.getCurrentUser().getEmail();
        Log.d("Email do cara logado:", emailAlunoLogado);

        databaseReference.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    usuarioLogado = objSnap.getValue(Usuario.class);
                    if(usuarioLogado.getEmail().equals(emailAlunoLogado)) {
                        nomeUser.setText(usuarioLogado.getNome());
                        emailUser.setText(usuarioLogado.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void pegarConsultasMarcadas() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        emailAlunoLogado = autenticacao.getCurrentUser().getEmail();
        Log.d("Email do cara logado:", emailAlunoLogado);

        databaseReference.child("ItemConsultaMarcada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaConsultas.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    itemConsultaMarcada = objSnap.getValue(ItemConsultaMarcada.class);
                    listaConsultas.add(itemConsultaMarcada);
                    if(itemConsultaMarcada.getTipo().equals("Psicóloga")) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        observer();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//        editor.putInt(QTD_CONSULTA, listaConsultas.size());
//        editor.apply();
//    }

    protected void sharedPreference() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putInt(QTD_CONSULTA, listaConsultas.size());
        editor.apply();
    }

    public void observer() {
        Log.d("Lista de contatos", ""+listaConsultas.size());
        Log.d("Qtd Consultas", ""+qtdConsultasMarcadas);

        if (listaConsultas.size() > qtdConsultasMarcadas) {
            databaseReference = FirebaseDatabase.getInstance().getReference("ItemFaq");
            ChildEventListener childListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Intent intent = new Intent(PrincipalActivity.this, SobreActivity.class);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getActivity(PrincipalActivity.this, 0, intent, 0);

                    NotificationCompat.Builder builderNotification = new NotificationCompat.Builder(PrincipalActivity.this);
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
                        Ringtone toque = RingtoneManager.getRingtone(PrincipalActivity.this, som);
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
        }
        sharedPreference();
        atualizaQtdConsulta();
    }

    private void atualizaQtdConsulta() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        qtdConsultasMarcadas = prefs.getInt(QTD_CONSULTA, 0);
    }

}
