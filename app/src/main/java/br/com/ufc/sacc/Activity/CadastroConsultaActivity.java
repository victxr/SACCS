package br.com.ufc.sacc.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsulta;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.UUID;

public class CadastroConsultaActivity extends AppCompatActivity {

    int selected;
    ListView listViewConsulta;
    Spinner spinner;
    Spinner spinnerDiaDaSemana;
    final String[] spinerListFuncao = {"Assistente Social", "Psicologia", "Nutrição"};
    final String[] spinerDiaDaSemana = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta"};

    FirebaseDatabase fireBaseDatabase;
    DatabaseReference databaseReference;

    private ArrayList<ItemConsulta> listaItens = new ArrayList<>();
    private ArrayAdapter<ItemConsulta> arrayAdapterItemConsulta;

    ItemFaq itemSelecionado;
    int posicao;
    int posicaoDiaDaSemana;

    CheckBox ab_manha;
    CheckBox cd_manha;
    CheckBox ab_tarde;
    CheckBox cd_tarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_consulta_activity);

        inicializarComponentes();
        iniciarFirebase();
        dispararAtualizacao();

        listViewConsulta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = (ItemFaq) parent.getItemAtPosition(position);

                selected = position;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicao = spinner.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDiaDaSemana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicaoDiaDaSemana = spinnerDiaDaSemana.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void inicializarComponentes() {
        selected = -1;

        ab_manha = findViewById(R.id.ab_manha);
        cd_manha = findViewById(R.id.cd_manha);
        ab_tarde = findViewById(R.id.ab_tarde);
        cd_tarde = findViewById(R.id.cd_tarde);

        listViewConsulta = findViewById(R.id.listViewConsulta);
        listViewConsulta.setSelector(android.R.color.holo_green_light);

        spinner = findViewById(R.id.edtFuncao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroConsultaActivity.this, android.R.layout.simple_dropdown_item_1line, spinerListFuncao);
        spinner.setAdapter(adapter);

        spinnerDiaDaSemana = findViewById(R.id.edtDiaDaSemana);
        ArrayAdapter<String> adapterDiaDaSemana = new ArrayAdapter<>(CadastroConsultaActivity.this, android.R.layout.simple_dropdown_item_1line, spinerDiaDaSemana);
        spinnerDiaDaSemana.setAdapter(adapterDiaDaSemana);

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(CadastroConsultaActivity.this);
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemConsulta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItens.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    ItemConsulta itemConsulta = objSnap.getValue(ItemConsulta.class);

                    listaItens.add(itemConsulta);
                }
                arrayAdapterItemConsulta = new ArrayAdapter<ItemConsulta>(CadastroConsultaActivity.this, android.R.layout.simple_list_item_1, listaItens);
                listViewConsulta.setAdapter(arrayAdapterItemConsulta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_consulta, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ItemConsulta itemConsulta;
        String diaDaSemana, horario, tipo;
        String uid;

        switch (item.getItemId()) {
            case R.id.addConsulta:

                diaDaSemana = spinerDiaDaSemana[posicaoDiaDaSemana];
                tipo = spinerListFuncao[posicao];

                if (ab_manha.isChecked()) {
                    uid = UUID.randomUUID().toString();
                    horario = ab_manha.getText().toString();
                    itemConsulta = new ItemConsulta(uid, diaDaSemana, horario, tipo);
                    listaItens.add(itemConsulta);
                }
                if (cd_manha.isChecked()) {
                    uid = UUID.randomUUID().toString();
                    horario = cd_manha.getText().toString();
                    itemConsulta = new ItemConsulta(uid, diaDaSemana, horario, tipo);
                    listaItens.add(itemConsulta);
                }
                if (ab_tarde.isChecked()) {
                    uid = UUID.randomUUID().toString();
                    horario = ab_tarde.getText().toString();
                    itemConsulta = new ItemConsulta(uid, diaDaSemana, horario, tipo);
                    listaItens.add(itemConsulta);
                }
                if (cd_tarde.isChecked()) {
                    uid = UUID.randomUUID().toString();
                    horario = cd_tarde.getText().toString();
                    itemConsulta = new ItemConsulta(uid, diaDaSemana, horario, tipo);
                    listaItens.add(itemConsulta);
                }
                for (int i = 0, tam = listaItens.size(); i < tam; i++) {
                    databaseReference.child("ItemConsulta").child(listaItens.get(i).getUid()).setValue(listaItens.get(i));
                }
                alert("Item adicionado.");
                break;
        }
        return true;
    }


    public void alert(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
