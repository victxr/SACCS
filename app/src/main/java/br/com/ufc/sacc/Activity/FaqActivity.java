package br.com.ufc.sacc.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import br.com.ufc.sacc.Activity.Fragments.NutricionistaFragment;
import br.com.ufc.sacc.Activity.Fragments.PsicologaFragment;
import br.com.ufc.sacc.Activity.Fragments.ServicoSocialFragment;
import br.com.ufc.sacc.R;

public class FaqActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.item_psicologa);

        loadFragment(new PsicologaFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()){
            case R.id.item_psicologa:
                fragment = new PsicologaFragment();
                break;

            case R.id.item_nutricionista:
                fragment = new NutricionistaFragment();
                break;

            case R.id.item_servico_social:
                fragment = new ServicoSocialFragment();
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
