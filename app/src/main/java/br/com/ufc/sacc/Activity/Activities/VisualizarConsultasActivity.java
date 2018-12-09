package br.com.ufc.sacc.Activity.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapter;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapterVisualizarConsulta;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class VisualizarConsultasActivity extends AppCompatActivity {

    private int selected;
    private ArrayList<ItemConsultaMarcada> listaItens;

    private ExpandableListAdapterVisualizarConsulta adapter;
    private ExpandableListView expandableListViewItens;
    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_consultas);

        inicializarComponentes();
        iniciarFirebase();
        dispararAtualizacao();

        expandableListViewItens.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                selected = groupPosition;
                return false;
            }
        });
    }

    private void inicializarComponentes() {
        selected = -1;

        expandableListViewItens = findViewById(R.id.listViewVisualizarConsultas);
        expandableListViewItens.setSelector( android.R.color.holo_purple);

        listaItens = new ArrayList<>();
    }

    private void iniciarFirebase() {
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

    private void dispararAtualizacao() {

        databaseReference.child("ItemConsultaMarcada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItens.clear();
                for(DataSnapshot objSnap: dataSnapshot.getChildren()){
                    ItemConsultaMarcada itemMarcada = objSnap.getValue(ItemConsultaMarcada.class);

                    listaItens.add(itemMarcada);
                }

                listaItens = ordenarPorTipo(listaItens);
                adapter = new ExpandableListAdapterVisualizarConsulta(VisualizarConsultasActivity.this, listaItens);
                expandableListViewItens.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private ArrayList<ItemConsultaMarcada> ordenarPorTipo(ArrayList<ItemConsultaMarcada> lista) {
        ArrayList<ItemConsultaMarcada> listaRetorno = new ArrayList<>();

        for(int i = 0, tam = lista.size(); i < tam; i++){
            if(lista.get(i).getTipo().equals("Nutricionista")){
                listaRetorno.add(lista.get(i));
            }
        }
        for(int i = 0, tam = lista.size(); i < tam; i++){
            if(lista.get(i).getTipo().equals("Psicóloga")){
                listaRetorno.add(lista.get(i));
            }
        }
        for(int i = 0, tam = lista.size(); i < tam; i++){
            if(lista.get(i).getTipo().equals("Serviço Social")){
                listaRetorno.add(lista.get(i));
            }
        }
        return listaRetorno;
    }

}
