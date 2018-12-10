package br.com.ufc.sacc.Activity.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import br.com.ufc.sacc.Activity.Fragments.FAQ.FaqNutricionistaFragment;
import br.com.ufc.sacc.Activity.Fragments.FAQ.FaqPsicologaFragment;
import br.com.ufc.sacc.Activity.Fragments.FAQ.FaqServicoSocialFragment;
import br.com.ufc.sacc.R;

public class FaqActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        inicializarComponentes();

        loadFragment(new FaqPsicologaFragment());
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
                fragment = new FaqPsicologaFragment();
//                Intent intent = new Intent(FaqActivity.this, ServiceNotificar.class);
//                startService(intent);
                break;

            case R.id.item_nutricionista:
                fragment = new FaqNutricionistaFragment();
                break;

            case R.id.item_servico_social:
                fragment = new FaqServicoSocialFragment();
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
