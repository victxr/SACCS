package br.com.ufc.sacc.Activity.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import br.com.ufc.sacc.Activity.Fragments.NavigationDrawer.MapaFragment;
import br.com.ufc.sacc.R;
import com.google.android.gms.maps.SupportMapFragment;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_mapa, new MapaFragment());
        transaction.commit();

    }
}
