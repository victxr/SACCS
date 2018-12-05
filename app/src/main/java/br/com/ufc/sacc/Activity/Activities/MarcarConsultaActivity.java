package br.com.ufc.sacc.Activity.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import br.com.ufc.sacc.Activity.Fragments.Marcar.MarcarNutricionistaFragment;
import br.com.ufc.sacc.Activity.Fragments.Marcar.MarcarPsicologaFragment;
import br.com.ufc.sacc.Activity.Fragments.Marcar.MarcarServicoSocialFragment;
import br.com.ufc.sacc.R;

public class MarcarConsultaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar);

        inicializarComponentes();

        loadFragment(new MarcarPsicologaFragment());
    }

    private void inicializarComponentes() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.item_psicologa);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()){
            case R.id.item_psicologa:
                fragment = new MarcarPsicologaFragment();
                break;

            case R.id.item_nutricionista:
                fragment = new MarcarNutricionistaFragment();
                break;

            case R.id.item_servico_social:
                fragment = new MarcarServicoSocialFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            return true;
        }
        return false;
    }
}