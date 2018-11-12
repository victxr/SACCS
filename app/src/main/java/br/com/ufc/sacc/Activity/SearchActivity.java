package br.com.ufc.sacc.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import br.com.ufc.sacc.Activity.Fragments.CadAlunoFragment;
import br.com.ufc.sacc.Activity.Fragments.CadServidorFragment;
import br.com.ufc.sacc.Activity.Fragments.MarcarFragment;
import br.com.ufc.sacc.Activity.Fragments.VizualizarFragment;
import br.com.ufc.sacc.R;

public class SearchActivity extends AppCompatActivity {

    private Button btnOpcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnOpcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SearchActivity.this, btnOpcao);
                popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemTitle = (String) item.getTitle();
                        itemTitle.toLowerCase();

                        Fragment fragment = null;
                        if (itemTitle.equalsIgnoreCase("marcar")) {
                            btnOpcao.setText("Marcar");
                            fragment = new MarcarFragment();
                            //edtCadCurso = fragment.getView().findViewById(R.id.edtCadCurso);
                            //edtCadMatricula = fragment.getView().findViewById(R.id.edtCadMatricula);

                        } else if (itemTitle.equalsIgnoreCase("visualizar")) {
                            btnOpcao.setText("Visualizar");
                            fragment = new VizualizarFragment();
                            //edtCadFuncao = fragment.getView().findViewById(R.id.edtCadFuncao);
                            //edtCadSiac = fragment.getView().findViewById(R.id.edtCadSiac);

                        }
                        return loadFragment(fragment);
                    }

                });
                popupMenu.show();
            }
        });

    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, fragment).commit();
            return true;
        }
        return false;
    }
}
